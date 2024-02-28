/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     28/02/2024 17:59:35                          */
/*==============================================================*/


drop table if exists ANOTACAO;

drop table if exists AUTORS;

drop table if exists OBRA;

drop table if exists REGISTO_DE_ACOES;

drop table if exists REVISAO;

drop table if exists REVISORS;

drop table if exists USERS;

/*==============================================================*/
/* Table: ANOTACAO                                              */
/*==============================================================*/
create table ANOTACAO
(
   ID_ANOTACAO          numeric(8,0) not null,
   ID_REVISAO           numeric(5,0),
   ID_USER              numeric(10,0),
   DESCRICAO            varchar(50) not null,
   NR_PAGINA            numeric(5,0) not null,
   PARAGRAFO            varchar(1000) not null,
   DATA_ANOTACAO        datetime not null,
   primary key (ID_ANOTACAO)
);

/*==============================================================*/
/* Table: AUTORS                                                */
/*==============================================================*/
create table AUTORS
(
   ID_USER              numeric(10,0) not null,
   NUMERO_CONTRIBUINTE_AUTOR numeric(9,0) not null,
   MORADA_AUTOR         varchar(50) not null,
   INICIO_ATIVIDADE     timestamp not null,
   CONTACTO_TELEFONICO_AUTOR numeric(9,0) not null,
   ESTILO_LITERARIO_AUTOR varchar(30) not null,
   primary key (ID_USER)
);

/*==============================================================*/
/* Table: OBRA                                                  */
/*==============================================================*/
create table OBRA
(
   ID                   numeric(10,0) AUTO_INCREMENT not null,
   ID_USER              numeric(10,0),
   AUT_ID_USER          numeric(10,0),
   AUTHOR               varchar(50) not null,
   TITLE                varchar(50) not null,
   SUBTITLE             varchar(200) not null,
   TIPO_PUBLICACAO      varchar(50) not null,
   NUMERO_PAGINAS       numeric(5,0) not null,
   NUMERO_PALAVRAS      int not null,
   CODIGO_ISBN          int not null,
   NUMERO_EDICAO        int not null,
   DATA_SUBMISSAO       datetime not null,
   DATA_APROVACAO       datetime not null,
   ESTILO_LITERARIO_OBRA varchar(30) not null,
   primary key (ID)
);

/*==============================================================*/
/* Table: REGISTO_DE_ACOES                                      */
/*==============================================================*/
create table REGISTO_DE_ACOES
(
   ID_LOG               numeric(5,0) AUTO_INCREMENT not null,
   ID_USER              numeric(10,0),
   HORA                 varchar(10) not null,
   ACAO                 varchar(100) not null,
   DATA_REGISTO         datetime not null,
   primary key (ID_LOG)
);

/*==============================================================*/
/* Table: REVISAO                                               */
/*==============================================================*/
create table REVISAO
(
   ID_REVISAO           numeric(5,0) AUTO_INCREMENT not null,
   ID_USER              numeric(10,0),
   NUMERO_SERIE         int not null,
   DATA_DE_REALIZACAO   varchar(10) not null,
   TEMPO_DECORRIDO      varchar(10) not null,
   OBSERVACOES_GENERICAS varchar(50) not null,
   CUSTO_DO_PROCESSO    float(5) not null,
   ESTADO_REVISAO       varchar(20) not null,
   primary key (ID_REVISAO)
);

/*==============================================================*/
/* Table: REVISORS                                              */
/*==============================================================*/
create table REVISORS
(
   ID_USER              numeric(10,0) not null,
   NUMERO_CONTRIBUINTE_REVISOR numeric(9,0) not null,
   MORADA_REVISOR       varchar(50) not null,
   AREA_ESPECIALIZACAO  varchar(20) not null,
   FORMACAO_ACADEMICA   varchar(20) not null,
   CONTACTO_TELEFONICO_REVISOR numeric(9,0) not null,
   primary key (ID_USER)
);

/*==============================================================*/
/* Table: USERS                                                 */
/*==============================================================*/
create table USERS
(
   ID_USER              numeric(10,0) AUTO_INCREMENT not null,
   NAME                 varchar(30) not null,
   LOGIN                varchar(20) not null,
   PASSWORD             varchar(20) not null,
   EMAIL                varchar(30) not null,
   TIPO                 varchar(20) not null,
   ESTADO_USER          bool not null,
   primary key (ID_USER)
);

alter table ANOTACAO add constraint FK_REVISAO_ANOTACAO foreign key (ID_REVISAO)
      references REVISAO (ID_REVISAO) on delete restrict on update restrict;

alter table ANOTACAO add constraint FK_REVISORS_ANOTACAO foreign key (ID_USER)
      references REVISORS (ID_USER) on delete restrict on update restrict;

alter table AUTORS add constraint FK_INHERITANCE foreign key (ID_USER)
      references USERS (ID_USER) on delete restrict on update restrict;

alter table OBRA add constraint FK_AUTORS_OBRA foreign key (AUT_ID_USER)
      references AUTORS (ID_USER) on delete restrict on update restrict;

alter table OBRA add constraint FK_REVISORS_OBRA foreign key (ID_USER)
      references REVISORS (ID_USER) on delete restrict on update restrict;

alter table REGISTO_DE_ACOES add constraint FK_USERS_REGISTO_DE_ACOES foreign key (ID_USER)
      references USERS (ID_USER) on delete restrict on update restrict;

alter table REVISAO add constraint FK_REVISORS_REVISAO foreign key (ID_USER)
      references REVISORS (ID_USER) on delete restrict on update restrict;

alter table REVISORS add constraint FK_INHERITANCE2 foreign key (ID_USER)
      references USERS (ID_USER) on delete restrict on update restrict;

