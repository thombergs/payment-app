package io.reflectoring.paymentservice.transfer.internal.incoming.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.Money;
import io.reflectoring.paymentservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("RequestAccountDebitEvent")
public record RequestAccountDebitEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount
) {
}
