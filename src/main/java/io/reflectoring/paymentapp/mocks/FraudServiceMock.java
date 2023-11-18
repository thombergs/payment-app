package io.reflectoring.paymentapp.mocks;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class FraudServiceMock {

    private final ApplicationEventPublisher publisher;
    private final Random random = new Random();

    @EventListener(RequestFraudCheckEvent.class)
    public void onRequestFraudCheck(RequestFraudCheckEvent event) throws InterruptedException {
        Thread.sleep(10000);
        publisher.publishEvent(new FraudCheckedEvent(event.transferId(), FraudCheckedEvent.FraudCheckResult.SUCCESS));
    }

    public FraudServiceMock(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @EventListener(RequestAccountDebitEvent.class)
    public void onRequestAccountDebitEvent(RequestAccountDebitEvent event) throws InterruptedException {
        Thread.sleep(10000);
        publisher.publishEvent(new AccountDebitedEvent(event.transferId(), event.sourceAccountId(), event.amount(), AccountDebitedEvent.DebitResult.SUCCESS));
    }

    @EventListener(RequestAccountCreditEvent.class)
    public void onRequestAccountCreditEvent(RequestAccountCreditEvent event) throws InterruptedException {
        Thread.sleep(10000);
        publisher.publishEvent(new AccountCreditedEvent(event.transferId(), event.targetAccountId(), event.amount(), AccountCreditedEvent.CreditResult.SUCCESS));
    }


}
