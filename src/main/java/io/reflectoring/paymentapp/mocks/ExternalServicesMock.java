package io.reflectoring.paymentapp.mocks;

import io.reflectoring.paymentapp.kalix.IncomingEventAction;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.client.ComponentClient;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * This class mocks the external payment service that is responsible for debiting and crediting
 * money to / from certain accounts.
 * <p>
 * It's using Kalix' {@link ComponentClient} to call the {@link IncomingEventAction} which
 * provides the API to receive response events from the payment service and forwards those events
 * to the {@link io.reflectoring.paymentapp.kalix.TransferWorkflow}.
 * <p>
 * We could have used the {@link ComponentClient} to call the {@link io.reflectoring.paymentapp.kalix.TransferWorkflow}
 * directly from here, but I wanted to have an Action with a dedicated API between the workflow
 * and the external service (i.e. I don't want the external service to call the workflow directly.
 * Also, this way is more "symmetric", given that we now have an {@link OutgoingEventAction} and
 * an {@link IncomingEventAction} ... making the flow easier to grasp.
 */
@Component
public class ExternalServicesMock {

    private final ComponentClient componentClient;

    public ExternalServicesMock(ComponentClient componentClient) {
        this.componentClient = componentClient;
    }

    @EventListener(RequestFraudCheckEvent.class)
    public void onRequestFraudCheck(RequestFraudCheckEvent event) throws InterruptedException {
        Thread.sleep(10000);
        componentClient.forAction()
                .call(IncomingEventAction::onFraudChecked)
                .params(new FraudCheckedEvent(
                        event.transferId(),
                        FraudCheckedEvent.FraudCheckResult.SUCCESS
                ));
    }

    @EventListener(RequestAccountDebitEvent.class)
    public void onRequestAccountDebitEvent(RequestAccountDebitEvent event) throws InterruptedException {
        Thread.sleep(10000);
        componentClient.forAction()
                .call(IncomingEventAction::onAccountDebited)
                .params(new AccountDebitedEvent(
                        event.transferId(),
                        event.sourceAccountId(),
                        event.amount(),
                        AccountDebitedEvent.DebitResult.SUCCESS
                ));
    }

    @EventListener(RequestAccountCreditEvent.class)
    public void onRequestAccountCreditEvent(RequestAccountCreditEvent event) throws InterruptedException {
        Thread.sleep(10000);
        componentClient.forAction()
                .call(IncomingEventAction::onAccountCredited)
                .params(new AccountCreditedEvent(
                        event.transferId(),
                        event.targetAccountId(),
                        event.amount(),
                        AccountCreditedEvent.CreditResult.SUCCESS
                ));
    }


}
