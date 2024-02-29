package io.reflectoring.plainexternalservice.model;

public record RequestAccountDebitEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount
) {
}
