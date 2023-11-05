package io.reflectoring.paymentapp.transfer.api;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

public record AccountId(String uuid) {

    public AccountId {
        requireNonNull(uuid);
    }

    public AccountId() {
        this(UUID.randomUUID().toString());
    }

}
