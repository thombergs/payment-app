package io.reflectoring.paymentapp.transfer.internal.outgoing.api;

public interface OutgoingEvents {

    void requestFraudCheck(RequestFraudCheckEvent event);

    void requestAccountDebit(RequestAccountDebitEvent event);

    void requestAccountCredit(RequestAccountCreditEvent event);

}
