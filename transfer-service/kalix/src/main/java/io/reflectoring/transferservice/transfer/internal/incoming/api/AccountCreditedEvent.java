package io.reflectoring.transferservice.transfer.internal.incoming.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.Money;
import io.reflectoring.transferservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("AccountCreditedEvent")
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


