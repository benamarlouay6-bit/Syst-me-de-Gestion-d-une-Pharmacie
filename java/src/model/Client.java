package model;

public class Client {
    
    private String nom;
    private String telephone;
    private String cin;
    public Client(String nom,String telephone,String cin)
    {
        
        this.nom=nom;
        this.telephone=telephone;
        this.cin=cin;
    }

   

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }
}
