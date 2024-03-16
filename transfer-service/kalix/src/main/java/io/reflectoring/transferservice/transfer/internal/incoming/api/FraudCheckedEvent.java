package io.reflectoring.transferservice.transfer.internal.incoming.api;

import io.reflectoring.transferservice.transfer.api.TransferId;
import kalix.javasdk.annotations.TypeName;

@TypeName("FraudCheckedEvent")
public record FraudCheckedEvent(
        TransferId transferId,
        FraudCheckResult result
) {

    public enum FraudCheckResult {
        SUCCESS,
        FAILED
    }

}


