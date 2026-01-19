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