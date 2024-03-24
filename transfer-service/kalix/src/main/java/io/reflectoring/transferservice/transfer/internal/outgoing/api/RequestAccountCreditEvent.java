package io.reflectoring.transferservice.transfer.internal.outgoing.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.Money;
import io.reflectoring.transferservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("RequestAccountCreditEvent")
public record RequestAccountCreditEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount
) implements OutgoingEvent {
}
