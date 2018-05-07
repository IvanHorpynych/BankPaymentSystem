drop database if exists bank_system;

create database bank_system;

use bank_system;

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
  ID                   tinyint unsigned not null,
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
  ID                   tinyint unsigned not null,
  NAME                 VARCHAR(50) not null,
  primary key (ID)
);

/*==============================================================*/
/* Table: STATUS                                                */
/*==============================================================*/

create table STATUS
(
  ID                   tinyint unsigned not null,
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
  primary key (ID),
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
  INTEREST_RATE          smallint not null,
  LAST_OPERATION       date not null,
  ACCRUED_INTEREST     DECIMAL(13,4) not null,
  VALIDITY_DATE        date not null,
  STATUS_ID            tinyint unsigned not null,
  primary key (ID),
  constraint CAD_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID) on delete restrict on update restrict,
  constraint CAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);



/*==============================================================*/
/* Table: DEBIT_ACCOUNT_DETAILS                                 */
/*==============================================================*/
create table DEBIT_ACCOUNT_DETAILS
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  BALANCE              DECIMAL(13,4) not null,
  LAST_OPERATION       date not null,
  MIN_BALANCE          DECIMAL(13,4) not null,
  ANNUAL_RATE          smallint not null,
  STATUS_ID            tinyint unsigned not null,
  primary key (ID),
  constraint DAD_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID) on delete restrict on update restrict,
  constraint DAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: REGULAR_ACCOUNT_DETAILS                               */
/*==============================================================*/
create table REGULAR_ACCOUNT_DETAILS
(
  ID             bigint unsigned not null AUTO_INCREMENT,
  BALANCE        DECIMAL(13, 4)  not null,
  STATUS_ID      tinyint unsigned not null,
  primary key (ID),
  constraint RAD_FK_STATUS_ID foreign key (STATUS_ID) references STATUS (ID) on delete restrict on update restrict,
  constraint RAD_FK_ACCOUNT_ID foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);


/*==============================================================*/
/* Table: CARD                                                  */
/*==============================================================*/
create table CARD
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  ACCOUNT_ID           bigint unsigned not null,
  CARD_NUMBER          bigint(16) unsigned not null unique,
  PIN                  SMALLINT(4) unsigned not null,
  CVV                  SMALLINT(3) unsigned not null,
  EXPIRE_DATE          date not null,
  primary key (ID),
  constraint CARD_FK_ACCOUNT_ID foreign key (ACCOUNT_ID) references ACCOUNT (ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: CREDIT_REQUEST                                        */
/*==============================================================*/
create table CREDIT_REQUEST
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  USER_ID              int unsigned not null,
  INTEREST_RATE        smallint not null,
  VALIDITY_DATE        date not null,
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
  ACCOUN_TO            bigint unsigned not null,
  OPERATION_DATE       datetime not null,
  primary key (ID),
  constraint FK_ACCOUNT_ID_FROM foreign key (ACCOUNT_FROM) references ACCOUNT(ID) on delete restrict on update restrict,
  constraint FK_ACCOUNT_ID_TO foreign key (ACCOUN_TO) references ACCOUNT(ID) on delete restrict on update restrict
);

/*==============================================================*/

insert into ROLE (ID, NAME) VALUES (1, 'ADMINISTRATOR'), (2, 'MANAGER'), (10, 'USER');

insert into USER (ROLE_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, PASSWORD) values
  (1, 'John', 'Ukraine', 'ivan.horpynych@gmail.com', '+380661715108', '123'),
  (2, 'Ivan', 'Horpynych-Raduzhenko', 'test@email.com', '+80661234567', '123'),
  (10, 'John', 'Tester', 'test@test.com', '+123456789123', '123');

insert into ACCOUNT_TYPE (ID, NAME) values (4, 'CREDIT'), (8, 'DEBIT'), (16, 'REGULAR');

insert into STATUS (ID, NAME) values (1, 'ACTIVE'), (4, 'PENDING'), (8, 'REJECT'), (16, 'BLOCKED');

start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'DEBIT'));
insert into DEBIT_ACCOUNT_DETAILS (ID, BALANCE, LAST_OPERATION, MIN_BALANCE, ANNUAL_RATE, STATUS_ID) values (last_insert_id(),2200,CURDATE(),1000, 8, (select ID from STATUS where NAME = 'ACTIVE'));

insert into CARD (ACCOUNT_ID, CARD_NUMBER, PIN, CVV, EXPIRE_DATE) values (last_insert_id(),1111111111111111,1234,444,'2019-1-01');

commit;


start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'CREDIT'));
insert into CREDIT_ACCOUNT_DETAILS (ID, BALANCE, CREDIT_LIMIT, INTEREST_RATE, LAST_OPERATION, ACCRUED_INTEREST, VALIDITY_DATE, STATUS_ID) values (last_insert_id(),100,2500,12,CURDATE(),123,'2019-1-01', (select ID from STATUS where NAME = 'ACTIVE'));

insert into CARD (ACCOUNT_ID, CARD_NUMBER, PIN, CVV, EXPIRE_DATE) values (last_insert_id(),2222222222222222,1234,555,'2019-1-01');

commit;

start transaction;
insert into ACCOUNT (USER_ID, TYPE_ID) values ((select ID
                                                from USER
                                                where (select id
                                                       from role
                                                       where NAME = 'USER') = ROLE_ID), (select ID
                                                                                         from ACCOUNT_TYPE
                                                                                         where NAME = 'REGULAR'));
insert into REGULAR_ACCOUNT_DETAILS (ID, BALANCE, STATUS_ID) values (last_insert_id(),150, (select ID from STATUS where NAME = 'ACTIVE'));

insert into CARD (ACCOUNT_ID, CARD_NUMBER, PIN, CVV, EXPIRE_DATE) values (last_insert_id(),3333333333333333,1234,666,'2019-1-01');

commit;

insert into CREDIT_REQUEST (USER_ID, INTEREST_RATE, VALIDITY_DATE, CREDIT_LIMIT, STATUS_ID) values
  ((select ID
    from USER
    where (select id
           from role
           where NAME = 'USER') = ROLE_ID),5,'2019-1-01',5000, (select ID from STATUS where NAME = 'PENDING'));


insert into PAYMENT (AMOUNT, ACCOUNT_FROM, ACCOUN_TO, OPERATION_DATE) VALUES (1200,3,1,curdate());