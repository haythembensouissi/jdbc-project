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


	public void menuEtudiantEnseignant(Utilisateur user,Livre livre,Emprunt emprunt,Etudiantenseignant etudiantenseignant,Reservation reservation){		
		int choix;
			System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");
		System.out.println("4:emprunter ou retourner un livre");
		System.out.println("5:consulter l'historique des emprunts");	
		System.out.println("6:faire une reservation");	
		System.out.println("7:annuler la reservation d un livre");
		System.out.println("entrer votre choix");
		 choix=scanner.nextInt();
			switch(choix){
	
	case 1:
	etudiantenseignant.ConsulterCatalogue(connecter());
	break;
	case 2:
	livre.rechercherunlivre();
	break;
	case 3:
	livre.afficherdetaildunlivre();
	break;
	case 4:
	emprunt.gestionempruntretour(user);
	break;
	case 5:
	emprunt.historiquedesemprunts(user);
	break;
	case 6:
	System.out.println("Donner l id du livre");
				int idLivre=scanner.nextInt();
	            reservation.faireReservation(connecter(),user,idLivre);
	break;
	case 7:
	System.out.println("Donner l id du livre");
				int iddLivre=scanner.nextInt();
	            reservation.annulerReservation(connecter(),user,iddLivre);
				break;
	case 0:
	System.exit(1);

}
			if(choix!=0){
		menuEtudiantEnseignant(user,livre,emprunt,etudiantenseignant,reservation);
	}
	}


	public void menuBibliothequaire(Bibliothequaire bibliothequaire){
		System.out.println("1: Notification par e-mail pour les rappels de retour");
		System.out.println("2: Generation des rapports statistiques");
		int choix=scanner.nextInt();
		switch (choix) {
			case 1:
			bibliothequaire.notification( connecter());
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
		Reservation reservation = new Reservation();

	
	try {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		user.connecter();
		Utilisateur utilisateur=user.authentifier();		
		if(utilisateur.getrole().equals("etudiant")|| utilisateur.getrole().equals("enseignant")){
			Etudiantenseignant etudenseignant=new Etudiantenseignant();
			biblio.menuEtudiantEnseignant(utilisateur,livre,emprunt,etudenseignant,reservation);
	
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