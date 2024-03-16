package io.reflectoring.transferservice.transfer.api;

public enum WorkflowStatus {
    NOT_STARTED,
    WAITING_FOR_FRAUD_CHECK,
    FRAUD_CHECKED,
    WAITING_FOR_DEBIT,
    SOURCE_ACCOUNT_DEBITED,
    WAITING_FOR_CREDIT,
    COMPLETE,
    FAILED
}
