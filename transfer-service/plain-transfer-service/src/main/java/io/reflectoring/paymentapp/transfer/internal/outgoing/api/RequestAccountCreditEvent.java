package io.reflectoring.paymentapp.transfer.internal.outgoing.api;

import io.reflectoring.paymentapp.transfer.api.AccountId;
import io.reflectoring.paymentapp.transfer.api.Money;
import io.reflectoring.paymentapp.transfer.api.TransferId;

public record RequestAccountCreditEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount
) {
}
