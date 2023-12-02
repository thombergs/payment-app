package io.reflectoring.paymentapp.transfer.api;

import java.util.Locale;

public record SimpleTransferState(
        TransferId id,
        Transfer.WorkflowStatus workflowStatus,
        String failedReason,
        AccountId sourceAccountId,
        AccountId targetAccountId,
        Money amount,
        Locale transactionLocation
) implements TransferState {

    public TransferState withStatus(Transfer.WorkflowStatus status) {
        return new SimpleTransferState(
                this.id,
                status,
                this.failedReason,
                this.sourceAccountId,
                this.targetAccountId,
                this.amount,
                this.transactionLocation
        );
    }

    public static SimpleTransferState fromTransferState(TransferState state) {
        return new SimpleTransferState(
                state.id(),
                state.workflowStatus(),
                state.failedReason(),
                state.sourceAccountId(),
                state.targetAccountId(),
                state.amount(),
                state.transactionLocation()
        );
    }

}
