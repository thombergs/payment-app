package io.reflectoring.transferservice.kalix.stream;

import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Publish;
import kalix.javasdk.annotations.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This action subscribes to the event stream emitted by the TransferEventEntity and
 * publishes it to the stream "transfer-events" so it can subscribed by other Kalix services.
 *
 * This class is removed by the build process when run with the profile "-P topic".
 */
@Subscribe.EventSourcedEntity(value = TransferEventEntity.class, ignoreUnknown = true)
@Publish.Stream(id = "transfer-events")
public class OutgoingEventsToStreamAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(OutgoingEventsToStreamAction.class);

    public Effect<RequestFraudCheckEvent> requestFraudCheck(@RequestBody RequestFraudCheckEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    public Effect<RequestAccountDebitEvent> requestAccountDebit(@RequestBody RequestAccountDebitEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    public Effect<RequestAccountCreditEvent> requestAccountCredit(@RequestBody RequestAccountCreditEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

}
