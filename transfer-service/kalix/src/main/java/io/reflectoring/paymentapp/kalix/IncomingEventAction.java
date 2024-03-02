package io.reflectoring.paymentapp.kalix;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import kalix.javasdk.JsonSupport;
import kalix.javasdk.action.Action;
import kalix.javasdk.annotations.Subscribe;
import kalix.javasdk.client.ComponentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IncomingEventAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(IncomingEventAction.class);

    private final ComponentClient componentClient;

    public IncomingEventAction(
            ComponentClient componentClient) {
        this.componentClient = componentClient;
    }

    @Subscribe.Topic(Topics.FRAUD_CHECKED)
    public Effect<String> onFraudCheckedEvent(byte[] eventBytes) throws IOException {
        logger.info("received event via topic {}", Topics.FRAUD_CHECKED);
        FraudCheckedEvent event = JsonSupport.getObjectMapper()
                .readValue(eventBytes, FraudCheckedEvent.class);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onFraudChecked)
                .params(event);
        return effects().reply("sent event");
    }

    @Subscribe.Topic(Topics.ACCOUNT_DEBITED)
    public Effect<String> onAccountDebitedEvent(AccountDebitedEvent event) {
        logger.info("received event {} via web call", event);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onSourceAccountDebited)
                .params(event);
        return effects().reply("sent event");
    }

    @Subscribe.Topic(Topics.ACCOUNT_CREDITED)
    public Effect<String> onAccountCreditedEvent(AccountCreditedEvent event) {
        logger.info("received event {} via web call", event);
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onTargetAccountCredited)
                .params(event);
        return effects().reply("sent event");
    }

}
