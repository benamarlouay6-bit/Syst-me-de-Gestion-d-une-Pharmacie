use pharmacie;

CREATE TABLE employe (
    idEmploye INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);

CREATE TABLE administrateur (
    idAdministrateur INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL
);


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

CREATE TABLE commandeFournisseur (
    idCommande INT AUTO_INCREMENT PRIMARY KEY,
    dateCommande DATE NOT NULL,
    dateReception DATE,
    quantite INT NOT NULL,
    montantTotal DOUBLE NOT NULL,
    etat VARCHAR(30) NOT NULL
        CHECK (etat IN ('EN_ATTENTE', 'RECUE', 'ANNULEE')),
    idFournisseur INT NOT NULL,
    idProduit INT NOT NULL,

    CONSTRAINT fk_commande_fournisseur
        FOREIGN KEY (idFournisseur) REFERENCES fournisseur(id),

    CONSTRAINT fk_commande_produit
        FOREIGN KEY (idProduit) REFERENCES produit(idProduit)
);


CREATE TABLE vente (
    idVente INT AUTO_INCREMENT PRIMARY KEY,
    dateVente DATE NOT NULL,
    quantite INT NOT NULL,
    montantTotal DOUBLE NOT NULL,
    idClient INT NOT NULL,
    idProduit INT NOT NULL,

    CONSTRAINT fk_vente_client
        FOREIGN KEY (idClient) REFERENCES client(idClient),

    CONSTRAINT fk_vente_produit
        FOREIGN KEY (idProduit) REFERENCES produit(idProduit)
);
