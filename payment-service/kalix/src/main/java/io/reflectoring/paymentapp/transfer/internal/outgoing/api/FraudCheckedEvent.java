package io.reflectoring.paymentapp.transfer.internal.outgoing.api;

import io.reflectoring.paymentapp.transfer.api.TransferId;

public record FraudCheckedEvent(
        TransferId transferId,
        FraudCheckResult result
) {

    public enum FraudCheckResult {
        SUCCESS,
        FAILED
    }

}


