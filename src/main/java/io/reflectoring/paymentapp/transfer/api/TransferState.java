package io.reflectoring.paymentapp.transfer.api;

import java.util.Locale;

public interface TransferState {
    TransferId id();

    Transfer.WorkflowStatus workflowStatus();

    String failedReason();

    AccountId sourceAccountId();

    AccountId targetAccountId();

    Money amount();

    Locale transactionLocation();

}


