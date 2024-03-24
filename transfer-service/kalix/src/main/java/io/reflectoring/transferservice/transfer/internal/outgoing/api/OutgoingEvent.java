package io.reflectoring.transferservice.transfer.internal.outgoing.api;

public sealed interface OutgoingEvent permits RequestAccountCreditEvent, RequestAccountDebitEvent, RequestFraudCheckEvent {
}
