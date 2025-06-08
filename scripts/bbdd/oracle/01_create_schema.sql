
    create sequence GOE_PROCEDIMENT_SEQ start with 1 increment by  1;
    create sequence GOE_UNITATORGANICA_SEQ start with 1 increment by  1;

    create table GOE_PROCEDIMENT (
        ID number(19,0) not null,
        CODISIA varchar2(8 char) not null,
        NOM varchar2(50 char) not null,
        UNITATORGANICAID number(19,0) not null
    );

    create table GOE_UNITATORGANICA (
        ID number(19,0) not null,
        CODIDIR3 varchar2(9 char) not null,
        DATACREACIO date not null,
        ESTAT number(10,0) not null,
        NOM varchar2(50 char) not null
    );

    create index GOE_PROCEDIMENT_PK_I on GOE_PROCEDIMENT (ID);
    create index GOE_PROCEDIMENT_CODISIA_UK_I on GOE_PROCEDIMENT (CODISIA);
    create index GOE_PROCEDIMENT_UNITAT_FK_I on GOE_PROCEDIMENT (UNITATORGANICAID);

    alter table GOE_PROCEDIMENT
        add constraint GOE_PROCEDIMENT_PK primary key (ID);

    alter table GOE_PROCEDIMENT
        add constraint GOE_PROCEDIMENT_CODISIA_UK unique (CODISIA);

    create index GOE_UNITAT_PK_I on GOE_UNITATORGANICA (ID);
    create index GOE_UNITAT_CODIDIR3_UK_I on GOE_UNITATORGANICA (CODIDIR3);

    alter table GOE_UNITATORGANICA
        add constraint GOE_UNITAT_PK primary key (ID);

    alter table GOE_UNITATORGANICA
        add constraint GOE_UNITAT_CODIDIR3_UK unique (CODIDIR3);

    alter table GOE_PROCEDIMENT
        add constraint GOE_PROCEDIMENT_UNITAT_FK
        foreign key (UNITATORGANICAID)
        references GOE_UNITATORGANICA;

    -- Grants per l'usuari www_goe
    -- seqüències
    GRANT SELECT, ALTER ON GOE_PROCEDIMENT_SEQ TO WWW_GOE;
    GRANT SELECT, ALTER ON GOE_UNITATORGANICA_SEQ TO WWW_GOE;
    -- taules
    GRANT SELECT, INSERT, UPDATE, DELETE ON GOE_PROCEDIMENT TO WWW_GOE;
    GRANT SELECT, INSERT, UPDATE, DELETE ON GOE_UNITATORGANICA TO WWW_GOE;


