drop database if exists bank_system;

create database bank_system;

use bank_system;

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
  ID                   tinyint unsigned not null AUTO_INCREMENT,
  NAME                 VARCHAR(50) not null,
  primary key (ID)
);


/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
  ID                   int unsigned not null AUTO_INCREMENT,
  ROLE_ID              tinyint unsigned not null,
  FIRST_NAME           VARCHAR(100) not null,
  LAST_NAME            VARCHAR(150) not null,
  EMAIL                VARCHAR(200),
  PHONE_NUMBER         VARCHAR(50),
  PASSWORD             VARCHAR(255) not null,
  primary key (ID),
  constraint FK_ROLE_ID foreign key (ROLE_ID) references ROLE (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: ACCOUNT_TYPE                                          */
/*==============================================================*/

create table ACCOUNT_TYPE
(
  ID                   tinyint unsigned not null AUTO_INCREMENT,
  NAME                 VARCHAR(50) not null,
  primary key (ID)
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/

create table STATUS
(
  ID                   tinyint unsigned not null AUTO_INCREMENT,
  NAME                 VARCHAR(50) not null,
  primary key (ID)
);

/*==============================================================*/
/* Table: ACCOUNT                                               */
/*==============================================================*/

create table ACCOUNT
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  USER_ID              int unsigned not null,
  TYPE_ID              tinyint unsigned not null,
  STATUS_ID            tinyint unsigned not null,
  primary key (ID),
  constraint ACCOUNT_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID) on delete restrict on update restrict,
  constraint FK_USER_ID foreign key (USER_ID) references USER (ID) on delete restrict on update restrict,
  constraint FK_TYPE_ID foreign key (TYPE_ID) references ACCOUNT_TYPE (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: CREDIT_ACCOUNT_DETAILS                                */
/*==============================================================*/
create table CREDIT_ACCOUNT_DETAILS
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  BALANCE              DECIMAL(13,4) not null,
  CREDIT_LIMIT         DECIMAL(13,4) not null,
  INTEREST_RATE        real not null,
  LAST_OPERATION       timestamp not null,
  ACCRUED_INTEREST     DECIMAL(13,4) not null,
  VALIDITY_DATE        timestamp not null,
  primary key (ID),
  constraint CAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: DEBIT_ACCOUNT_DETAILS                                 */
/*==============================================================*/
create table DEBIT_ACCOUNT_DETAILS
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  BALANCE              DECIMAL(13,4) not null,
  LAST_OPERATION       timestamp not null,
  MIN_BALANCE          DECIMAL(13,4) not null,
  ANNUAL_RATE          real not null,
  primary key (ID),
  constraint DAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: REGULAR_ACCOUNT_DETAILS                               */
/*==============================================================*/
create table REGULAR_ACCOUNT_DETAILS
(
  ID             bigint unsigned not null AUTO_INCREMENT,
  BALANCE        DECIMAL(13, 4)  not null,
  primary key (ID),
  constraint RAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: CARD                                                  */
/*==============================================================*/
create table CARD
(
  ACCOUNT_ID           bigint unsigned not null,
  CARD_NUMBER          bigint(16) unsigned not null AUTO_INCREMENT,
  PIN                  SMALLINT(4) unsigned not null,
  CVV                  SMALLINT(3) unsigned not null,
  EXPIRE_DATE          timestamp not null,
  TYPE                 VARCHAR(20) not null,
  primary key (CARD_NUMBER),
  constraint CARD_FK_ACCOUNT_ID foreign key (ACCOUNT_ID) references ACCOUNT (ID) on delete restrict on update restrict
);
ALTER TABLE CARD AUTO_INCREMENT = 1000000000000000;

/*==============================================================*/
/* Table: CREDIT_REQUEST                                        */
/*==============================================================*/
create table CREDIT_REQUEST
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  USER_ID              int unsigned not null,
  INTEREST_RATE        real not null,
  VALIDITY_DATE        timestamp not null,
  CREDIT_LIMIT         DECIMAL(13,4) not null,
  STATUS_ID            tinyint unsigned not null,
  primary key (ID),
  constraint CR_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID) on delete restrict on update restrict,
  constraint CR_FK_USER_ID foreign key (USER_ID) references USER(ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: PAYMENT                                               */
/*==============================================================*/
create table PAYMENT
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  AMOUNT               DECIMAL(13,4) not null,
  ACCOUNT_FROM         bigint unsigned not null,
  ACCOUNT_TO            bigint unsigned not null,
  OPERATION_DATE       timestamp not null,
  primary key (ID),
  constraint FK_ACCOUNT_ID_FROM foreign key (ACCOUNT_FROM) references ACCOUNT(ID) on delete restrict on update restrict,
  constraint FK_ACCOUNT_ID_TO foreign key (ACCOUNT_TO) references ACCOUNT(ID) on delete restrict on update restrict
);

/*==============================================================*/
/* VIEWS                                                        */
/*==============================================================*/
CREATE VIEW payment_details AS SELECT
                                 payment.id, payment.amount,
                                 payment.account_from, payment.account_to, payment.operation_date,
                                 acc1_user.id AS acc1_user_id, acc1_user.first_name AS acc1_first_name,
                                 acc1_user.last_name AS acc1_last_name, acc1_user.email AS acc1_email,
                                 acc1_user.phone_number AS acc1_phone_number, acc1_user.password AS acc1_password,
                                 acc1_user.role_id AS acc1_role_id,acc1_role.name AS acc1_role_name,
                                 acc1_account.id AS acc1_id,
                                 acc1_account.status_id AS acc1_status_id, acc1_status.name AS acc1_status_name,
                                 acc1_type.id AS acc1_type_id, acc1_type.name AS acc1_type_name,
                                 acc1_cad.id AS acc1_credit_id,
                                 acc1_cad.balance AS acc1_credit_balance,
                                 acc1_cad.credit_limit AS acc1_credit_credit_limit,
                                 acc1_cad.interest_rate AS acc1_credit_interest_rate,
                                 acc1_cad.last_operation AS acc1_credit_last_operation,
                                 acc1_cad.accrued_interest AS acc1_credit_accrued_interest,
                                 acc1_cad.validity_date AS acc1_credit_validity_date,
                                 acc1_dad.id AS acc1_debit_id,
                                 acc1_dad.balance AS acc1_debit_balance,
                                 acc1_dad.annual_rate AS acc1_debit_annual_rate,
                                 acc1_dad.last_operation AS acc1_debit_last_operation,
                                 acc1_dad.min_balance AS acc1_debit_min_balance,
                                 acc1_rad.id AS acc1_regular_id,
                                 acc1_rad.balance AS acc1_regular_balance,

                                 acc2_user.id AS acc2_user_id, acc2_user.first_name AS acc2_first_name,
                                 acc2_user.last_name AS acc2_last_name, acc2_user.email AS acc2_email,
                                 acc2_user.phone_number AS acc2_phone_number, acc2_user.password AS acc2_password,
                                 acc2_user.role_id AS acc2_role_id,acc2_role.name AS acc2_role_name,
                                 acc2_account.id AS acc2_id,
                                 acc2_account.status_id AS acc2_status_id, acc2_status.name AS acc2_status_name,
                                 acc2_type.id AS acc2_type_id, acc2_type.name AS acc2_type_name,
                                 acc2_cad.id AS acc2_credit_id,
                                 acc2_cad.balance AS acc2_credit_balance,
                                 acc2_cad.credit_limit AS acc2_credit_credit_limit,
                                 acc2_cad.interest_rate AS acc2_credit_interest_rate,
                                 acc2_cad.last_operation AS acc2_credit_last_operation,
                                 acc2_cad.accrued_interest AS acc2_credit_accrued_interest,
                                 acc2_cad.validity_date AS acc2_credit_validity_date,
                                 acc2_dad.id AS acc2_debit_id,
                                 acc2_dad.balance AS acc2_debit_balance,
                                 acc2_dad.annual_rate AS acc2_debit_annual_rate,
                                 acc2_dad.last_operation AS acc2_debit_last_operation,
                                 acc2_dad.min_balance AS acc2_debit_min_balance,
                                 acc2_rad.id AS acc2_regular_id,
                                 acc2_rad.balance AS acc2_regular_balance
                               FROM payment
                                 JOIN account AS acc1_account ON account_from = acc1_account.id
                                 JOIN user AS acc1_user ON acc1_account.user_id = acc1_user.id
                                 JOIN role  AS acc1_role ON acc1_user.role_id = acc1_role.id
                                 JOIN account_type AS acc1_type ON acc1_account.type_id = acc1_type.id
                                 LEFT JOIN credit_account_details AS acc1_cad
                                   ON acc1_account.id = acc1_cad.id
                                 LEFT JOIN debit_account_details AS acc1_dad
                                   ON acc1_account.id = acc1_dad.id
                                 LEFT JOIN regular_account_details AS acc1_rad
                                   ON acc1_account.id = acc1_rad.id
                                 LEFT JOIN status AS acc1_status
                                   ON acc1_account.status_id  =acc1_status.id

                                 JOIN account AS acc2_account ON account_to = acc2_account.id
                                 JOIN user AS acc2_user ON acc2_account.user_id = acc2_user.id
                                 JOIN role  AS acc2_role ON acc2_user.role_id = acc2_role.id
                                 JOIN account_type AS acc2_type ON acc2_account.type_id = acc2_type.id
                                 LEFT JOIN credit_account_details AS acc2_cad
                                   ON acc2_account.id = acc2_cad.id
                                 LEFT JOIN debit_account_details AS acc2_dad
                                   ON acc2_account.id = acc2_dad.id
                                 LEFT JOIN regular_account_details AS acc2_rad
                                   ON acc2_account.id = acc2_rad.id
                                 LEFT JOIN status AS acc2_status
                                   ON acc2_account.status_id  =acc2_status.id;

/*==============================================================*/

CREATE VIEW card_details AS SELECT
                              card_number,
                              pin, cvv, expire_date, type,
                                                 user.id AS user_id, user.first_name,
                              user.last_name, user.email,
                              user.phone_number, user.password,
                              user.role_id,
                                                 role.name AS role_name,
                              account.id,
                              account.status_id, status.name AS status_name,
                                                 type.id AS type_id, type.name AS type_name,
                                                 cad.id AS credit_id,
                                                 cad.balance AS credit_balance,
                                                 cad.credit_limit AS credit_credit_limit,
                                                 cad.interest_rate AS credit_interest_rate,
                                                 cad.last_operation AS credit_last_operation,
                                                 cad.accrued_interest AS credit_accrued_interest,
                                                 cad.validity_date AS credit_validity_date,
                                                 dad.id AS debit_id,
                                                 dad.balance AS debit_balance,
                                                 dad.annual_rate AS debit_annual_rate,
                                                 dad.last_operation AS debit_last_operation,
                                                 dad.min_balance AS debit_min_balance,
                                                 rad.id AS regular_id,
                                                 rad.balance AS regular_balance
                            FROM card
                              JOIN account ON account_id = account.id
                              JOIN user ON account.user_id = user.id
                              JOIN role ON user.role_id = role.id
                              JOIN account_type AS type ON account.type_id = type.id
                              LEFT JOIN credit_account_details AS cad
                                ON account.id = cad.id
                              LEFT JOIN debit_account_details AS dad
                                ON account.id = dad.id
                              LEFT JOIN regular_account_details AS rad
                                ON account.id = rad.id
                              LEFT JOIN status
                                ON account.status_id  =status.id;

/*==============================================================*/

CREATE VIEW credit_details AS SELECT
                                cad.id, cad.balance, cad.credit_limit,
                                cad.interest_rate, cad.last_operation,
                                cad.accrued_interest, cad.validity_date,
                                type.id AS type_id, type.name AS type_name,
                                status.id AS status_id,
                                status.name AS status_name,
                                user.id AS user_id, user.first_name,
                                user.last_name, user.email,
                                user.password, user.phone_number,
                                role.id AS role_id, role.name AS role_name
                              FROM account
                                JOIN user ON user_id = user.id
                                JOIN role ON role_id = role.id
                                JOIN account_type AS type ON type_id = type.id
                                LEFT JOIN credit_account_details AS cad ON account.id = cad.id
                                LEFT JOIN status ON account.status_id = status.id
                              WHERE type_id = (select id
                                               from account_type where name like 'CREDIT');

/*==============================================================*/

CREATE VIEW debit_details AS SELECT
                               dad.id, dad.balance,
                               dad.annual_rate, dad.last_operation,
                               dad.min_balance,
                               type.id AS type_id, type.name AS type_name,
                               status.id AS status_id,
                               status.name AS status_name,
                               user.id AS user_id, user.first_name,
                               user.last_name, user.email,
                               user.password, user.phone_number,
                               role.id AS role_id, role.name AS role_name
                             FROM account
                               JOIN user ON user_id = user.id
                               JOIN role ON role_id = role.id
                               JOIN account_type AS type ON type_id = type.id
                               JOIN debit_account_details AS dad ON account.id = dad.id
                               JOIN status ON account.status_id = status.id
                             WHERE type_id = (select id
                                              from account_type where name like 'DEBIT');

/*==============================================================*/

CREATE VIEW regular_details AS SELECT
                                 rad.id, rad.balance,
                                 type.id AS type_id, type.name AS type_name,
                                 status.id AS status_id,
                                 status.name AS status_name,
                                 user.id AS user_id, user.first_name,
                                 user.last_name, user.email,
                                 user.password, user.phone_number,
                                 role.id AS role_id, role.name AS role_name
                               FROM account
                                 JOIN user ON user_id = user.id
                                 JOIN role ON role_id = role.id
                                 JOIN account_type AS type ON type_id = type.id
                                 JOIN regular_account_details AS rad ON account.id = rad.id
                                 JOIN status ON account.status_id = status.id
                               WHERE type_id = (select id
                                                from account_type where name like 'REGULAR');

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
  (1, 'John', 'Ukraine', 'ivan.horpynych@gmail.com', '+380661715108', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'),
  (2, 'Ivan', 'Horpynych-Raduzhenko', 'test@email.com', '+806612345678', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5'),
  (10, 'John', 'Tester', 'test@test.com', '+123456789123', '65e84be33532fb784c48129675f9eff3a682b27168c0ea744b2cf58ee02337c5');

insert into ACCOUNT_TYPE (ID, NAME) values (4, 'CREDIT'), (8, 'DEBIT'), (16, 'REGULAR');

insert into STATUS (ID, NAME) values (1, 'ACTIVE'), (4, 'PENDING'), (8, 'REJECT'), (16, 'BLOCKED');

start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID, STATUS_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'DEBIT'), (select ID from STATUS where NAME = 'ACTIVE'));
insert into DEBIT_ACCOUNT_DETAILS (ID, BALANCE, LAST_OPERATION, MIN_BALANCE, ANNUAL_RATE) values (last_insert_id(),2200,now(),1000, 8.2);

insert into CARD (ACCOUNT_ID, PIN, CVV, EXPIRE_DATE, TYPE) values (last_insert_id(),1234,444,'2019-1-01','VISA');

commit;


start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID, STATUS_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'CREDIT'), (select ID from STATUS where NAME = 'ACTIVE'));
insert into CREDIT_ACCOUNT_DETAILS (ID, BALANCE, CREDIT_LIMIT, INTEREST_RATE, LAST_OPERATION, ACCRUED_INTEREST, VALIDITY_DATE) values (last_insert_id(),-100,2500,12.5,now(),123,'2019-1-01');

insert into CARD (ACCOUNT_ID, CARD_NUMBER, PIN, CVV, EXPIRE_DATE, TYPE) values (last_insert_id(),2222222222222222,1234,555,'2019-1-01', 'VISA');

commit;

start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID, STATUS_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'REGULAR'), (select ID from STATUS where NAME = 'ACTIVE'));
insert into REGULAR_ACCOUNT_DETAILS (ID, BALANCE) values (last_insert_id(),150);

insert into CARD (ACCOUNT_ID, PIN, CVV, EXPIRE_DATE, TYPE) values (last_insert_id(),1234,666,'2019-1-01', 'MASTERCARD');

commit;

insert into CREDIT_REQUEST (USER_ID, INTEREST_RATE, VALIDITY_DATE, CREDIT_LIMIT, STATUS_ID) values
  ((select ID
    from USER
    where (select id
           from role
           where NAME = 'USER') = ROLE_ID),5.2,'2019-1-01',5000, (select ID from STATUS where NAME = 'PENDING'));


insert into PAYMENT (AMOUNT, ACCOUNT_FROM, ACCOUNT_TO, OPERATION_DATE) VALUES (1200,3,1,curdate());