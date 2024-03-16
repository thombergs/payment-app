package io.reflectoring.paymentservice.transfer.internal.outgoing.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.Money;
import io.reflectoring.paymentservice.transfer.api.TransferId;

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


