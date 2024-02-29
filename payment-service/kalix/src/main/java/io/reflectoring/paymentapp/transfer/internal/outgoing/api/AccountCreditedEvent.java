package io.reflectoring.paymentapp.transfer.internal.outgoing.api;

import io.reflectoring.paymentapp.transfer.api.AccountId;
import io.reflectoring.paymentapp.transfer.api.Money;
import io.reflectoring.paymentapp.transfer.api.TransferId;

public record AccountCreditedEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount,
        CreditResult result
) {

    public enum CreditResult {
        SUCCESS,
        FAILED
    }

}


