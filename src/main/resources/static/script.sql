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
    payment_id       varchar(60),
    gold             boolean,
    currency         varchar(20) NOT NULL,
    send             boolean     NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT transaction_product_reference_uq UNIQUE (reference_number),
    CONSTRAINT transaction_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);


INSERT INTO transactions.partner(name, status, server_id)
VALUES ('Wow Libre', 1, 1);

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
VALUES ('Riendas de Invencible', 1,
        '¡Vuela por Azeroth con estilo!', 20, 0, 0, 0,
        'Forjadas por los más hábiles artesanos, las Riendas de Invencible están hechas de materiales raros y mágicos, lo que les confiere un brillo único y un diseño elegante que refleja la grandeza de quien las utiliza. Además de sus atributos físicos, estas riendas son un símbolo de fuerza y dominio, permitiendo a su portador ejecutar maniobras impresionantes y controlar a su montura con una precisión excepcional.',
        'https://wowdevils.com/wp-content/uploads/2024/04/Riendas-de-Invencible-50818.png',
        1, 'SYOTGT8I6P1V9WNRNT9X', 1);


INSERT INTO transactions.packages
    (id, code_core, product_id)
VALUES (1, '50818', 1);

INSERT INTO transactions.product_details
    (id, product_id, title, description, img_url)
VALUES (1, 2, 'Forjadas por los más hábiles artesanos',
        'Con cada uso, las Riendas de Invencible brindan un sentido de invulnerabilidad, como si el usuario estuviera destinado a superar cualquier desafío. Aquellos que las poseen son reconocidos no solo como grandes guerreros, sino como leyendas en sus respectivos mundos.',
        'https://totemz.files.wordpress.com/2010/02/invinc-2.jpg?w=600&h=450');
INSERT INTO transactions.product_details
    (id, product_id, title, description, img_url)
VALUES (2, 2, 'La Herencia de los Héroes',
        ' Las Riendas de Invencible han sido transmitidas a través de generaciones de héroes, y su legado está tejido en las historias de batallas épicas. Con cada uso, desatan una corriente de energía mágica que mejora la agilidad y resistencia de tu montura. Imagina galopar a través de los campos de batalla, con la certeza de que no hay enemigo que pueda detener tu avance. Eres más que un guerrero; eres una leyenda en ascenso.',
        'https://wow.zamimg.com/uploads/screenshots/normal/171750.jpg');
INSERT INTO transactions.product_details
    (id, product_id, title, description, img_url)
VALUES (3, 2, 'El Lazo de la Invulnerabilidad',
        'Portar las Riendas de Invencible es aceptar un destino glorioso. Estas riendas, creadas por la unión de elementos de la naturaleza y magia arcana, te otorgan un aura de invulnerabilidad. Cada vez que tiras de ellas, te sentirás invencible, listo para desafiar a los más feroces adversarios. Con cada paso que das, el mundo se inclina ante tu valentía, convirtiéndote en un faro de esperanza para tus aliados y un terror para tus enemigos.',
        'https://evelongames.com/wp-content/uploads/2022/12/riendas-del-invencible.jpg');



CREATE TABLE transactions.client
(
    id              bigint AUTO_INCREMENT NOT NULL,
    username        varchar(50) NOT NULL,
    jwt             text        NOT NULL,
    refresh_token   text        NOT NULL,
    expiration_date date        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT client_username_uq UNIQUE (username)
)


ALTER TABLE transactions.transaction
    ADD COLUMN send boolean NOT NULL;