package io.reflectoring.paymentservice.model;

import java.util.Objects;

public record Money(
        long amountInCents,
        Currency currency) {

    public Money {
        Objects.requireNonNull(currency);
        if (amountInCents < 0) {
            throw new IllegalArgumentException("amount must be positive!");
        }
    }
}
