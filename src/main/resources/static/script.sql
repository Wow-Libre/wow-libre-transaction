CREATE TABLE transactions.plan
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(50) NOT NULL,
    price DOUBLE NOT NULL,
    discount        INTEGER     NOT NULL,
    status          BOOLEAN     NOT NULL,
    reference_id    BIGINT      NOT NULL,
    merchant_id     BIGINT,
    currency        VARCHAR(4)  NOT NULL,
    frequency_type  VARCHAR(30),
    frequency_value INTEGER,
    free_trial_days INTEGER,
    plan_token      TEXT        NOT NULL,
    subscribe_url   TEXT        NOT NULL,
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
INSERT INTO transactions.product_category(name, description, disclaimer)
VALUES ('Compañeros',
        'Compañeros',
        '¡Añade nuevos y emocionantes compañeros a tu arsenal! Perfectos para divertir y deslumbrar en cada batalla.');

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
);


INSERT INTO transactions.product
(name, product_category_id, disclaimer, price, discount, gambling_money, gold_price, description, image_url, partner_id,
 reference_number, status)
VALUES ('Riendas del tigre espectral presto', 1,
        '¡Domina el Éter con las Riendas del Tigre Espectral!', 0, 0, 1, 10000000,
        'Embárcate en una aventura como ninguna otra montando al magnífico Tigre Espectral, una montura que es tan feroz como encantadora. Con su pelaje etéreo y ojos brillantes como estrellas, este tigre no solo es un espectáculo visual, sino que también es el compañero perfecto para los guerreros que buscan distinguirse en el campo de batalla.',
        'https://wowdevils.com/wp-content/uploads/2024/03/Riendas-del-tigre-espectral-presto-49284.png',
        1, 'SYOTGT8I6P1V9WNRNT9Xd2', 1);


INSERT INTO transactions.packages
    (code_core, product_id)
VALUES ('49284', 2);

INSERT INTO transactions.product_details
    (product_id, title, description, img_url)
VALUES (2, 'Domina el Mundo con el Tigre Espectral Presto',
        'Atrévete a recorrer las tierras de Azeroth con la elegancia y la velocidad del Tigre Espectral Presto. Esta montura no solo es un símbolo de estatus entre los aventureros, sino también un compañero leal que te llevará a la victoria en cada batalla. Con su impresionante diseño etéreo y su aura mágica, el Tigre Espectral Presto te garantiza un viaje sin igual, fusionando la belleza de la naturaleza con la destreza de los más hábiles artesanos. ¡No te pierdas la oportunidad de agregar esta joya a tu colección!',
        'https://wow.zamimg.com/uploads/screenshots/normal/1107058-riendas-del-tigre-espectral-presto-swift-spectral-tiger.jpg');


INSERT INTO transactions.product_details
    (product_id, title, description, img_url)
VALUES (2, 'La Joya de Azeroth: Tigre Espectral Presto',
        'Descubre la majestuosidad del Tigre Espectral Presto, una montura que no solo es un medio de transporte, sino un símbolo de poder y elegancia en el mundo de Azeroth. Con su pelaje iridiscente y su velocidad sobrenatural, esta montura te llevará a nuevas alturas, permitiéndote explorar cada rincón del mapa con estilo. ¡Hazte con este magnífico felino y deja una huella imborrable en cada aventura!',
        'https://wow.zamimg.com/uploads/screenshots/normal/1107058-riendas-del-tigre-espectral-presto-swift-spectral-tiger.jpg');

INSERT INTO transactions.product_details
    (product_id, title, description, img_url)
VALUES (2, 'Velocidad y Estilo: Tigre Espectral Presto',
        'Sube a lomos del Tigre Espectral Presto y siente la adrenalina mientras te desplazas a la velocidad del rayo. Esta impresionante montura combina agilidad y belleza, perfecta para los guerreros que buscan destacar en el campo de batalla. Su diseño etéreo y su aura mágica harán que todos los ojos se posen en ti. ¡No esperes más y añade esta maravilla a tu colección de monturas!',
        'https://wow.zamimg.com/uploads/screenshots/normal/1107058-riendas-del-tigre-espectral-presto-swift-spectral-tiger.jpg');



INSERT INTO transactions.product
(name, product_category_id, disclaimer, price, discount, gambling_money, gold_price, description, image_url, partner_id,
 reference_number, status)
VALUES ('Máscara de fumigador vil', 2,
        '¡Utiliza la Máscara de fumigador vil con precaución! Este artefacto oscuro está diseñado para protegerte de los vapores nocivos, pero su poder también puede atraer a fuerzas indeseadas. No olvides que su uso prolongado podría alterar tu percepción de la realidad.',
        0, 0, 1, 10000000,
        'Adéntrate en las sombras con la Máscara de fumigador vil, un accesorio misterioso que no solo te protegerá de los vapores tóxicos, sino que también te otorgará un aura de poder oscuro. Forjada con materiales de la más alta calidad, esta máscara es el complemento perfecto para cualquier aventurero que busque dominar los secretos del mundo. ¡Prepárate para sumergirte en lo desconocido y enfrentar a tus enemigos con estilo y astucia!',
        'https://static.wikia.nocookie.net/wowwiki/images/f/f4/Vile_Fumigator%27s_Mask.jpg/revision/latest?cb=20110210185018',
        1, 'SYOTGT8I6P1V9WNRNT9Xd2xa2', 1);


INSERT INTO transactions.product
(name, product_category_id, disclaimer, price, discount, gambling_money, gold_price, description, image_url, partner_id,
 reference_number, status)
VALUES ('Osezno de Blizzard', 2,
        '¡Cuidado! El Osezno de Blizzard puede atraer la atención no deseada de enemigos y competidores. Úsalo sabiamente y mantén siempre tus habilidades a la mano para protegerte mientras disfrutas de la compañía de este adorable compañero.',
        2, 0, 0, 0,
        'Descubre la magia del invierno con el Osezno de Blizzard, tu fiel compañero que nunca te dejará solo en tus aventuras. Este adorable osezno no solo te hará compañía, sino que también te proporcionará una cálida sensación de protección en las tierras frías. Con su pelaje suave como la nieve y su espíritu juguetón, este osezno es el compañero perfecto para los exploradores del mundo de Azeroth. ¡Adopta al Osezno de Blizzard y deja que la aventura comience!',
        'https://cdn.altertime.es/08195608-e34e-4e08-8ccd-c8609d0fd747.jpg',
        1, 'SYOTGT8fP1V9WNRNT9Xd2xa2x2', 1);


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