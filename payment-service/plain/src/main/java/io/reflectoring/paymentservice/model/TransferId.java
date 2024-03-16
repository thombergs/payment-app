package io.reflectoring.paymentservice.model;

import java.util.UUID;

public record TransferId(String uuid) {

    public TransferId() {
        this(UUID.randomUUID().toString());
    }

}
