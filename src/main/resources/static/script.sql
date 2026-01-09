CREATE SCHEMA IF NOT EXISTS transactions;

CREATE TABLE transactions.plan
(
    id              BIGINT AUTO_INCREMENT NOT NULL,
    name            VARCHAR(50) NOT NULL,
    price           DOUBLE NOT NULL,
    price_title     varchar(30) NOT NULL,
    description     text,
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
    features JSON NULL,
    language        VARCHAR(20),
    PRIMARY KEY (id)
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
    realm_id                BIGINT      NOT NULL,
    realm_name              varchar(80) NOT NULL,
    reference_number        VARCHAR(80) NOT NULL,
    status                  BOOLEAN     NOT NULL,
    credit_points_enabled   BOOLEAN     NOT NULL,
    credit_points_amount    BIGINT,
    tax                     VARCHAR(50) NOT NULL,
    return_tax              VARCHAR(50) NOT NULL,
    language                VARCHAR(20) NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT product_name_and_realm_id_and_language_uq UNIQUE (name,realm_id,language),
    CONSTRAINT product_reference_number_uq UNIQUE (reference_number),
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
    realm_id         BIGINT,
    price             DOUBLE NOT NULL,
    status            VARCHAR(50) NOT NULL,
    product_id        BIGINT,
    subscription_id   BIGINT,
    plan_id         BIGINT,
    reference_number  VARCHAR(80) NOT NULL,
    creation_date     DATETIME(6) NOT NULL,
    payment_method    VARCHAR(60),
    credit_points     BOOLEAN NOT NULL,
    currency          VARCHAR(20) NOT NULL,
    send              BOOLEAN     NOT NULL,
    reference_payment varchar (150),
    is_subscription   BOOLEAN NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT transaction_product_reference_uq UNIQUE (reference_number),
    CONSTRAINT transaction_product_id FOREIGN KEY (product_id) REFERENCES transactions.product (id)
);


CREATE TABLE transactions.subscription_benefit
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT NOT NULL,
    benefit_id BIGINT NOT NULL,
    created_at DATE   NOT NULL,
    realm_id   BIGINT NOT NULL,
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


CREATE TABLE transactions.payment_gateways (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('STRIPE', 'PAYU') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT unique_type UNIQUE (type)
);

CREATE TABLE transactions.payu_credentials (
    gateway_id BIGINT PRIMARY KEY,
    host VARCHAR(255) NOT NULL,
    api_key VARCHAR(255) NOT NULL,
    api_login VARCHAR(255) NOT NULL,
    key_public VARCHAR(255) NOT NULL,
    success_url VARCHAR(255) NOT NULL,
    cancel_url VARCHAR(255),
    webhook_url VARCHAR(255) NOT NULL,
    merchant_id VARCHAR(255) NOT NULL,
    account_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (gateway_id) REFERENCES payment_gateways(id) ON DELETE CASCADE
);

CREATE TABLE transactions.stripe_credentials (
    gateway_id BIGINT NOT NULL PRIMARY KEY,
    api_secret VARCHAR(255) NOT NULL,
    api_public VARCHAR(255) NOT NULL,
    success_url VARCHAR(255) NOT NULL,
    cancel_url VARCHAR(255) NOT NULL,
    webhook_url VARCHAR(255) NOT NULL,
    webhook_secret VARCHAR(255) NOT NULL,
    CONSTRAINT fk_stripe_credentials_gateway
        FOREIGN KEY (gateway_id)
        REFERENCES payment_gateways(id)
        ON DELETE CASCADE
);


CREATE TABLE `benefit_premium` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `img` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` TEXT DEFAULT NULL,
  `command` VARCHAR(255) DEFAULT NULL,
  `send_item` TINYINT(1) NOT NULL DEFAULT 0,
  `reactivable` TINYINT(1) NOT NULL DEFAULT 0,
   amount DOUBLE NOT NULL,
  `btn_text` VARCHAR(255) DEFAULT NULL,
  `type` VARCHAR(100) DEFAULT NULL,
  `realm_id` BIGINT DEFAULT NULL,
  `language` VARCHAR(20) DEFAULT NULL,
  `status` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `benefit_premium_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `benefit_premium_id` BIGINT NOT NULL,
  `code` VARCHAR(255) NOT NULL,
  `quantity` INT NOT NULL,

  PRIMARY KEY (`id`),
  CONSTRAINT `fk_benefit_premium_item_benefit_premium`
    FOREIGN KEY (`benefit_premium_id`)
    REFERENCES `benefit_premium` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =====================
