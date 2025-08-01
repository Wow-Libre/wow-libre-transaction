
CREATE TABLE transactions.plan
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(50) NOT NULL,
    price           DOUBLE NOT NULL,
    discount        INTEGER     NOT NULL,
    status          BOOLEAN     NOT NULL,
    currency        VARCHAR(4)  NOT NULL,
    frequency_type  VARCHAR(30),
    frequency_value INTEGER,
    free_trial_days INTEGER,
    created_at      TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at      TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    tax             VARCHAR(50) NOT NULL,
    return_tax      VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT plan_name_uq UNIQUE (name)
);

CREATE TABLE transactions.subscription
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    user_id           BIGINT      NOT NULL,
    plan_id           BIGINT      NOT NULL,
    creation_date     DATE        NOT NULL,
    next_invoice_date DATE        NOT NULL,
    reference_number  VARCHAR(80) NOT NULL,
    status            VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT subscription_reference_number_uq UNIQUE (reference_number)
);

CREATE TABLE transactions.partner
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(50) NOT NULL,
    realm_id BIGINT      NOT NULL,
    status    BOOLEAN     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT partner_realm_id UNIQUE (realm_id)
);

CREATE TABLE transactions.product_category
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description TEXT,
    disclaimer  TEXT,
    PRIMARY KEY (id),
    CONSTRAINT product_category_name_uq UNIQUE (name)
);

CREATE TABLE transactions.product
(
    id                      BIGINT AUTO_INCREMENT NOT NULL,
    name                    VARCHAR(50) NOT NULL,
    product_category_id     BIGINT      NOT NULL,
    disclaimer              TEXT,
    price                   DOUBLE NOT NULL,
    discount                INTEGER,
    description             TEXT,
    image_url               TEXT        NOT NULL,
    partner_id              BIGINT      NOT NULL,
    reference_number        VARCHAR(80) NOT NULL,
    status                  BOOLEAN     NOT NULL,
    credit_points_enabled   BOOLEAN     NOT NULL,
    credit_points_amount    BIGINT,
    tax                     VARCHAR(50) NOT NULL,
    return_tax              VARCHAR(50) NOT NULL,
    language                VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT product_name_language_uq UNIQUE (name,language),
    CONSTRAINT product_reference_uq UNIQUE (reference_number),
    CONSTRAINT product_partner_id_fk FOREIGN KEY (partner_id) REFERENCES transactions.partner (id),
    CONSTRAINT product_product_category_id_fk FOREIGN KEY (product_category_id) REFERENCES transactions.product_category (id)
);

CREATE TABLE transactions.product_details
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    product_id  BIGINT      NOT NULL,
    title       VARCHAR(60) NOT NULL,
    description TEXT        NOT NULL,
    img_url     TEXT        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT product_details_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);

CREATE TABLE transactions.packages
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    code_core  VARCHAR(50) NOT NULL,
    product_id BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT packages_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);

CREATE TABLE transactions.transaction
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    user_id           BIGINT      NOT NULL,
    account_id        BIGINT,
    server_id         BIGINT,
    price             DOUBLE NOT NULL,
    status            VARCHAR(50) NOT NULL,
    product_id        BIGINT,
    subscription_id   BIGINT,
    reference_number  VARCHAR(80) NOT NULL,
    creation_date     DATE        NOT NULL,
    payment_method    VARCHAR(60),
    credit_points     BOOLEAN NOT NULL,
    currency          VARCHAR(20) NOT NULL,
    send              BOOLEAN     NOT NULL,
    is_subscription   BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT transaction_product_reference_uq UNIQUE (reference_number),
    CONSTRAINT transaction_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);

CREATE TABLE transactions.client
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    username        VARCHAR(50) NOT NULL,
    jwt             TEXT        NOT NULL,
    refresh_token   TEXT        NOT NULL,
    expiration_date DATE        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT client_username_uq UNIQUE (username)
);

CREATE TABLE transactions.subscription_benefit
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT NOT NULL,
    benefit_id BIGINT NOT NULL,
    created_at DATE   NOT NULL,
    server_id  BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT subscription_benefit_uq UNIQUE (user_id, benefit_id)
);

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


