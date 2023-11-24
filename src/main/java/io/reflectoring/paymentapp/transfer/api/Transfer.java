package io.reflectoring.paymentapp.transfer.api;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.OutgoingEvents;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.util.Objects.requireNonNull;

public class Transfer {

    private final OutgoingEvents outgoingEvents;

    public enum TransferStatus {
        NOT_STARTED(TransferWorkflowAction.FRAUD_CHECK),
        WAITING_FOR_FRAUD_CHECK(TransferWorkflowAction.FRAUD_CHECK),
        FRAUD_CHECKED(TransferWorkflowAction.DEBIT_SOURCE_ACCOUNT),
        WAITING_FOR_DEBIT(TransferWorkflowAction.DEBIT_SOURCE_ACCOUNT),
        SOURCE_ACCOUNT_DEBITED(TransferWorkflowAction.CREDIT_TARGET_ACCOUNT),
        WAITING_FOR_CREDIT(TransferWorkflowAction.CREDIT_TARGET_ACCOUNT),
        COMPLETE(TransferWorkflowAction.NONE),
        FAILED(TransferWorkflowAction.NONE);

        private final TransferWorkflowAction nextAction;

        TransferStatus(TransferWorkflowAction nextAction) {
            this.nextAction = nextAction;
        }

        public TransferWorkflowAction getNextAction() {
            return nextAction;
        }
    }

    public enum TransferWorkflowAction {
        FRAUD_CHECK,
        DEBIT_SOURCE_ACCOUNT,
        CREDIT_TARGET_ACCOUNT,
        NONE
    }

    private final TransferId id;
    private TransferStatus status;
    private String failedReason;
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money amount;
    private final Locale transactionLocation;

    public Transfer(
            OutgoingEvents outgoingEvents,
            AccountId sourceAccountId,
            AccountId targetAccountId,
            Money amount,
            Locale transactionLocation) {
        this.outgoingEvents = requireNonNull(outgoingEvents);
        this.sourceAccountId = requireNonNull(sourceAccountId);
        this.targetAccountId = requireNonNull(targetAccountId);
        this.amount = requireNonNull(amount);
        this.transactionLocation = requireNonNull(transactionLocation);
        this.id = new TransferId();
        this.status = TransferStatus.NOT_STARTED;
    }

    public TransferId getId() {
        return id;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public Money getAmount() {
        return amount;
    }

    public AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public Locale getTransactionLocation() {
        return transactionLocation;
    }

    public void complete() {
        this.status = TransferStatus.COMPLETE;
    }

    public void fail(String reason) {
        this.failedReason = reason;
        this.status = TransferStatus.FAILED;
    }

    /**
     * Equivalent to {@link #continueTransfer()};
     */
    public void startTransfer() {
        this.continueTransfer();
    }

    /**
     * Continues the transfer from its current status. It will send out events
     * but the receivers of those events are expected to be idempotent so that
     * each event is actioned only once. Thus, this method is idempotent, too.
     * <p>
     * After this method has been called, the state of the Transfer entity
     * needs to be updated in the data store.
     */
    public void continueTransfer() {
        switch (this.status.nextAction) {

            case FRAUD_CHECK -> {
                outgoingEvents.requestFraudCheck(
                        new RequestFraudCheckEvent(
                                this.id,
                                this.sourceAccountId,
                                this.transactionLocation));
                this.status = TransferStatus.WAITING_FOR_FRAUD_CHECK;
            }

            case DEBIT_SOURCE_ACCOUNT -> {
                outgoingEvents.requestAccountDebit(
                        new RequestAccountDebitEvent(
                                this.id,
                                this.sourceAccountId,
                                this.getAmount()
                        ));
                this.status = TransferStatus.WAITING_FOR_DEBIT;
            }

            case CREDIT_TARGET_ACCOUNT -> {
                outgoingEvents.requestAccountCredit(
                        new RequestAccountCreditEvent(
                                this.id,
                                this.targetAccountId,
                                this.getAmount()
                        ));
                this.status = TransferStatus.WAITING_FOR_CREDIT;
            }

            case NONE -> {
                // do nothing
            }
        }
    }

    public synchronized void onFraudChecked(FraudCheckedEvent event, boolean continueTransfer) {
        if (!event.transferId().equals(this.id)) {
            throw new IllegalStateException(String.format("event was targeted at different transfer (expected transfer ID: %s; actual transfer ID: %s)!", this.id, event.transferId()));
        }

        if (this.getStatus() != TransferStatus.WAITING_FOR_FRAUD_CHECK) {
            // idempotency: in case of duplicate event, we don't want to do anything
            return;
        }

        if (event.result() == FraudCheckedEvent.FraudCheckResult.FAILED) {
            this.fail("Fraud check failed");
            return;
        }

        this.status = TransferStatus.FRAUD_CHECKED;

        if (continueTransfer) {
            continueTransfer();
        }
    }

    public synchronized void onSourceAccountDebited(AccountDebitedEvent event, boolean continueTransfer) {
        if (!event.transferId().equals(this.id)) {
            throw new IllegalStateException(String.format("event was targeted at different transfer (expected transfer ID: %s; actual transfer ID: %s)!", this.id, event.transferId()));
        }

        if (this.getStatus() != TransferStatus.WAITING_FOR_DEBIT) {
            // idempotency: in case of duplicate event, we don't want to do anything
            return;
        }

        if (event.result() == AccountDebitedEvent.DebitResult.FAILED) {
            this.fail("Debiting source account failed");
            return;
        }

        this.status = TransferStatus.SOURCE_ACCOUNT_DEBITED;

        if (continueTransfer) {
            continueTransfer();
        }
    }

    public synchronized void onTargetAccountCredited(AccountCreditedEvent event) {
        if (!event.transferId().equals(this.id)) {
            throw new IllegalStateException(String.format("event was targeted at different transfer (expected transfer ID: %s; actual transfer ID: %s)!", this.id, event.transferId()));
        }

        if (this.getStatus() != TransferStatus.WAITING_FOR_CREDIT) {
            // idempotency: in case of duplicate event, we don't want to do anything
            return;
        }

        if (event.result() == AccountCreditedEvent.CreditResult.FAILED) {
            this.fail("Crediting target account failed");
            return;
        }

        this.status = TransferStatus.COMPLETE;
    }

}
