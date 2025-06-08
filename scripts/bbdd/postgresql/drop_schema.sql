
    alter table GOE_PROCEDIMENT 
       drop constraint GOE_PROCEDIMENT_UNITAT_FK;

    drop table if exists GOE_PROCEDIMENT cascade;

    drop table if exists GOE_UNITATORGANICA cascade;

    drop sequence if exists GOE_PROCEDIMENT_SEQ;

    drop sequence if exists GOE_UNITATORGANICA_SEQ;
