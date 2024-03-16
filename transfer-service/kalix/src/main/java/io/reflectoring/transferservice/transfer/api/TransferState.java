package io.reflectoring.transferservice.transfer.api;

import java.util.Locale;

public interface TransferState {
    TransferId id();

    WorkflowStatus workflowStatus();

    String failedReason();

    AccountId sourceAccountId();

    AccountId targetAccountId();

    Money amount();

    Locale transactionLocation();

}


