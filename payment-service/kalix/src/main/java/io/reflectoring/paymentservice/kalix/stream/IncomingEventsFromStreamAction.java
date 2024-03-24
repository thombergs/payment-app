package io.reflectoring.paymentservice.kalix.stream;

import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestAccountCreditEvent;
import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestAccountDebitEvent;
import io.reflectoring.paymentservice.transfer.internal.incoming.api.RequestFraudCheckEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountCreditedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountDebitedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Publish;
import kalix.javasdk.annotations.Subscribe;
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

    public IncomingEventsFromStreamAction(@Value("${app.event-delay}") Duration eventDelay) {
        this.eventDelay = eventDelay;
    }

    public Effect<FraudCheckedEvent> onRequestFraudCheckEvent(RequestFraudCheckEvent event) {
        logger.info("received event {} via topic", event);
        delay();
        return effects().reply(new FraudCheckedEvent(
                event.transferId(),
                FraudCheckedEvent.FraudCheckResult.SUCCESS
        ));
    }

    public Effect<AccountDebitedEvent> onRequestFraudCheckEvent(RequestAccountDebitEvent event) {
        logger.info("received event {} via topic", event);
        delay();
        return effects().reply(new AccountDebitedEvent(
                event.transferId(),
                event.sourceAccountId(),
                event.amount(),
                AccountDebitedEvent.DebitResult.SUCCESS
        ));
    }

    public Effect<AccountCreditedEvent> onRequestFraudCheckEvent(RequestAccountCreditEvent event) {
        logger.info("received event {} via topic", event);
        delay();
        return effects().reply(new AccountCreditedEvent(
                event.transferId(),
                event.targetAccountId(),
                event.amount(),
                AccountCreditedEvent.CreditResult.SUCCESS
        ));
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
