package io.reflectoring.paymentapp;


import dev.stratospheric.cdk.ApplicationEnvironment;
import dev.stratospheric.cdk.Network;
import dev.stratospheric.cdk.Service;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class ServiceApp {

    public static void main(final String[] args) {
        App app = new App();

        String environmentName = (String) app.getNode().tryGetContext("environmentName");
        Validations.requireNonEmpty(environmentName, "context variable 'environmentName' must not be null");

        String applicationName = (String) app.getNode().tryGetContext("applicationName");
        Validations.requireNonEmpty(applicationName, "context variable 'applicationName' must not be null");

        String accountId = (String) app.getNode().tryGetContext("accountId");
        Validations.requireNonEmpty(accountId, "context variable 'accountId' must not be null");

        String springProfile = (String) app.getNode().tryGetContext("springProfile");
        Validations.requireNonEmpty(springProfile, "context variable 'springProfile' must not be null");

        String dockerRepositoryName = (String) app.getNode().tryGetContext("dockerRepositoryName");
        Validations.requireNonEmpty(dockerRepositoryName, "context variable 'dockerRepositoryName' must not be null");

        String dockerImageTag = (String) app.getNode().tryGetContext("dockerImageTag");
        Validations.requireNonEmpty(dockerImageTag, "context variable 'dockerImageTag' must not be null");

        String region = (String) app.getNode().tryGetContext("region");
        Validations.requireNonEmpty(region, "context variable 'region' must not be null");

        String httpListenerPriorityString = (String) app.getNode().tryGetContext("httpListenerPriority");
        Validations.requireNonEmpty(httpListenerPriorityString, "context variable 'httpListenerPriority' must not be null");
        int httpListenerPriority = Integer.valueOf(httpListenerPriorityString);

        Environment awsEnvironment = makeEnv(accountId, region);

        ApplicationEnvironment applicationEnvironment = new ApplicationEnvironment(
                applicationName,
                environmentName
        );

        // This stack is just a container for the parameters below, because they need a Stack as a scope.
        // We're making this parameters stack unique with each deployment by adding a timestamp, because updating an existing
        // parameters stack will fail because the parameters may be used by an old service stack.
        // This means that each update will generate a new parameters stack that needs to be cleaned up manually!
        long timestamp = System.currentTimeMillis();
        Stack parametersStack = new Stack(app, "ServiceParameters-" + timestamp, StackProps.builder()
                .stackName(applicationEnvironment.prefix("Service-Parameters-" + timestamp))
                .env(awsEnvironment)
                .build());

        Stack serviceStack = new Stack(app, "ServiceStack", StackProps.builder()
                .stackName(applicationEnvironment.prefix("Service"))
                .env(awsEnvironment)
                .build());

        new Service(
                serviceStack,
                "Service",
                awsEnvironment,
                applicationEnvironment,
                new Service.ServiceInputParameters(
                        new Service.DockerImageSource(dockerRepositoryName, dockerImageTag),
                        emptyList(),
                        environmentVariables(
                                serviceStack,
                                springProfile,
                                environmentName))
                        .withTaskRolePolicyStatements(List.of(
                                PolicyStatement.Builder.create()
                                        .sid("AllowMSKAccess")
                                        .effect(Effect.ALLOW)
                                        .resources(List.of(
                                                "arn:aws:kafka:ap-southeast-2:590183826197:cluster/msk-cluster/acdfa467-0419-4f71-8681-ab69d0012438-s2")
                                        )
                                        .actions(Arrays.asList(
                                                "kafka-cluster:Connect",
                                                "kafka-cluster:AlterCluster",
                                                "kafka-cluster:DescribeCluster"))
                                        .build(),
                                PolicyStatement.Builder.create()
                                        .sid("AllowTopicAccess")
                                        .effect(Effect.ALLOW)
                                        .resources(List.of(
                                                "arn:aws:kafka:ap-southeast-2:590183826197:cluster/msk-cluster/acdfa467-0419-4f71-8681-ab69d0012438-s2/*")
                                        )
                                        .actions(Arrays.asList(
                                                "kafka-cluster:*Topic*",
                                                "kafka-cluster:WriteData",
                                                "kafka-cluster:ReadData"))
                                        .build()))
                        .withStickySessionsEnabled(true)
                        .withHealthCheckPath("/actuator/health")
                        .withAwsLogsDateTimeFormat("%Y-%m-%dT%H:%M:%S.%f%z")
                        .withHttpListenerPriority(httpListenerPriority)
                        .withHealthCheckIntervalSeconds(30), // needs to be long enough to allow for slow start up with low-end computing instances

                Network.getOutputParametersFromParameterStore(serviceStack, applicationEnvironment.getEnvironmentName()));

        app.synth();
    }

    static Map<String, String> environmentVariables(
            Construct scope,
            String springProfile,
            String environmentName
    ) {
        Map<String, String> vars = new HashMap<>();
        vars.put("SPRING_PROFILES_ACTIVE", springProfile);
        vars.put("ENVIRONMENT_NAME", environmentName);
        return vars;
    }

    static Environment makeEnv(String account, String region) {
        return Environment.builder()
                .account(account)
                .region(region)
                .build();
    }
}
