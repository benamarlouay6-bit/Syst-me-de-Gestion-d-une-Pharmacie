package model;

public class Fournisseur {
    private int id;
    private String nom;
    private String telephone;
    private String mail;
    Fournisseur(int id, String nom, String telephone, String mail){
        this.id=id
        this.nom=nom
        this.telephone=telephone
        this.mail=mail
            }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }
}
