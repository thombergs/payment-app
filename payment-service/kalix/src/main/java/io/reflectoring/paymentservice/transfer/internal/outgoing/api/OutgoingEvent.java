package io.reflectoring.paymentservice.transfer.internal.outgoing.api;

public sealed interface OutgoingEvent permits AccountCreditedEvent, AccountDebitedEvent, FraudCheckedEvent {
}
