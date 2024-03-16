package io.reflectoring.paymentservice.model;

import java.util.Locale;

public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transactionLocation
) {
}
