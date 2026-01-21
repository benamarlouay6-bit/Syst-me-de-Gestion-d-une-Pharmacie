INSERT INTO employe (nom)
VALUES (?);

INSERT INTO administrateur (nom)
VALUES (?);




INSERT INTO fournisseur (nom,telephone,mail)
VALUES
('PharmaTunisie', '71234567', 'contact@pharmatunisie.tn'),
('MedDistrib', '73456789', 'info@meddistrib.tn'),
('HealthPlus', '70321456', 'support@healthplus.tn');

INSERT INTO medicament (nom) VALUES
('Doliprane'),
('Efferalgan'),
('Amoxicilline'),
('Aspirine'),
('Ibuprofene'),
('Vitamine C'),

('Paracetamol'),
('Augmentin'),
('Spasfon'),
('Smecta'),
('Gaviscon'),
('Zyrtec'),

('Insuline'),
('Ventoline'),
('Cardioaspirine'),
('Levothyrox'),
('Omeprazole'),
('Metformine');


INSERT INTO fournisseur_medicament 
(idFournisseur, idMedicament, prixFournisseur, quantiteStock) VALUES
(1, 1, 2.50, 100),
(1, 2, 2.80, 120),
(1, 3, 8.00, 60),
(1, 4, 3.00, 90),
(1, 5, 3.50, 75),
(1, 6, 1.80, 200);

INSERT INTO fournisseur_medicament 
(idFournisseur, idMedicament, prixFournisseur, quantiteStock) VALUES
(2, 7, 2.30, 110),
(2, 8, 9.50, 50),
(2, 9, 2.00, 130),
(2, 10, 1.90, 140),
(2, 11, 4.20, 80),
(2, 12, 3.60, 95);

INSERT INTO fournisseur_medicament 
(idFournisseur, idMedicament, prixFournisseur, quantiteStock) VALUES
(3, 13, 15.00, 40),
(3, 14, 6.00, 70),
(3, 15, 2.70, 100),
(3, 16, 4.50, 85),
(3, 17, 3.20, 90),
(3, 18, 5.80, 65);



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
(dateCommande, dateReception, quantite, montantTotal, etat, idFournisseur, nom_medicamment)
VALUES (?, ?, ?, ?, ?, ?, ?);

SELECT id FROM fournisseur WHERE id = ?

 SELECT fm.prixFournisseur 
        FROM medicament m 
        JOIN fournisseur_medicament fm ON m.idMedicament = fm.idMedicament 
        WHERE m.nom = ? AND fm.idFournisseur = ?;


SELECT etat FROM commandefournisseur WHERE idCommande = ?;

 SELECT fm.prixFournisseur 
        FROM medicament m 
        JOIN fournisseur_medicament fm ON m.idMedicament = fm.idMedicament
        WHERE m.nom = ? AND fm.idFournisseur = ?;

-- Modifier une commande fournisseur
UPDATE commandeFournisseur
SET quantite = ?, montantTotal = ?
WHERE idCommande = ?;

-- Annuler une commande fournisseur
UPDATE commande_fournisseur SET etat = 'ANNULÉE' WHERE idCommande = ?;


-- reception d'une commande
UPDATE commandefournisseur SET etat = 'ANNULEE' WHERE idCommande = ?

-- augmentation de stock
UPDATE produit
SET quantiteStock = quantiteStock + ?
WHERE idProduit = ?;




-- enregistrerVente
INSERT INTO vente (dateVente, quantite, montantTotal, idClient, idProduit)
VALUES (?, ?, ?, ?, ?);

-- diminuer la quantite de stock
UPDATE produit SET quantiteStock = quantiteStock + ? 
        WHERE nomMedicament = ?;

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
