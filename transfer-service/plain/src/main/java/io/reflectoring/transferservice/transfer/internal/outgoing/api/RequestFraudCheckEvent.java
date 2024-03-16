package io.reflectoring.transferservice.transfer.internal.outgoing.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.TransferId;

import java.util.Locale;

public record RequestFraudCheckEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Locale transactionLocation
) {
}
