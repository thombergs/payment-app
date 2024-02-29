package io.reflectoring.plainexternalservice.model;

public record AccountDebitedEvent(
        TransferId transferId,
        AccountId sourceAccountId,
        Money amount,
        DebitResult result
) {

    public enum DebitResult {
        SUCCESS,
        FAILED
    }

}


