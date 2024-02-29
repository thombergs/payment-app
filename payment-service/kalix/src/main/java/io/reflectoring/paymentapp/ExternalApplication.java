package io.reflectoring.paymentapp;

import kalix.javasdk.annotations.Acl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
// Allow all other Kalix services deployed in the same project to access the components of this
// Kalix service, but disallow access from the internet. This can be overridden explicitly
// per component or method using annotations.
// Documentation at https://docs.kalix.io/services/using-acls.html
@Acl(allow = @Acl.Matcher(principal = Acl.Principal.INTERNET))
@EnableAsync
public class ExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalApplication.class, args);
    }
}