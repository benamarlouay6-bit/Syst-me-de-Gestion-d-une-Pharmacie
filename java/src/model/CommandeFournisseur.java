package model;

import java.sql.Date;

public class CommandeFournisseur {

    
    private Date dateCommande;
    private Date dateReception;
    private int quantite;
    private double montantTotal;
    private String etat;
    private int idFournisseur;
    private String nom_medicamment;
    private int idCommande;
    
    public CommandeFournisseur(Date dateCommande, int quantite,int idFournisseur,String nom_medicamment)
    {
        this.dateCommande = dateCommande;
        this.quantite = quantite;
        this.idFournisseur = idFournisseur;
        this.nom_medicamment = nom_medicamment;
        this.etat = "EN_ATTENTE";      
        this.dateReception = null;     
        this.montantTotal = 0; 
    } 
    public int getIdCommande() {
        return idCommande;
    }
    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(int idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public String getNom_medicamment() {
        return nom_medicamment;
    }

    public void setNom_medicamment(String nom_medicamment) {
        this.nom_medicamment = nom_medicamment;
    }
}
