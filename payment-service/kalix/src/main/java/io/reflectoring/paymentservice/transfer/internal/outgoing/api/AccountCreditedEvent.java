package io.reflectoring.paymentservice.transfer.internal.outgoing.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.Money;
import io.reflectoring.paymentservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("AccountCreditedEvent")
public record AccountCreditedEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount,
        CreditResult result
) implements OutgoingEvent {

    public enum CreditResult {
        SUCCESS,
        FAILED
    }

}


