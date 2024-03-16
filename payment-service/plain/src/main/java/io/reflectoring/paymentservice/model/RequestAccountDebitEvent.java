package io.reflectoring.paymentservice.model;

public record RequestAccountDebitEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount
) {
}
