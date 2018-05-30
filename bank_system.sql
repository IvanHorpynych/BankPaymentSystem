drop database if exists bank_system;

create database bank_system;

use bank_system;

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
  ID   tinyint unsigned not null AUTO_INCREMENT,
  NAME VARCHAR(50)      not null,
  primary key (ID)
);


/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
  ID           bigint unsigned  not null AUTO_INCREMENT,
  ROLE_ID      tinyint unsigned not null,
  FIRST_NAME   VARCHAR(100)     not null,
  LAST_NAME    VARCHAR(150)     not null,
  EMAIL        VARCHAR(200),
  PHONE_NUMBER VARCHAR(50),
  PASSWORD     VARCHAR(255)     not null,
  primary key (ID),
  constraint FK_ROLE_ID foreign key (ROLE_ID) references ROLE (ID)
  on delete restrict
  on update restrict
);


/*==============================================================*/
/* Table: ACCOUNT_TYPE                                          */
/*==============================================================*/

create table ACCOUNT_TYPE
(
  ID   tinyint unsigned not null AUTO_INCREMENT,
  NAME VARCHAR(50)      not null,
  primary key (ID)
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/

create table STATUS
(
  ID   tinyint unsigned not null AUTO_INCREMENT,
  NAME VARCHAR(50)      not null,
  primary key (ID)
);
/*==============================================================*/
/* Table: CURR_ANNUAL_RATE                                                */
/*==============================================================*/

create table CURR_ANNUAL_RATE
(
  ID           bigint unsigned not null AUTO_INCREMENT,
  ANNUAL_RATE  real            not null,
  CREATED_TIME TIMESTAMP                default current_timestamp,
  primary key (ID)
);

/*==============================================================*/
/* Table: ACCOUNT                                               */
/*==============================================================*/

create table ACCOUNT
(
  ID        bigint unsigned  not null AUTO_INCREMENT,
  USER_ID   bigint unsigned  not null,
  TYPE_ID   tinyint unsigned not null,
  BALANCE   DECIMAL(13, 4)   not null default 0,
  STATUS_ID tinyint unsigned not null,
  primary key (ID),
  constraint ACCOUNT_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID)
  on delete restrict
  on update cascade,
  constraint FK_USER_ID foreign key (USER_ID) references USER (ID)
  on delete restrict
  on update restrict,
  constraint FK_TYPE_ID foreign key (TYPE_ID) references ACCOUNT_TYPE (ID)
  on delete restrict
  on update restrict
);


/*==============================================================*/
/* Table: CREDIT_ACCOUNT_DETAILS                                */
/*==============================================================*/
create table CREDIT_ACCOUNT_DETAILS
(
  ID               bigint unsigned not null AUTO_INCREMENT,
  CREDIT_LIMIT     DECIMAL(13, 4)  not null,
  INTEREST_RATE    real            not null,
  ACCRUED_INTEREST DECIMAL(13, 4)  not null default 0,
  VALIDITY_DATE    timestamp       not null,
  primary key (ID),
  constraint CAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID)
  on delete restrict
  on update restrict
);

/*==============================================================*/
/* Table: DEPOSIT_ACCOUNT_DETAILS                                 */
/*==============================================================*/
create table DEPOSIT_ACCOUNT_DETAILS
(
  ID             bigint unsigned not null AUTO_INCREMENT,
  LAST_OPERATION TIMESTAMP       not null default current_timestamp,
  MIN_BALANCE    DECIMAL(13, 4)  not null default 0,
  ANNUAL_RATE    real            not null,
  primary key (ID),
  constraint DPAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID)
  on delete restrict
  on update restrict
);


/*==============================================================*/
/* Table: CARD                                                  */
/*==============================================================*/
create table CARD
(
  ACCOUNT_ID  bigint unsigned      not null,
  CARD_NUMBER bigint(16) unsigned  not null AUTO_INCREMENT,
  PIN         SMALLINT(4) unsigned not null,
  CVV         SMALLINT(3) unsigned not null,
  EXPIRE_DATE timestamp            not null,
  TYPE        VARCHAR(20)          not null,
  STATUS_ID   tinyint unsigned     not null,
  primary key (CARD_NUMBER),
  constraint CARD_FK_ACCOUNT_ID foreign key (ACCOUNT_ID) references ACCOUNT (ID)
  on delete restrict
  on update restrict,
  constraint CARD_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID)
  on delete restrict
  on update cascade
);
ALTER TABLE CARD
AUTO_INCREMENT = 1000000000000000;

