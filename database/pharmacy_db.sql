use pharmacie;
CREATE TABLE produit (
    idProduit INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prixF DOUBLE NOT NULL,
    prixC DOUBLE NOT NULL,
    quantiteStock INT NOT NULL,
    seuilAlerte INT NOT NULL
);

CREATE TABLE fournisseur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    mail VARCHAR(100) NOT NULL
);


CREATE TABLE client (
    idClient INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    cin VARCHAR(20)
);

