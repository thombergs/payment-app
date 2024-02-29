# Transfer Service implemented with Kalix

This project implements a money transfer service with Kalix.

To start the service locally, run:

```shell
mvn kalix:runAll
```

## Overview

TODO: visual overview of the components

## Transfer workflow API

- [Start a transfer](http/start-transfer.http)
- [Simulate a successful fraud check](http/on-fraud-check-success.http)
- [Simulate a successful debit from the source account](http/on-debit-success.http)
- [Simulate a successful credit to the target account](http/on-credit-success.http)
- [Get the current status of the transfer](http/get-transfer-status.http)
