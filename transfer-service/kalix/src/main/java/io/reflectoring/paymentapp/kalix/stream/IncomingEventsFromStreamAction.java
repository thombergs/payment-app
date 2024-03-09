package io.reflectoring.paymentapp.kalix.stream;

import io.reflectoring.paymentapp.kalix.Topics;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Subscribe;
import kalix.javasdk.client.ComponentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This class is removed by the build process when run with the profile "-P topic".
 */
@Subscribe.Stream(id = "payment-events", service = "payment-service")
public class IncomingEventsFromStreamAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(IncomingEventsFromStreamAction.class);

    private final ComponentClient componentClient;

    public IncomingEventsFromStreamAction(
            ComponentClient componentClient) {
        this.componentClient = componentClient;
    }

    public Effect<String> onFraudCheckedEvent(FraudCheckedEvent event) throws IOException {
        logger.info("received event via topic {}", Topics.FRAUD_CHECKED);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onFraudChecked)
                .params(event);
        return effects().reply("sent event");
    }

    public Effect<String> onAccountDebitedEvent(AccountDebitedEvent event) {
        logger.info("received event {} via web call", event);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onSourceAccountDebited)
                .params(event);
        return effects().reply("sent event");
    }

    public Effect<String> onAccountCreditedEvent(AccountCreditedEvent event) {
        logger.info("received event {} via web call", event);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onTargetAccountCredited)
                .params(event);
        return effects().reply("sent event");
    }

}