/*==============================================================*/
/* Table: CREDIT_REQUEST                                        */
/*==============================================================*/
create table CREDIT_REQUEST
(
  ID            bigint unsigned  not null AUTO_INCREMENT,
  USER_ID       bigint unsigned  not null,
  INTEREST_RATE real             not null,
  VALIDITY_DATE timestamp        not null,
  CREDIT_LIMIT  DECIMAL(13, 4)   not null,
  STATUS_ID     tinyint unsigned not null,
  primary key (ID),
  constraint CR_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID)
  on delete restrict
  on update cascade,
  constraint CR_FK_USER_ID foreign key (USER_ID) references USER (ID)
  on delete restrict
  on update restrict
);

/*==============================================================*/
/* Table: PAYMENT                                               */
/*==============================================================*/
create table PAYMENT
(
  ID               bigint unsigned not null AUTO_INCREMENT,
  AMOUNT           DECIMAL(13, 4)  not null,
  ACCOUNT_FROM     bigint unsigned not null,
  CARD_NUMBER_FROM bigint(16) unsigned,
  ACCOUNT_TO       bigint unsigned not null,
  OPERATION_DATE   timestamp       not null,
  primary key (ID),
  constraint FK_ACCOUNT_ID_FROM foreign key (ACCOUNT_FROM) references ACCOUNT (ID)
  on delete restrict
  on update restrict,
  constraint FK_ACCOUNT_ID_TO foreign key (ACCOUNT_TO) references ACCOUNT (ID)
  on delete restrict
  on update restrict,
  constraint FK_CARD_FROM foreign key (CARD_NUMBER_FROM) references CARD (CARD_NUMBER)
  on delete restrict
    on update restrict
);

/*==============================================================*/
/* VIEWS                                                        */
/*==============================================================*/
CREATE view payment_details AS
  SELECT
    payment.id,
    payment.amount,
    payment.card_number_from,
    payment.account_from,
    payment.account_to,
    payment.operation_date,
    acc1_user.id              AS acc1_user_id,
    acc1_user.first_name      AS acc1_first_name,
    acc1_user.last_name       AS acc1_last_name,
    acc1_user.email           AS acc1_email,
    acc1_user.phone_number    AS acc1_phone_number,
    acc1_user.password        AS acc1_password,
    acc1_user.role_id         AS acc1_role_id,
    acc1_role.name            AS acc1_role_name,
    acc1_account.id           AS acc1_debit_id,
    acc1_account.balance      AS acc1_debit_balance,
    acc1_account.status_id    AS acc1_status_id,
    acc1_status.name          AS acc1_status_name,
    acc1_type.id              AS acc1_type_id,
    acc1_type.name            AS acc1_type_name,
    acc1_cad.id               AS acc1_credit_id,
    acc1_account.balance      AS acc1_credit_balance,
    acc1_cad.credit_limit     AS acc1_credit_credit_limit,
    acc1_cad.interest_rate    AS acc1_credit_interest_rate,
    acc1_cad.accrued_interest AS acc1_credit_accrued_interest,
    acc1_cad.validity_date    AS acc1_credit_validity_date,
    acc1_dpad.id              AS acc1_deposit_id,
    acc1_account.balance      AS acc1_deposit_balance,
    acc1_dpad.annual_rate     AS acc1_deposit_annual_rate,
    acc1_dpad.last_operation  AS acc1_deposit_last_operation,
    acc1_dpad.min_balance     AS acc1_deposit_min_balance,

    acc2_user.id              AS acc2_user_id,
    acc2_user.first_name      AS acc2_first_name,
    acc2_user.last_name       AS acc2_last_name,
    acc2_user.email           AS acc2_email,
    acc2_user.phone_number    AS acc2_phone_number,
    acc2_user.password        AS acc2_password,
    acc2_user.role_id         AS acc2_role_id,
    acc2_role.name            AS acc2_role_name,
    acc2_account.id           AS acc2_debit_id,
    acc2_account.balance      AS acc2_debit_balance,
    acc2_account.status_id    AS acc2_status_id,
    acc2_status.name          AS acc2_status_name,
    acc2_type.id              AS acc2_type_id,
    acc2_type.name            AS acc2_type_name,
    acc2_cad.id               AS acc2_credit_id,
    acc2_account.balance      AS acc2_credit_balance,
    acc2_cad.credit_limit     AS acc2_credit_credit_limit,
    acc2_cad.interest_rate    AS acc2_credit_interest_rate,
    acc2_cad.accrued_interest AS acc2_credit_accrued_interest,
    acc2_cad.validity_date    AS acc2_credit_validity_date,
    acc2_dpad.id              AS acc2_deposit_id,
    acc2_account.balance      AS acc2_deposit_balance,
    acc2_dpad.annual_rate     AS acc2_deposit_annual_rate,
    acc2_dpad.last_operation  AS acc2_deposit_last_operation,
    acc2_dpad.min_balance     AS acc2_deposit_min_balance
  FROM payment
    JOIN account AS acc1_account ON account_from = acc1_account.id
