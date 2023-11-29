package biblio;

import java.sql.*;
import java.util.Scanner;

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
    public static Connection connecter(){
		
			Connection connection;
			connection=null;
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","root");
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			return connection;
	}
    public static Livre[] ConsulterCatalogue(Connection connection) {
	Livre[] cat = new Livre[100];
    String query = "SELECT * FROM Livre";
    try (Statement statement = connection.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {
    	int i=0;
        while (resultSet.next()) {
            Livre livre = new Livre();
            livre.setIdLivre(resultSet.getInt("id_livre"));
            livre.setTitre(resultSet.getString("titre"));
            livre.setAuteur(resultSet.getString("auteur"));
            livre.setGenre(resultSet.getString("genre"));
            livre.setDisponibilite(resultSet.getBoolean("disponibilité"));
            cat[i]=livre;
            i++;
        }
		for(int j=0;j<i;j++){
			System.out.println(cat[j].getTitre());
			System.out.println(cat[j].getAuteur());
			System.out.println(cat[j].getGenre());
		}
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cat;
}
    	public Livre rechercherunlivre(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("entrer l'id");
		int id=scanner.nextInt();
		Connection connection=connecter();
		Livre livre=new Livre();
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM LIVRE WHERE id_livre=?")) {
			ps.setInt(1, id);
		ResultSet rs=ps.executeQuery();
		if(rs.next()){
			livre.setIdLivre(rs.getInt("id_livre"));
			livre.setTitre(rs.getString("titre"));
			livre.setAuteur(rs.getString("auteur"));
			livre.setGenre(rs.getString("genre"));
			livre.Afficher();
			if (rs.getBoolean("disponibilité")==true) {
				System.out.println("le livre existe");
			}
			else{
				System.out.println("le livre n'existe pas");
			}
		}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("le livre n'existe pas");
		}

		return null;
	}
}
