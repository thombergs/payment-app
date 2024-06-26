package io.reflectoring.paymentservice.transfer.internal.incoming.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

import java.util.Locale;

@TypeName("RequestFraudCheckEvent")
public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transcationLocation
) {
}
