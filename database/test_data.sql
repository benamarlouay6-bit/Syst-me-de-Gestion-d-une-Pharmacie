INSERT INTO employe (nom)
VALUES (?);

INSERT INTO administrateur (nom)
VALUES (?);


INSERT INTO produit (nom,prixF,prixC,quantiteStock,seuilAlerte)
VALUES
('Doliprane 500mg',2.20,3.50,120, 20),
('Efferalgan 1g',2.80,4.20,80, 15),
('Aspirine 500mg',1.90,3.00,60,10),
('Amoxicilline 500mg',5.50,7.80,40, 10),
('Vitamine C 1000mg',3.00,4.90,70,15),
('Ibuprofène 400mg',2.10,3.60,90,20),
('Smecta',2.50,4.00,50,10),
('Gaviscon',4.20,6.80,45,10),
('Biseptine',3.80,6.00,30,8),
('Spasfon',2.70,4.30,65,15);

INSERT INTO fournisseur (nom,telephone,mail)
VALUES
('PharmaTunisie', '71234567', 'contact@pharmatunisie.tn'),
('MedDistrib', '73456789', 'info@meddistrib.tn'),
('HealthPlus', '70321456', 'support@healthplus.tn');

INSERT INTO client (nom, telephone, cin)
VALUES
('Ahmed Ben Ali', '22123456', '01234567'),
('Sami Trabelsi', '98123456', '11223344'),
('Mouna Khelifi', '55123456', '22334455'),
('Nour Zoghlami', '99123456', '33445566'),
('Yassine Bouazizi', '24123456', '44556677'),
('Clinique El Amen', '71345678', NULL),
('Clinique Les Jasmins', '71788990', NULL),
('Clinique Ibn Sina', '71999888', NULL);


exemple pour louay fil java
(String sqlVente =
    "INSERT INTO vente (dateVente, quantite, montantTotal, idClient, idProduit) " +
    "VALUES (?, ?, ?, ?, ?)";

PreparedStatement psVente = cnx.prepareStatement(sqlVente);

// remplissage des ?
psVente.setDate(1, dateVente); // 👈 LA DATE
psVente.setInt(2, quantite);        // quantite
psVente.setDouble(3,  montantTotal); // montantTotal
psVente.setInt(4, idClient);        // idClient
psVente.setInt(5, idProduit);        // idProduit

psVente.executeUpdate();)

-- ajouter employe
INSERT INTO employe (nom)
VALUES (?);

-- liste des employe
SELECT * FROM employe;

-- ajouter administrateur
INSERT INTO administrateur (nom)
VALUES (?);

-- liste des administrateur
SELECT * FROM administrateur;


-- ajouter prouduit
INSERT INTO produit (nom, prixF, prix, quantiteStock, seuilAlerte)
VALUES (?, ?, ?, ?, ?);

-- Consulter tous les produits
SELECT * FROM produit;

-- Modifier un produit
UPDATE produit
SET nom = ?, prixF = ?, prix = ?, seuilAlerte = ?
WHERE idProduit = ?;

-- Supprimer un produit
DELETE FROM produit
WHERE idProduit = ?;

-- Produits en alerte de stock
SELECT *
FROM produit
WHERE quantiteStock <= seuilAlerte;


-- ajouter fournisseur
INSERT INTO fournisseur(nom,telephone,mail)
VALUES(?,?,?);

-- Liste des fournisseurs
SELECT * FROM fournisseur;

-- ajouter client
INSERT INTO client(nom,telephone,cin)
VALUES(?,?,?);

-- liste des clients
SELECT * FROM client;

-- Créer une commande fournisseur
INSERT INTO commandeFournisseur
(dateCommande, dateReception, quantite, montantTotal, etat, idFournisseur, idProduit)
VALUES (?, ?, ?, ?, ?, ?, ?);

-- Modifier une commande fournisseur
UPDATE commandeFournisseur
SET quantite = ?, montantTotal = ?
WHERE idCommande = ?;

-- Annuler une commande fournisseur
DELETE FROM commandefournisseur WHERE idCommande=?;


-- reception d'une commande
UPDATE commandeFournisseur
SET dateReception = ?, etat = 'RECUE'
WHERE idCommande = ?;

-- augmentation de stock
UPDATE produit
SET quantiteStock = quantiteStock + ?
WHERE idProduit = ?;




-- enregistrerVente
INSERT INTO vente (dateVente, quantite, montantTotal, idClient, idProduit)
VALUES (?, ?, ?, ?, ?);

-- diminuer la quantite de stock
UPDATE produit
SET quantiteStock = quantiteStock - ?
WHERE idProduit = ?;

-- liste  de vente
SELECT * FROM vente;

-- HISTORIQUE DES ACHATS CLIENT(chaque client les produit qu'il les a acheté)
SELECT c.nom , p.nom, v.quantite,v.montantTotal
FROM vente v,client c,produit p
WHERE v.idProduit =p.idProduit 
AND v.idClient=c.idClient;

-- HISTORIQUE DES ACHATS ENTRE DEUX DATES
SELECT c.nom,p.nom ,v.quantite,v.montantTotal
FROM vente v
JOIN client c ON v.idClient = c.idClient
JOIN produit p ON v.idProduit = p.idProduit
WHERE v.dateVente BETWEEN ? AND ?
ORDER BY v.dateVente;


-- RAPPORTS D’ANALYSE:

-- État des stocks
SELECT idProduit, nom, quantiteStock, seuilAlerte
FROM produit;


-- Chiffre d’affaires par période
SELECT SUM(v.montantTotal)-SUM(c.montantTotal) AS chiffreAffaires
FROM vente v,commandefournisseur c
WHERE c.etat='RECUE'
AND v.dateReception BETWEEN ? AND ?
AND v.dateVente BETWEEN ? AND ?;


-- Performance des fournisseurs
 -- Montant total commandé par fournisseur
SELECT f.nom, SUM(c.montantTotal) AS totalCommandes
FROM commandeFournisseur c
JOIN fournisseur f ON c.idFournisseur = f.id
GROUP BY f.nom;
