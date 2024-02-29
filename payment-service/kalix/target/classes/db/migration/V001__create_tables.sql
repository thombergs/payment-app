create table transfer (
    id varchar(36) unique,
    status varchar(40) not null,
    failed_reason varchar(1000),
    source_account_id varchar(36) not null,
    target_account_id varchar(36) not null,
    amount bigint not null,
    currency varchar(3) not null,
    transaction_location varchar(10) not null
);