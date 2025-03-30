CREATE TABLE transactions.plan
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    discount        INTEGER     NOT NULL,
    status          BOOLEAN     NOT NULL,
    reference_id    BIGINT      NOT NULL,
    currency        VARCHAR(4)  NOT NULL,
    frequency_type  VARCHAR(30),
    frequency_value INTEGER,
    free_trial_days INTEGER,
    created_at      TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP (6),
    PRIMARY KEY (id),
    CONSTRAINT plan_name_uq UNIQUE (name)
);



CREATE TABLE transactions.subscription
(
    id                bigint AUTO_INCREMENT NOT NULL,
    user_id           bigint      NOT NULL,
    plan_id           bigint      NOT NULL,
    creation_date     date        NOT NULL,
    next_invoice_date date        NOT NULL,
    reference_number  varchar(80) NOT NULL,
    status            varchar(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT subscription_reference_number_uq UNIQUE (reference_number)
);

CREATE TABLE transactions.partner
(
    id        bigint AUTO_INCREMENT NOT NULL,
    name      varchar(50) NOT NULL,
    server_id bigint      NOT NULL,
    status    boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT partner_name_uq UNIQUE (name),
    constraint partner_server_id UNIQUE (server_id)
);

CREATE TABLE transactions.product_category
(
    id          bigint AUTO_INCREMENT NOT NULL,
    name        varchar(50) NOT NULL,
    description text,
    disclaimer  text,
    PRIMARY KEY (id),
    CONSTRAINT product_category_name_uq UNIQUE (name)
);

CREATE TABLE transactions.product
(
    id                  bigint AUTO_INCREMENT NOT NULL,
    name                varchar(50) NOT NULL,
    product_category_id bigint      NOT NULL,
    disclaimer          text,
    price double NOT NULL,
    discount            integer,
    gambling_money      boolean     NOT NULL,
    gold_price double NOT NULL,
    description         text,
    image_url           text        NOT null,
    partner_id          bigint      NOT NULL,
    reference_number    varchar(80) NOT NULL,

    status              boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT product_name_category_uq UNIQUE (name),
    CONSTRAINT product_reference_uq UNIQUE (reference_number),
    CONSTRAINT product_partner_id_fk FOREIGN KEY (partner_id) REFERENCES transactions.partner (id),
    CONSTRAINT product_product_category_id_fk FOREIGN KEY (product_category_id) REFERENCES transactions.product_category (id)

);


CREATE TABLE transactions.product_details
(
    id          bigint AUTO_INCREMENT NOT NULL,
    product_id  bigint      NOT NULL,
    title       varchar(60) NOT NULL,
    description text        NOT NULL,
    img_url     text        NOT null,

    PRIMARY KEY (id),
    CONSTRAINT product_details_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
)


CREATE TABLE transactions.packages
(
    id         bigint AUTO_INCREMENT NOT NULL,
    code_core  varchar(50) NOT NULL,
    product_id bigint      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT packages_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
)



CREATE TABLE transactions.transaction
(
    id               bigint AUTO_INCREMENT NOT NULL,
    user_id          bigint      NOT NULL,
    account_id       bigint,
    server_id        bigint,
    price double NOT NULL,
    status           varchar(50) NOT NULL,
    product_id       bigint,
    subscription_id  bigint,
    reference_number varchar(80) NOT NULL,
    creation_date    date        NOT NULL,
    payment_method   varchar(60),
    credit_points    boolean,
    currency         varchar(20) NOT NULL,
    send             boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT transaction_product_reference_uq UNIQUE (reference_number),
    CONSTRAINT transaction_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);



CREATE TABLE transactions.client
(
    id              bigint AUTO_INCREMENT NOT NULL,
    username        varchar(50) NOT NULL,
    jwt             text        NOT NULL,
    refresh_token   text        NOT NULL,
    expiration_date date        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT client_username_uq UNIQUE (username)
);



CREATE TABLE transactions.subscription_benefit
(
    id         bigint AUTO_INCREMENT NOT NULL,
    user_id    bigint NOT NULL,
    benefit_id bigint NOT NULL,
    created_at date   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT subscription_benefit_uq UNIQUE (user_id, benefit_id)
);


ALTER TABLE subscription_benefit
    ADD server_id bigint;



ALTER TABLE transactions.transaction
DROP
COLUMN gold,
    DROP
COLUMN payment_id,
    ADD COLUMN credit_points BOOLEAN NOT NULL,
    ADD COLUMN payment_method VARCHAR(60);

ALTER TABLE transactions.product
DROP
COLUMN gambling_money,
    DROP
COLUMN gold_price,
    ADD COLUMN credit_points_enabled BOOLEAN NOT NULL,
    ADD COLUMN credit_points_amount BIGINT;

ALTER TABLE transactions.plan
DROP
COLUMN merchant_id,
    DROP
COLUMN plan_token,
    DROP
COLUMN subscribe_url;


ALTER TABLE transactions.plan
    ADD COLUMN tax varchar(50) NOT NULL,
    ADD COLUMN return_tax varchar(50) NOT NULL;

ALTER TABLE transactions.product
    ADD COLUMN tax varchar(50) NOT NULL,
    ADD COLUMN return_tax varchar(50) NOT NULL;

ALTER TABLE transactions.plan
DROP
COLUMN reference_id;

ALTER TABLE transactions.transaction
    ADD COLUMN is_subscription BOOLEAN NOT NULL;


ALTER TABLE subscription_benefit
    ADD server_id bigint;


ALTER TABLE transactions.product
    ADD language varchar(20) NOT NULL;



CREATE TABLE transactions.wallet
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    email      VARCHAR(255),
    points     BIGINT NOT NULL,
    status     BOOLEAN,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);