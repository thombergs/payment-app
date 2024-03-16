package io.reflectoring.transferservice.transfer.internal.incoming.api;

import io.reflectoring.transferservice.transfer.api.TransferId;

public record FraudCheckedEvent(
        TransferId transferId,
        FraudCheckResult result
) {

    public enum FraudCheckResult {
        SUCCESS,
        FAILED
    }

}


