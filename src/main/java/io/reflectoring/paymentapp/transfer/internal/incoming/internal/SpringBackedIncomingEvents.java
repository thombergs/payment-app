package io.reflectoring.paymentapp.transfer.internal.incoming.internal;

import io.reflectoring.paymentapp.transfer.api.TransferService;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.IncomingEvents;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SpringBackedIncomingEvents implements IncomingEvents {

    private final TransferService transferService;

    public SpringBackedIncomingEvents(TransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    @Async
    @EventListener(FraudCheckedEvent.class)
    public void onFraudChecked(FraudCheckedEvent event) {
        transferService.onFraudChecked(event);
    }

    @Override
    @Async
    @EventListener(AccountCreditedEvent.class)
    public void onAccountCredited(AccountCreditedEvent event) {
        transferService.onTargetAccountCredited(event);
    }

    @Override
    @Async
    @EventListener(AccountDebitedEvent.class)
    public void onAccountDebited(AccountDebitedEvent event) {
        transferService.onSourceAccountDebited(event);
    }
}
