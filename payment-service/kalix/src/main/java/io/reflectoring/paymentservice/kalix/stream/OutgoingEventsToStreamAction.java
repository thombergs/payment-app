package io.reflectoring.paymentservice.kalix.stream;

import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountCreditedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountDebitedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Publish;
import kalix.javasdk.annotations.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This action subscribes to the event stream emitted by the TransferEventEntity and
 * publishes it to the stream "payment-events" so it can subscribed by other Kalix services.
 * <p>
 * This class is removed by the build process when run with the profile "-P topic".
 */
@Subscribe.EventSourcedEntity(value = PaymentEventEntity.class, ignoreUnknown = true)
@Publish.Stream(id = "payment-events")
public class OutgoingEventsToStreamAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(OutgoingEventsToStreamAction.class);

    public Effect<FraudCheckedEvent> fraudChecked(@RequestBody FraudCheckedEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    public Effect<AccountDebitedEvent> accountDebited(@RequestBody AccountDebitedEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    public Effect<AccountCreditedEvent> accountCredited(@RequestBody AccountCreditedEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

}
