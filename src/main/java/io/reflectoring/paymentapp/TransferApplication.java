package io.reflectoring.paymentapp;

import kalix.javasdk.annotations.Acl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Allow all other Kalix services deployed in the same project to access the components of this
// Kalix service, but disallow access from the internet. This can be overridden explicitly
// per component or method using annotations.
// Documentation at https://docs.kalix.io/services/using-acls.html
@Acl(allow = @Acl.Matcher(service = "*"))
public class TransferApplication {

    private static final Logger logger = LoggerFactory.getLogger(TransferApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TransferApplication.class, args);
    }
}