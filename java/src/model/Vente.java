package model;

import java.sql.Date;

public class Vente {

    private int IdVente;
    private Date dateVente;
    private int quantite;
    private double montantTotal;
    private int idClient;
    private String nom_medicamment;

    Vente(Date dateVente, int quantite, double montantTotal, int idClient, String nom_medicamment){
        this.dateVente=dateVente;
        this.quantite=quantite;
        this.montantTotal;
        this.idClient=idClient;
        this.nom_medicamment=nom_medicamment;

       Vente(int IdVente,Date dateVente, int quantite, double montantTotal, int idClient, String nom_medicamment){
        this.IdVente=IdVente;
        this.dateVente=dateVente;
        this.quantite=quantite;
        this.montantTotal;
        this.idClient=idClient;
        this.nom_medicamment=nom_medicamment;
    }
   public int getIdVente(){
      return IdVente;}

    public void setIdVente(int idVente) {
        this.idVente = idVente;
    }

    public Date getDateVente() {
        return dateVente;
    }

    public void setDateVente(Date dateVente) {
        this.dateVente = dateVente;
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

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    
}
