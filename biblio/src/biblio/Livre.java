package biblio;
public class Livre {
    private int idLivre;
    private String titre;
    private String auteur;
    private String genre;
    private boolean disponibilite;

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }
    public void Afficher() {
        System.out.println("l'id: "+this.idLivre);

        System.out.println("le titre: "+this.titre);

        System.out.println("l'auteur: "+this.auteur);

        System.out.println("le genre: "+this.genre);

       
    }
    
}
