package io.reflectoring.paymentservice.transfer.internal.incoming.api;

import io.reflectoring.paymentservice.transfer.api.AccountId;
import io.reflectoring.paymentservice.transfer.api.TransferId;

import java.util.Locale;

public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transcationLocation
) {
}
