package io.reflectoring.transferservice.transfer.internal.incoming.api;

import java.io.IOException;

public interface IncomingEvents {

    void onFraudChecked(FraudCheckedEvent event) throws IOException;

    void onAccountCredited(AccountCreditedEvent event) throws IOException;

    void onAccountDebited(AccountDebitedEvent event) throws IOException;

}
