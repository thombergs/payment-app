package io.reflectoring.paymentservice.transfer.internal.outgoing.api;

import io.reflectoring.paymentservice.transfer.api.TransferId;

public record FraudCheckedEvent(
        TransferId transferId,
        FraudCheckResult result
) implements OutgoingEvent {

    public enum FraudCheckResult {
        SUCCESS,
        FAILED
    }

}


