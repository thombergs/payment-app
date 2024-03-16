package io.reflectoring.paymentservice.model;

public record RequestAccountCreditEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount
) {
}
