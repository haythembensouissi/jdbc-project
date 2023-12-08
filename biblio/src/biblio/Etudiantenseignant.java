 package biblio;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;
class Etudiantenseignant extends Utilisateur{
Etudiantenseignant(){
    super();
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
            livre.setDisponibilite(resultSet.getString("disponibilité"));
            cat[i]=livre;
            i++;
        }
		for(int j=0;j<i;j++){
			System.out.print("titre :"+cat[j].getTitre()+" ");
			System.out.print("auteur :"+cat[j].getAuteur()+" ");
			System.out.println("genre :"+cat[j].getGenre());
		}
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return cat;
}
  
   

}