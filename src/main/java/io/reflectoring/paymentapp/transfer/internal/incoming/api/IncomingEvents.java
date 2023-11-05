package io.reflectoring.paymentapp.transfer.internal.incoming.api;

public interface IncomingEvents {

    void onFraudChecked(FraudCheckedEvent event);

    void onAccountCredited(AccountCreditedEvent event);

    void onAccountDebited(AccountDebitedEvent event);

}
