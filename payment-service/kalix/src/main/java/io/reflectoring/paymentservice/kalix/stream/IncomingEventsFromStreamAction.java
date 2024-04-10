package io.reflectoring.paymentservice.kalix.stream;

import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestAccountCreditEvent;
import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestAccountDebitEvent;
import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestFraudCheckEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountCreditedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountDebitedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Subscribe;
import kalix.javasdk.client.ComponentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * This class is removed by the build process when run with the profile "-P topic".
 */
@Subscribe.Stream(id = "transfer-events", service = "transfer-service")
public class IncomingEventsFromStreamAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(IncomingEventsFromStreamAction.class);
    private final Duration eventDelay;
    private final ComponentClient componentClient;

    public IncomingEventsFromStreamAction(@Value("${app.event-delay}") Duration eventDelay, ComponentClient componentClient) {
        this.eventDelay = eventDelay;
        this.componentClient = componentClient;
    }

    public Effect<String> onRequestFraudCheckEvent(RequestFraudCheckEvent incomingEvent) {
        logger.info("received event {} via topic", incomingEvent);

        // simulating time-consuming processing of the event ...
        delay();

        FraudCheckedEvent outgoingEvent = new FraudCheckedEvent(
                incomingEvent.transferId(),
                FraudCheckedEvent.FraudCheckResult.SUCCESS);

        // put the outgoing event on the journal
        var call = componentClient.forEventSourcedEntity(incomingEvent.transferId().toString())
                .call(PaymentEventEntity::fraudChecked)
                .params(outgoingEvent);
        return effects().forward(call);
    }

    public Effect<String> onRequestAccountDebitEvent(RequestAccountDebitEvent incomingEvent) {
        logger.info("received event {} via topic", incomingEvent);

        // simulating time-consuming processing of the event ...
        delay();

        AccountDebitedEvent outgoingEvent = new AccountDebitedEvent(
                incomingEvent.transferId(),
                incomingEvent.sourceAccountId(),
                incomingEvent.amount(),
                AccountDebitedEvent.DebitResult.SUCCESS
        );

        // put the outgoing event on the journal
        var call = componentClient.forEventSourcedEntity(incomingEvent.transferId().toString())
                .call(PaymentEventEntity::accountDebited)
                .params(outgoingEvent);
        return effects().forward(call);
    }

    public Effect<String> onRequestAccountCreditEvent(RequestAccountCreditEvent incomingEvent) {
        logger.info("received event {} via topic", incomingEvent);

        // simulating time-consuming processing of the event ...
        delay();

        AccountCreditedEvent outgoingEvent = new AccountCreditedEvent(
                incomingEvent.transferId(),
                incomingEvent.targetAccountId(),
                incomingEvent.amount(),
                AccountCreditedEvent.CreditResult.SUCCESS
        );

        // put the outgoing event on the journal
        var call = componentClient.forEventSourcedEntity(incomingEvent.transferId().toString())
                .call(PaymentEventEntity::accountCredited)
                .params(outgoingEvent);
        return effects().forward(call);
    }

    private void delay() {
        try {
            logger.info("delaying for {}", eventDelay);
            TimeUnit.SECONDS.sleep(eventDelay.getSeconds());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