-- Spanish plans
-- =====================
INSERT INTO transactions.plan
(name, price, price_title, description, discount, status, currency, frequency_type, frequency_value, free_trial_days, tax, return_tax, features, language)
VALUES
('Explorador',0,'Gratis','Crea una cuenta y comienza a disfrutar del juego completo sin costo alguno. Perfecto para nuevos jugadores.',0,true,'USD',NULL,NULL,NULL,0,0,'["Una cuenta de juego por usuario","Acceso completo a todo el contenido del juego","Participación en eventos globales","Acceso a la app móvil (Android)","Soporte comunitario a través del foro y Discord"]','es'),
('Campeón',12,'$12/año','Disfruta de una experiencia premium durante todo el año con un único pago anual. Ahorra dinero y accede a beneficios exclusivos.',0,true,'USD','YEARLY',1,NULL,0,0,'["Ahorro exclusivo anual","Acceso a la app móvil (Android)","Gestión de cuentas desde web y móvil","Paquete de 100 Slots para la ruleta mensual","Equipo inicial para todos los personajes","Soporte técnico prioritario","Hasta 20 cuentas de juego","Cambio de facción y renombrado de personajes gratis","Items y monturas gratuitas cada mes","Montura personalizada VIP"]','es'),
('Viajero',2,'$2/mes','Accede a beneficios adicionales con una suscripción mensual asequible y flexible. Perfecto para jugadores activos que buscan ventajas continuas.',0,true,'USD','MONTHLY',1,NULL,0,0,'["Acceso a la app móvil (Android)","Gestión de cuentas desde web y móvil","Paquete de 100 Slots para la ruleta","Equipo inicial para todos los personajes","Soporte técnico prioritario","Hasta 20 cuentas de juego","Cambio de facción y renombrado de personajes gratis","Items y monturas gratuitas al suscribirte"]','es');

-- =====================
-- English plans
-- =====================
INSERT INTO transactions.plan
(name, price, price_title, description, discount, status, currency, frequency_type, frequency_value, free_trial_days, tax, return_tax, features, language)
VALUES
('Explorer',0,'Free','Create an account and start enjoying the full game at no cost. Perfect for new players.',0,true,'USD',NULL,NULL,NULL,0,0,'["One game account per user","Full access to all game content","Participation in global events","Access to the mobile app (Android)","Community support via forum and Discord"]','en'),
('Champion',12,'$12/year','Enjoy a premium experience all year long with a single annual payment. Save money and access exclusive benefits.',0,true,'USD','YEARLY',1,NULL,0,0,'["Exclusive annual savings","Access to the mobile app (Android)","Account management via web and mobile","100 Slots package for the monthly roulette","Starter gear for all characters","Priority technical support","Up to 20 game accounts","Faction change and character renaming for free","Free items and mounts every month","VIP custom mount"]','en'),
('Traveler',2,'$2/month','Access additional benefits with an affordable and flexible monthly subscription. Perfect for active players seeking continuous advantages.',0,true,'USD','MONTHLY',1,NULL,0,0,'["Access to the mobile app (Android)","Account management via web and mobile","100 Slots package for the roulette","Starter gear for all characters","Priority technical support","Up to 20 game accounts","Faction change and character renaming for free","Free items and mounts upon subscribing"]','en');

-- =====================
-- Portuguese plans
-- =====================
INSERT INTO transactions.plan
(name, price, price_title, description, discount, status, currency, frequency_type, frequency_value, free_trial_days, tax, return_tax, features, language)
VALUES
('Explorador',0,'Grátis','Crie uma conta e comece a desfrutar do jogo completo sem nenhum custo. Perfeito para novos jogadores.',0,true,'USD',NULL,NULL,NULL,0,0,'["Uma conta de jogo por usuário","Acesso completo a todo o conteúdo do jogo","Participação em eventos globais","Acesso ao app móvel (Android)","Suporte da comunidade via fórum e Discord"]','pt'),
('Campeão',12,'$12/ano','Desfrute de uma experiência premium durante todo o ano com um único pagamento anual. Economize e acesse benefícios exclusivos.',0,true,'USD','YEARLY',1,NULL,0,0,'["Economia anual exclusiva","Acesso ao app móvel (Android)","Gestão de contas via web e mobile","Pacote de 100 Slots para a roleta mensal","Equipamento inicial para todos os personagens","Suporte técnico prioritário","Até 20 contas de jogo","Mudança de facção e renomeação de personagens grátis","Itens e montarias gratuitas todo mês","Montaria VIP personalizada"]','pt'),
('Viajante',2,'$2/mês','Acesse benefícios adicionais com uma assinatura mensal acessível e flexível. Perfeito para jogadores ativos que buscam vantagens contínuas.',0,true,'USD','MONTHLY',1,NULL,0,0,'["Acesso ao app móvel (Android)","Gestão de contas via web e mobile","Pacote de 100 Slots para a roleta","Equipamento inicial para todos os personagens","Suporte técnico prioritário","Até 20 contas de jogo","Mudança de facção e renomeação de personagens grátis","Itens e montarias gratuitas ao assinar"]','pt');
