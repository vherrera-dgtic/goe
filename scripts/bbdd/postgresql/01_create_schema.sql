
    create sequence GOE_PROCEDIMENT_SEQ start 1 increment 1;
    create sequence GOE_UNITATORGANICA_SEQ start 1 increment 1;

    create table GOE_PROCEDIMENT (
       ID int8 not null,
        CODISIA varchar(8) not null,
        NOM varchar(50) not null,
        UNITATORGANICAID int8 not null,
        constraint GOE_PROCEDIMENT_PK primary key (ID)
    );

    create table GOE_UNITATORGANICA (
       ID int8 not null,
        CODIDIR3 varchar(9) not null,
        DATACREACIO date not null,
        ESTAT int4 not null,
        NOM varchar(50) not null,
        constraint GOE_UNITAT_PK primary key (ID)
    );

    create index GOE_PROCEDIMENT_PK_I on GOE_PROCEDIMENT (ID);
    create index GOE_PROCEDIMENT_CODISIA_UK_I on GOE_PROCEDIMENT (CODISIA);
    create index GOE_PROCEDIMENT_UNITAT_FK_I on GOE_PROCEDIMENT (UNITATORGANICAID);

    alter table GOE_PROCEDIMENT 
       add constraint GOE_PROCEDIMENT_CODISIA_UK unique (CODISIA);

    create index GOE_UNITAT_PK_I on GOE_UNITATORGANICA (ID);
    create index GOE_UNITAT_CODIDIR3_UK_I on GOE_UNITATORGANICA (CODIDIR3);

    alter table GOE_UNITATORGANICA 
       add constraint GOE_UNITAT_CODIDIR3_UK unique (CODIDIR3);

    alter table GOE_PROCEDIMENT 
       add constraint GOE_PROCEDIMENT_UNITAT_FK 
       foreign key (UNITATORGANICAID) 
       references GOE_UNITATORGANICA;
