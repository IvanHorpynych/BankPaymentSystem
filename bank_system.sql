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
   ID                   bigint unsigned not null AUTO_INCREMENT,
   ROLE_ID              tinyint unsigned not null,
   FIRST_NAME           VARCHAR(100) not null,
   LAST_NAME            VARCHAR(150) not null,
   EMAIL                VARCHAR(200),
   PHONE_NUMBER         VARCHAR(50),
   PASSWORD             VARCHAR(255) not null,
   primary key (ID),
   foreign key (ROLE_ID) references ROLE (ID) on delete restrict on update restrict
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
/* Table: ACCOUNT                                               */
/*==============================================================*/
create table ACCOUNT
(
   ID                   bigint unsigned not null AUTO_INCREMENT,
   USER_ID              bigint unsigned not null,
   TYPE_ID              tinyint unsigned not null,
   primary key (ID),
   foreign key (USER_ID) references USER (ID) on delete restrict on update restrict,
   foreign key (TYPE_ID) references ACCOUNT_TYPE (ID) on delete restrict on update restrict,
);


/*==============================================================*/
/* Table: CREDIT_ACCOUNT_DETAILS                                */
/*==============================================================*/
create table CREDIT_ACCOUNT_DETAILS
(
   ID                   bigint unsigned not null AUTO_INCREMENT,
   BALANCE              DECIMAL(13,4) not null,
   CREDIT_LIMIT         DECIMAL(13,4) not null,
   ANNURAL_RATE         smallint not null,
   LAST_OPERATION       date not null,
   ACCRUED_INTEREST     DECIMAL(13,4) not null,
   VALIDITY_DATE        date not null,
   ACTING               BOOLEAN NOT NULL,
   primary key (ID),
   foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
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
   ACTING               BOOLEAN NOT NULL,
   primary key (ID),
   foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: CARD                                                  */
/*==============================================================*/
create table CARD
(
   ACCOUNT_ID           bigint unsigned not null,
   CARD_NUMBER          bigint(16) unsigned not null unique,
   PIN                  SMALLINT(4) unsigned not null,
   CVV                  SMALLINT(3) unsigned not null,
   EXPIRE_DATE          date not null,
   foreign key (ACCOUNT_ID) references ACCOUNT (ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: CREDIT_REQUEST                                        */
/*==============================================================*/
create table CREDIT_REQUEST
(
   ID                   bigint unsigned not null,
   USER_ID              bigint unsigned not null,
   ANNURAL_RATE         smallint not null,
   REJECT               BOOLEAN NOT NULL,
   VALIDITY_DATE        date not null,
   CREDIT_LIMIT         DECIMAL(13,4) not null,
   primary key (ID),
   foreign key (USER_ID) references USER(ID) on delete restrict on update restrict
);

/*==============================================================*/
/* Table: PAYMENT                                               */
/*==============================================================*/
create table PAYMENT
(
   ID                   bigint unsigned not null,
   AMOUNT               DECIMAL(13,4) not null,
   ACCOUNT_FROM         bigint unsigned not null,
   ACCOUN_TO            bigint unsigned not null,
   OPERATION_DATE       date not null,
   primary key (ID),
   foreign key (ACCOUNT_FROM) references ACCOUNT(ID) on delete restrict on update restrict,
   foreign key (ACCOUN_TO) references ACCOUNT(ID) on delete restrict on update restrict
);


///////////////////
create table DEBIT_ACCOUNT_DETAILS
(
  ID                   bigint unsigned not null AUTO_INCREMENT,
  BALANCE              DECIMAL(13,4) not null,
  LAST_OPERATION       date not null,
  MIN_BALANCE          DECIMAL(13,4) not null,
  ACTING               BOOLEAN NOT NULL,
  primary key (ID),
  foreign key (ID) references ACCOUNT (ID) on delete restrict on update restrict,
  CONSTRAINT chk_type_of_acc
  CHECK (case when (select NAME from ACCOUNT_TYPE where ID = (select TYPE_ID from ACCOUNT where ACCOUNT.ID = DEBIT_ACCOUNT_DETAILS.ID)) in ('debit') then
    true
         else false end)
);