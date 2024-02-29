package io.reflectoring.paymentapp.transfer.internal.incoming.api;

import io.reflectoring.paymentapp.transfer.api.AccountId;
import io.reflectoring.paymentapp.transfer.api.TransferId;

import java.util.Locale;

public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transcationLocation
) {
}
