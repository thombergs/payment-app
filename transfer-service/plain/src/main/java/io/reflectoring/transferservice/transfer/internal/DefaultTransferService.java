package io.reflectoring.transferservice.transfer.internal;

import io.reflectoring.transferservice.database.api.TransferDatabase;
import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.Currency;
import io.reflectoring.transferservice.transfer.api.Money;
import io.reflectoring.transferservice.transfer.api.StartTransferRequest;
import io.reflectoring.transferservice.transfer.api.Transfer;
import io.reflectoring.transferservice.transfer.api.TransferId;
import io.reflectoring.transferservice.transfer.api.TransferNotFoundException;
import io.reflectoring.transferservice.transfer.api.TransferService;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.OutgoingEvents;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;

@Component
public class DefaultTransferService implements TransferService {

    private final OutgoingEvents outgoingEvents;
    private final TransferDatabase transferDatabase;

    public DefaultTransferService(
            OutgoingEvents outgoingEvents,
            TransferDatabase transferDatabase) {
        this.outgoingEvents = outgoingEvents;
        this.transferDatabase = transferDatabase;
    }

    @Override
    public Optional<Transfer> findTransfer(TransferId transferId) {
        return transferDatabase.findById(transferId);
    }

    @Override
    public void updateTransfer(Transfer transfer) {
        transferDatabase.update(transfer);
    }

    @Override
    public void continueTransfer(Transfer transfer) {

    }

    @Override
    public TransferId startNewTransfer(TransferId transferId, StartTransferRequest request) {
        Transfer transfer = new Transfer(
                transferId,
                outgoingEvents,
                new AccountId(request.sourceAccountId()),
                new AccountId(request.targetAccountId()),
                new Money(request.amount(), Currency.valueOf(request.currency())),
                new Locale(request.transactionLocation())
        );

        // Persist transfer first thing so that it's available in the database for any
        // events that come in after we start the transfer.
        updateTransfer(transfer);

        // TODO: Does the call above need to be in its own database transaction that is
        // committed before we call startTransfer()? After startTransfer(), events for that
        // transfer start to come in and they wouldn't find this transfer in the database!


        // Start the transfer.
        // This starts sending events out.
        transfer.startTransfer();

        // Starting now, we'll have incoming events for this transfer. We can't control
        // how fast or how slow they may come in.

        // TODO: what happens if an event comes in and the transfer is still
        // in the NOT_STARTED state (i.e. the next updateTransfer() hasn't been
        // called, yet)? Is that cool, or do we have to do something
        // about that?

        // Persist the new state of the transfer again.
        updateTransfer(transfer);

        return transfer.getId();
    }

    @Override
    public void onFraudChecked(FraudCheckedEvent event) {
        Transfer transfer = findTransfer(event.transferId())
                .orElseThrow(TransferNotFoundException::new);

        transfer.onFraudChecked(event, true);

        updateTransfer(transfer);
    }

    @Override
    public void onSourceAccountDebited(AccountDebitedEvent event) {
        Transfer transfer = findTransfer(event.transferId())
                .orElseThrow(TransferNotFoundException::new);

        transfer.onSourceAccountDebited(event, true);

        updateTransfer(transfer);
    }

    @Override
    public void onTargetAccountCredited(AccountCreditedEvent event) {
        Transfer transfer = findTransfer(event.transferId())
                .orElseThrow(TransferNotFoundException::new);

        transfer.onTargetAccountCredited(event);

        updateTransfer(transfer);
    }
}