JOIN user AS acc1_user ON acc1_account.user_id = acc1_user.id
JOIN role AS acc1_role ON acc1_user.role_id = acc1_role.id
JOIN account_type AS acc1_type ON acc1_account.type_id = acc1_type.id
LEFT JOIN credit_account_details AS acc1_cad
ON acc1_account.id = acc1_cad.id
LEFT JOIN deposit_account_details AS acc1_dpad
ON acc1_account.id = acc1_dpad.id
LEFT JOIN status AS acc1_status
ON acc1_account.status_id = acc1_status.id

JOIN account AS acc2_account ON account_to = acc2_account.id
JOIN user AS acc2_user ON acc2_account.user_id = acc2_user.id
JOIN role AS acc2_role ON acc2_user.role_id = acc2_role.id
JOIN account_type AS acc2_type ON acc2_account.type_id = acc2_type.id
LEFT JOIN credit_account_details AS acc2_cad
ON acc2_account.id = acc2_cad.id
LEFT JOIN deposit_account_details AS acc2_dpad
ON acc2_account.id = acc2_dpad.id
LEFT JOIN status AS acc2_status
ON acc2_account.status_id = acc2_status.id
ORDER BY payment.id DESC;

/*==============================================================*/

CREATE VIEW card_details AS
  SELECT
    card_number,
    pin,
    cvv,
    expire_date,
    type,
    card.status_id   AS card_status_id,
    card_status.name AS card_status_name,
    user.id          AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.phone_number,
    user.password,
    user.role_id,
    role.name        AS role_name,
    account.id,
    account.status_id,
    status.name      AS status_name,
    type.id          AS type_id,
    type.name        AS type_name,
    account.id       AS debit_id,
    account.balance  AS debit_balance
  FROM card
    JOIN account ON account_id = account.id
    JOIN user ON account.user_id = user.id
    JOIN role ON user.role_id = role.id
    JOIN account_type AS type ON account.type_id = type.id
JOIN status
ON account.status_id = status.id
JOIN status AS card_status
ON card.status_id = card_status.id
where type.id = (select id
from account_type
where name like 'DEBIT');

/*==============================================================*/

CREATE VIEW credit_details AS
  SELECT
    cad.id,
    account.balance,
    cad.credit_limit,
    cad.interest_rate,
    cad.accrued_interest,
    cad.validity_date,
    type.id     AS type_id,
    type.name   AS type_name,
    status.id   AS status_id,
    status.name AS status_name,
    user.id     AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.password,
    user.phone_number,
    role.id     AS role_id,
    role.name   AS role_name
  FROM account
    JOIN user ON user_id = user.id
    JOIN role ON role_id = role.id
    JOIN account_type AS type ON type_id = type.id
LEFT JOIN credit_account_details AS cad ON account.id = cad.id
LEFT JOIN status ON account.status_id = status.id
WHERE type_id = (select id
from account_type
where name like 'CREDIT');

/*==============================================================*/

