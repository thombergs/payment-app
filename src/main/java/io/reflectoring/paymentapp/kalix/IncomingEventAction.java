package io.reflectoring.paymentapp.kalix;

import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.paymentapp.transfer.internal.incoming.api.FraudCheckedEvent;
import kalix.javasdk.action.Action;
import kalix.javasdk.client.ComponentClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/incoming")
public class IncomingEventAction extends Action {

    private final ComponentClient componentClient;

    public IncomingEventAction(
            ComponentClient componentClient) {
        this.componentClient = componentClient;
    }

    @PostMapping("/fraud-checked")
    public Effect<String> onFraudChecked(@RequestBody FraudCheckedEvent event) {
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onFraudChecked)
                .params(event);
        return effects().reply("sent event");
    }

    @PostMapping("/account-debited")
    public Effect<String> onAccountDebited(@RequestBody AccountDebitedEvent event) {
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onSourceAccountDebited)
                .params(event);
        return effects().reply("sent event");
    }

    @PostMapping("/account-credited")
    public Effect<String> onAccountCredited(@RequestBody AccountCreditedEvent event) {
        componentClient.forWorkflow("transfer")
                .call(TransferWorkflow::onTargetAccountCredited)
                .params(event);
        return effects().reply("sent event");
    }

}
