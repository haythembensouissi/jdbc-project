package biblio;


 class Rapport {
private String nomlivre;
private String nomutilisateur;
    public void setnomlivre(String nomlivre){
        this.nomlivre=nomlivre;
    }
      public void setnomutilisateur(String nomutilisateur){
        this.nomutilisateur=nomutilisateur;
    }
    public String getnomlivre(){
        return this.nomlivre;
    }
      public String getnomutilisateur(){
       return this.nomutilisateur;
    }
}