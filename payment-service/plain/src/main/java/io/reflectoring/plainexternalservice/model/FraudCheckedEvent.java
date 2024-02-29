package io.reflectoring.plainexternalservice.model;

public record FraudCheckedEvent(
        TransferId transferId,
        FraudCheckResult result
) {

    public enum FraudCheckResult {
        SUCCESS,
        FAILED
    }

}


