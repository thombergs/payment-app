package io.reflectoring.paymentservice.transfer.internal.outgoing.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.Money;
import io.reflectoring.paymentservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("AccountDebitedEvent")
public record AccountDebitedEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount,
        DebitResult result
) implements OutgoingEvent {

    public enum DebitResult {
        SUCCESS,
        FAILED
    }

}


