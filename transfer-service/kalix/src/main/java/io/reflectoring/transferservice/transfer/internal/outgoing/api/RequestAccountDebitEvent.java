package io.reflectoring.transferservice.transfer.internal.outgoing.api;

import io.reflectoring.transferservice.transfer.api.AccountId;
import io.reflectoring.transferservice.transfer.api.Money;
import io.reflectoring.transferservice.transfer.api.TransferId;

public record RequestAccountDebitEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount
) {
}
