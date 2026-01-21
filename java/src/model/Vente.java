package model;

import java.sql.Date;

public class Vente {

    private int idVente;
    private Date dateVente;
    private int quantite;
    private double montantTotal;
    private String nom_medicamment;
    private int idClient;

    public Vente(int idVente, Date dateVente, int quantite,
                 double montantTotal, String nom_medicamment, int idClient) {
        this.idVente = idVente;
        this.dateVente = dateVente;
        this.quantite = quantite;
        this.montantTotal = montantTotal;
        this.nom_medicamment = nom_medicamment;
        this.idClient = idClient;
    }
    public Vente(Date dateVente, int quantite, String nom_medicamment, int idClient) {
        this.dateVente = dateVente;
        this.quantite = quantite;
        this.nom_medicamment = nom_medicamment;
        this.idClient = idClient;
    }


    public int getIdVente() { return idVente; }
    public Date getDateVente() { return dateVente; }
    public int getQuantite() { return quantite; }
    public double getMontantTotal() { return montantTotal; }
    public String getNom_medicamment() { return nom_medicamment; }
    public int getIdClient() { return idClient; }

	public void setMontantTotal(double montant) {this.montantTotal=montant;
		
	}
}
