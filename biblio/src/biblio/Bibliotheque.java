package biblio;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.time.*;
import javax.swing.JFrame;

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


	public void menuEtudiantEnseignant(Utilisateur user,Livre livre,Emprunt emprunt,Etudiantenseignant etudiantenseignant){		
		int choix;
			System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");
		System.out.println("4:emprunter ou retourner un livre");
		System.out.println("5:consulter l'historique des emprunts");	
		System.out.println("entrer votre choix");
		 choix=scanner.nextInt();
			switch(choix){
	
	case 1:
	etudiantenseignant.ConsulterCatalogue(connecter());
	break;
	case 2:
	etudiantenseignant.rechercherunlivre();
	break;
	case 3:
	livre.afficherdetaildunlivre();
	break;
	case 4:
	etudiantenseignant.gestionempruntretour(user);
	break;
	case 5:
	etudiantenseignant.historiquedesemprunts(user);
	break;
	case 0:
	System.exit(1);

}
			if(choix!=0){
		menuEtudiantEnseignant(user,livre,emprunt,etudiantenseignant);
	}
	}


	public void menuBibliothequaire(Bibliothequaire bibliothequaire){
		System.out.println("1: Notification par e-mail pour les rappels de retour");
		System.out.println("2: Generation des rapports statistiques");
		int choix=scanner.nextInt();
		switch (choix) {
			case 1:
				break;
			case 2:
			bibliothequaire.genererdesrapports();
				break;
		}
if(choix !=0){
	menuBibliothequaire(bibliothequaire);
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
		if(utilisateur.getrole().equals("etudiant")|| utilisateur.getrole().equals("enseignant")){
			Etudiantenseignant etudenseignant=new Etudiantenseignant();
			biblio.menuEtudiantEnseignant(utilisateur,livre,emprunt,etudenseignant);
	
		}
		else{
			Bibliothequaire bibliothequaire=new Bibliothequaire();
			biblio.menuBibliothequaire(bibliothequaire);
		}
	}
	catch(Exception e) {
		System.out.println(e);
	}
}
}