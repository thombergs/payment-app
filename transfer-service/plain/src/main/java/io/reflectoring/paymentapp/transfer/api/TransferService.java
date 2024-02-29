package io.reflectoring.paymentapp.transfer.api;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;

import java.util.Optional;

public interface TransferService {

    /**
     * Retrieves a given transfer.
     */
    Optional<Transfer> findTransfer(TransferId transferId);

    /**
     * Updates the state of a transfer in the data store.
     */
    void updateTransfer(Transfer transfer);

    /**
     * Continues a transfer workflow. If the transfer has been started before, the processing
     * will pick up at the state it was left in. The workflow is idempotent, so this method can
     * be called multiple times.
     */
    void continueTransfer(Transfer transfer);

    /**
     * Creates a new Transfer entity and starts the transfer.
     */
    TransferId startNewTransfer(TransferId transferId, StartTransferRequest startTransferRequest);

    void onFraudChecked(FraudCheckedEvent event);

    void onSourceAccountDebited(AccountDebitedEvent event);

    void onTargetAccountCredited(AccountCreditedEvent event);
}