CREATE VIEW deposit_details AS
  SELECT
    dpad.id,
    account.balance,
    dpad.annual_rate,
    dpad.last_operation,
    dpad.min_balance,
    type.id     AS type_id,
    type.name   AS type_name,
    status.id   AS status_id,
    status.name AS status_name,
    user.id     AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.password,
    user.phone_number,
    role.id     AS role_id,
    role.name   AS role_name
  FROM account
    JOIN user ON user_id = user.id
    JOIN role ON role_id = role.id
    JOIN account_type AS type ON type_id = type.id
JOIN deposit_account_details AS dpad ON account.id = dpad.id
JOIN status ON account.status_id = status.id
WHERE type_id = (select id
from account_type
where name like 'DEPOSIT');

/*==============================================================*/

CREATE VIEW debit_details AS
  SELECT
    account.id,
    account.balance,
    type.id     AS type_id,
    type.name   AS type_name,
    status.id   AS status_id,
    status.name AS status_name,
    user.id     AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.password,
    user.phone_number,
    role.id     AS role_id,
    role.name   AS role_name
  FROM account
    JOIN user ON user_id = user.id
    JOIN role ON role_id = role.id
    JOIN account_type AS type ON type_id = type.id
JOIN status ON account.status_id = status.id
WHERE type_id = (select id
from account_type
where name like 'DEBIT');

/*==============================================================*/

CREATE VIEW account_details AS
  SELECT
    account.id,
    account.balance,
    type.id     AS type_id,
    type.name   AS type_name,
    status.id   AS status_id,
    status.name AS status_name,
    user.id     AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.password,
    user.phone_number,
    role.id     AS role_id,
    role.name   AS role_name
  FROM account
    JOIN user ON user_id = user.id
    JOIN role ON role_id = role.id
    JOIN account_type AS type ON type_id = type.id
JOIN status ON account.status_id = status.id;

/*==============================================================*/

CREATE VIEW credit_request_details AS
  SELECT
    credit_request.id,
    credit_limit,
    interest_rate,
    validity_date,
    status.id   AS status_id,
    status.name AS status_name,
    user.id     AS user_id,
    user.first_name,
    user.last_name,
    user.email,
    user.password,
    user.phone_number,
    role.id     AS role_id,
    role.name   AS role_name
  FROM credit_request
    JOIN user ON user_id = user.id
    JOIN role ON role_id = role.id
    JOIN status ON status_id = status.id;

/*==============================================================*/
/* DATA                                                         */
/*==============================================================*/
insert into ROLE (ID, NAME) VALUES (1, 'ADMINISTRATOR'), (2, 'MANAGER'), (10, 'USER');

insert into USER (ROLE_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, PASSWORD) values
  (1, 'Admin', 'account', 'noreply@email.com', '-',
   '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'),
(2, 'Ivan', 'Horpynych-Raduzhenko', 'test@email.com', '+806612345678',
'65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'),
(10, 'John', 'Tester', 'test@test.com', '+123456789123',
'65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5');

insert into ACCOUNT_TYPE (ID, NAME) values (4, 'CREDIT'), (8, 'DEPOSIT'), (16, 'DEBIT'), (32, 'ATM');

insert into STATUS (ID, NAME) values (1, 'ACTIVE'), (4, 'PENDING'), (8, 'REJECT'), (16, 'BLOCKED'), (20, 'CLOSED'), (24, 'CONFIRM');

insert into CURR_ANNUAL_RATE (ANNUAL_RATE) values (8.6);

insert into ACCOUNT (USER_ID, BALANCE, TYPE_ID, STATUS_ID) values ((select ID
                                                                    from USER
                                                                    where (select id
                                                                           from role
                                                                           where NAME = 'ADMINISTRATOR') = ROLE_ID),
                                                                   0,
                                                                   (select ID
                                                                    from ACCOUNT_TYPE
                                                                    where NAME = 'ATM'), (select ID
                                                                                          from STATUS
                                                                                          where NAME = 'ACTIVE'));

insert into ACCOUNT (USER_ID, BALANCE, TYPE_ID, STATUS_ID) values ((select ID
                                                                    from USER
                                                                    where (select id
                                                                           from role
                                                                           where NAME = 'USER') = ROLE_ID), 3000,
                                                                   (select ID
                                                                    from ACCOUNT_TYPE
                                                                    where NAME = 'DEBIT'), (select ID
                                                                                            from STATUS
                                                                                            where NAME = 'ACTIVE'));


