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
            livre.setDisponibilite(resultSet.getBoolean("disponibilité"));
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
			System.out.println("le livre existe");
			
		}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("le livre n'existe pas");
		}

		return livre;
        }
        public void gestionempruntretour(Utilisateur user){
	System.out.println("1:emprunter un livre");
	System.out.println("2:retourner un livre");
	int choix=scanner.nextInt();
	switch (choix) {
		case 1:
		emprunter(user);
		break;
	
		case 2:
		retourner(user);
		break;
	}
}
public void emprunter(Utilisateur user){
	Connection connection=connecter();
    boolean encours=false;
System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
try (PreparedStatement ps=connection.prepareStatement("SELECT * from livre where id_livre=?")) {
    ps.setInt(1, id);
    ResultSet rs=ps.executeQuery();
    if(!rs.next()||rs.getString("disponibilité").equals("non disponible")){
        System.out.println("le livre n'existe pas");
    }
    else{
        try (PreparedStatement ps3=connection.prepareStatement("SELECT * from emprunt where id_livre= ?")) {
            ps3.setInt(1, id);
            ResultSet rs3=ps3.executeQuery();
            while(rs3.next()){
                if(rs3.getString("status").equals("en cours")){
                    System.out.println("le livre est en cours d'emprunt");
                    encours=true;
                }
                
                
            }
         

            

        } catch (Exception e) {
            // TODO: handle exception
        }
        if(encours==false){
               try (PreparedStatement ps4 = connection.prepareStatement("INSERT INTO emprunt (date_emprunt,date_retour,status,id_utilisateur,id_livre) values (?,?,?,?,?) ")) {
		ps4.setObject(1, LocalDate.now());
		ps4.setObject(2, LocalDate.now().plusMonths(1));
		ps4.setString(3, "en cours");
		ps4.setInt(4, user.getid());
		ps4.setInt(5, id);
		ps4.executeUpdate();

	} catch (SQLException e) {
		
		System.out.println(e);
	}
        }
     
        
    }
} catch (Exception e) {
    // TODO: handle exception
}


}
public void retourner(Utilisateur user){
	Connection connection=connecter();
	System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
boolean terminé=false;
try (PreparedStatement ps=connection.prepareStatement("SELECT * FROM emprunt WHERE  id_livre=? and status='en cours'")) {
	ps.setInt(1, id);
	ResultSet rs=ps.executeQuery();
    
if(!rs.next()||rs.getString("status").equals("terminé")){
    System.out.println(rs.getInt(1));
		System.out.println("le livre n'est pas emprunté");
	}

	
	else{
			int idemprunt=rs.getInt(1);
            System.out.println(idemprunt);
			try (PreparedStatement ps1=connection.prepareStatement("UPDATE emprunt SET status='terminé' WHERE id_emprunt= ?")) {
			
			ps1.setInt(1, idemprunt);
			ps1.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
} catch (SQLException e) {
	System.out.println("pas de livre emprunté");
}
}
public Emprunt[] historiquedesemprunts(Utilisateur user){
	Emprunt[] res=new Emprunt[100];
	Connection connection=connecter();
	try (PreparedStatement ps=connection.prepareStatement("SELECT * from emprunt where id_utilisateur=?")) {
		ps.setInt(1, user.getid());
		int i=0;
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			Emprunt emprunt=new Emprunt();
			emprunt.setidemprunt(rs.getInt("id_emprunt"));
			emprunt.setidlivre(rs.getInt("id_livre"));
			emprunt.setidutilisateur(rs.getInt("id_utilisateur"));;
			emprunt.setstatus(rs.getString("status"));
			emprunt.setdateemprunt(rs.getDate("date_emprunt"));
			emprunt.setdateretour(rs.getDate("date_retour"));
			res[i]=emprunt;
			i++;
		}
		for(int j=0;j<i;j++){
			System.out.println("l'id de l'emprunte: "+res[j].getidemprunt());
			System.out.println("l'id de livre "+res[j].getidlivre());
			System.out.println("l'id d'utilisateur "+res[j].getidutilisateur());
			System.out.println("le status: "+res[j].getsatus());
			System.out.println("emprunté le : "+res[j].getdateemprunt());
			System.out.println("retourné le : "+res[j].getdateretour());
		}
	} catch (SQLException e) {
		System.out.println("pas de livres ");
	}
	return res;
}
}