package io.reflectoring.paymentapp.transfer.api;

public enum WorkflowStatus {
    NOT_STARTED(Transfer.TransferWorkflowAction.FRAUD_CHECK),
    WAITING_FOR_FRAUD_CHECK(Transfer.TransferWorkflowAction.FRAUD_CHECK),
    FRAUD_CHECKED(Transfer.TransferWorkflowAction.DEBIT_SOURCE_ACCOUNT),
    WAITING_FOR_DEBIT(Transfer.TransferWorkflowAction.DEBIT_SOURCE_ACCOUNT),
    SOURCE_ACCOUNT_DEBITED(Transfer.TransferWorkflowAction.CREDIT_TARGET_ACCOUNT),
    WAITING_FOR_CREDIT(Transfer.TransferWorkflowAction.CREDIT_TARGET_ACCOUNT),
    COMPLETE(Transfer.TransferWorkflowAction.NONE),
    FAILED(Transfer.TransferWorkflowAction.NONE);

    private final Transfer.TransferWorkflowAction nextAction;

    WorkflowStatus(Transfer.TransferWorkflowAction nextAction) {
        this.nextAction = nextAction;
    }

    public Transfer.TransferWorkflowAction getNextAction() {
        return nextAction;
    }
}
