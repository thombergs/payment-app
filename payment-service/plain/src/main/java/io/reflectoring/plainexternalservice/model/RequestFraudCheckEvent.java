package io.reflectoring.plainexternalservice.model;

import java.util.Locale;

public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transcationLocation
) {
}
