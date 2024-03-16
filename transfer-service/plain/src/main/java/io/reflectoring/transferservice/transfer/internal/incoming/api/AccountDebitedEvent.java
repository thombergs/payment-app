package io.reflectoring.transferservice.transfer.internal.incoming.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.Money;
import io.reflectoring.transferservice.transfer.api.TransferId;

public record AccountDebitedEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount,
        DebitResult result
) {

    public enum DebitResult {
        SUCCESS,
        FAILED
    }

}


