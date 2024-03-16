package io.reflectoring.transferservice.kalix.topic;

import io.reflectoring.transferservice.kalix.Topics;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Publish;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This class is removed by the build process when run with the profile "-P stream".
 */
@RequestMapping("/outgoing")
public class OutgoingEventsToTopicAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(OutgoingEventsToTopicAction.class);

    @Publish.Topic(Topics.REQUEST_FRAUD_CHECK)
    @PostMapping("/request-fraud-check")
    public Effect<RequestFraudCheckEvent> requestFraudCheck(@RequestBody RequestFraudCheckEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    @Publish.Topic(Topics.REQUEST_ACCOUNT_DEBIT)
    @PostMapping("/request-account-debit")
    public Effect<RequestAccountDebitEvent> requestAccountDebit(@RequestBody RequestAccountDebitEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

    @Publish.Topic(Topics.REQUEST_ACCOUNT_CREDIT)
    @PostMapping("/request-account-credit")
    public Effect<RequestAccountCreditEvent> requestAccountCredit(@RequestBody RequestAccountCreditEvent event) {
        logger.info("publishing event: {}", event);
        return effects().reply(event);
    }

}
