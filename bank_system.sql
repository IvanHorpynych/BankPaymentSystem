drop database if exists bank_system;

create database bank_system;

use bank_system;

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
  ID   int unsigned not null AUTO_INCREMENT,
  NAME VARCHAR(50)      not null,
  primary key (ID)
);


/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
  ID           bigint unsigned  not null AUTO_INCREMENT,
  ROLE_ID      int unsigned not null,
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
  ID   int unsigned not null AUTO_INCREMENT,
  NAME VARCHAR(50)      not null,
  primary key (ID)
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/

create table STATUS
(
  ID   int unsigned not null AUTO_INCREMENT,
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
  TYPE_ID   int unsigned not null,
  BALANCE   DECIMAL(13, 4)   not null default 0,
  STATUS_ID int unsigned not null,
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
  STATUS_ID   int unsigned     not null,
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
  STATUS_ID     int unsigned not null,
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
    on update restrict
);


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


