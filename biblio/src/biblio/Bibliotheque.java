package biblio;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.time.*;

import com.mysql.cj.util.Util;
public class Bibliotheque {
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


	static Scanner scanner=new Scanner(System.in);


	public void menuEtudiantEnseignant(Utilisateur user,Livre livre,Emprunt emprunt){		
		int choix;
			System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");
		System.out.println("4:emprunter ou retourner un livre");	
		System.out.println("entrer votre choix");
		 choix=scanner.nextInt();
			switch(choix){
	
	case 1:
	livre.ConsulterCatalogue(connecter());
	break;
	case 2:
	livre.rechercherunlivre();
	break;
	case 3:
	System.out.println(" affiche des details");
	break;
	case 4:
	emprunt.gestionempruntretour(user);
	break;
	case 0:
	System.exit(1);

}
			if(choix!=0){
		menuEtudiantEnseignant(user,livre,emprunt);
	}
	}


	public void menuBibliothequaire(){
		System.out.println("1: Notification par e-mail pour les rappels de retour");
		System.out.println("2: Generation des rapports statistiques");
		int choix=scanner.nextInt();
		switch (choix) {
			case 1:
				
				break;
		
			case 2:
				break;
		}

	}

public void retourner(Utilisateur user){
	Connection connection=connecter();
	System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
try (PreparedStatement ps=connection.prepareStatement("SELECT * FROM emprunt WHERE id_livre=?")) {
	ps.setInt(1, id);
	ResultSet rs=ps.executeQuery();
	if(!rs.next()||rs.getString("status").equals("terminé")){
		System.out.println("le livre n'est pas emprunté");
	}
	else{
			int idemprunt=rs.getInt(1);
			try (PreparedStatement ps1=connection.prepareStatement("UPDATE emprunt SET status='terminé' WHERE id_emprunt= ?")) {
			
			ps1.setInt(1, idemprunt);
			ps1.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
} catch (SQLException e) {
	System.out.println(e);
}
}
public void emprunter(Utilisateur user){
	Connection connection=connecter();
System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
try (PreparedStatement ps1 = connection.prepareStatement("SELECT * from emprunt WHERE id_livre=?")) {
	ps1.setInt(1,id);
	ResultSet rs=ps1.executeQuery();
	if (!rs.next()||rs.getString("status").equals("en cours")){
		System.out.println("livre en cours d'emprunte");
	}
	else{
try (PreparedStatement ps = connection.prepareStatement("INSERT INTO emprunt (date_emprunt,date_retour,status,id_utilisateur,id_livre) values (?,?,?,?,?) ")) {
		ps.setObject(1, LocalDate.now());
		ps.setObject(2, LocalDate.now().plusMonths(1));
		ps.setString(3, "en cours");
		ps.setInt(4, user.id);
		ps.setInt(5, id);
		ps.executeUpdate();

	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	}
} catch (SQLException e1) {
	e1.printStackTrace();
	
}

	
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
public static void main(String[]args) {
	Bibliotheque biblio= new Bibliotheque();
	Utilisateur user=new Utilisateur();
	Livre livre=new Livre();
	Emprunt emprunt=new Emprunt();
	
	try {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		user.connecter();
		Utilisateur utilisateur=user.authentifier();		
		if(utilisateur.role.equals("etudiant")|| utilisateur.role.equals("enseignant")){
			biblio.menuEtudiantEnseignant(utilisateur,livre,emprunt);
			
		}
		else{
			biblio.menuBibliothequaire();
		}
	}
	catch(Exception e) {
		System.out.println(e);
	}
}
}