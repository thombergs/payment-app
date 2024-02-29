package io.reflectoring.plainexternalservice.model;

public record AccountCreditedEvent(
        TransferId transferId,
        AccountId targetAccountId,
        Money amount,
        CreditResult result
) {

    public enum CreditResult {
        SUCCESS,
        FAILED
    }

}


