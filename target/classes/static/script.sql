CREATE TABLE transactions.plan
(
    id       bigint AUTO_INCREMENT NOT NULL,
    name     varchar(50) NOT NULL,
    price double NOT NULL,
    discount integer     NOT NULL,
    status   boolean     NOT NULL,

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
    status            boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT subscription_reference_number_uq UNIQUE (reference_number)
);

CREATE TABLE transactions.partner
(
    id     bigint AUTO_INCREMENT NOT NULL,
    name   varchar(50) NOT NULL,
    status boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT partner_name_uq UNIQUE (name)
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
    price double NOT NULL,
    status           boolean     NOT NULL,
    product_id       bigint,
    subscription_id  bigint,
    reference_number varchar(80) NOT NULL,
    create_date      date        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT transaction_product_reference_uq UNIQUE (reference_number),
    CONSTRAINT transaction_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);


INSERT INTO transactions.partner(name, status)
VALUES ('Wow Libre', 1);

INSERT INTO transactions.product_category(name, description, disclaimer)
VALUES ('Monturas', 'Monturas',
        'Prepárate, tu montura viajará tan rápido como tu habilidad de equitación se lo permita.');
INSERT INTO transactions.product_category(name, description, disclaimer)
VALUES ('Juguetes', 'Juguetes',
        '¡Añade nuevos y divertidos juguetes a tu colección del juego para entretener a tus aliados o provocar a tus enemigos!');
INSERT INTO transactions.product_category (name, description, disclaimer)
VALUES ('Packs', 'Packs', 'La diversión siempre es mejor a lo grande, como en estos packs de objetos en el juego.');

INSERT INTO transactions.product
(name, product_category_id, disclaimer, price, discount, gambling_money, gold_price, description, image_url, partner_id,
 reference_number, status)
VALUES ('Maestro de manada de las Colinas Pardas', 1,
        '¡Vuela por Azeroth con estilo con esta montura que ofrece reparaciones y transfiguraciones!', 20, 0, 0, 0,
        'Description',
        'https://blz-contentstack-images.akamaized.net/v3/assets/bltf408a0557f4e4998/blt6eecb28ec5aba217/66eb20ff55753d40e87393a8/WoW_GrizzlyHillsPackMaster_ShopBrowsingCard_Indicators_1920x1080.png?imwidth=1088&imdensity=1',
        1, '000001', 1);