package io.reflectoring.transferservice.transfer.api;

public record StartTransferRequest(
        String sourceAccountId,
        String targetAccountId,
        long amount,
        String currency,
        String transactionLocation
) {
}