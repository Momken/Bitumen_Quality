
ALTER TABLE Lager ADD FOREIGN KEY(besitzerId) REFERENCES Lager(id) ON UPDATE CASCADE ON DELETE CASCADE;

CREATE TABLE Ernte(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1) primary key,
    sortenId INTEGER,
    schlagId INTEGER,
    lagerId INTEGER,
    erntedatum DATE,
    erntemenge DOUBLE,
    ernteausfall DOUBLE,
    isDeleted BOOLEAN DEFAULT FALSE
);