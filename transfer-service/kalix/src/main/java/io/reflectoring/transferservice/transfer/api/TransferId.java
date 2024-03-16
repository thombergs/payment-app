package io.reflectoring.transferservice.transfer.api;

import java.util.UUID;

public record TransferId(String uuid) {

    public TransferId() {
        this(UUID.randomUUID().toString());
    }

}
