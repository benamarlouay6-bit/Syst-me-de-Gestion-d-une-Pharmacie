package model;

public class Produit {
    private int idProduit;
    private String nom;
    private double prixF;
    private double prixC;
    private int quantiteStock;
    private int seuilAlerte;
    public Produit(String nom,double prixF,double prixC,int quantiteStock,int seuilAlerte)
    {
    	this.nom=nom;
    	this.prixF=prixF;
    	this.prixC=prixC;
    	this.quantiteStock=quantiteStock;
    	this.seuilAlerte=seuilAlerte;
    }
    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public double getPrixF() { return prixF; }
    public void setPrixF(double prixF) { this.prixF = prixF; }

    public double getPrixC() { return prixC; }
    public void setPrixC(double prixC) { this.prixC = prixC; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }

    public int getSeuilAlerte() { return seuilAlerte; }
    public void setSeuilAlerte(int seuilAlerte) { this.seuilAlerte = seuilAlerte; }
    public boolean estInferieurAuSeuil() {
        return quantiteStock < seuilAlerte;
    }
    public String getEtatStock() {
        return estInferieurAuSeuil() ? " Stock bas" : "OK";
    }
    public boolean alerteStockBas() {
        return quantiteStock < seuilAlerte;
    }


}
