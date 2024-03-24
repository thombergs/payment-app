package io.reflectoring.transferservice.transfer.internal.outgoing.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

import java.util.Locale;

@TypeName("RequestFraudCheckEvent")
public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transcationLocation
) implements OutgoingEvent {
}
