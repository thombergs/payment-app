package io.reflectoring.plainexternalservice.model;

public record RequestAccountCreditEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount
) {
}
