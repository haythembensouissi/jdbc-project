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
	public static Utilisateur obtenirauthentification(String login, String pwd,Connection connection){
		Utilisateur user=new Utilisateur();
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM utilisateur WHERE login = ? AND pwd = ?")) {
			ps.setString(1, login);
			ps.setString(2, pwd);
			try (ResultSet rs=ps.executeQuery()){
				if (rs.next()) {
				user.id=rs.getInt(1);
				user.nom=rs.getString("nom");
			user.login=rs.getString("login");
			user.prenom=rs.getString("prenom");
			user.pwd=rs.getString("pwd");
			user.role=rs.getString("role");
				}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return user;
	}

	static Scanner scanner=new Scanner(System.in);
	  public static boolean validate(String username, String password) {
        String query = "SELECT * FROM utilisateur WHERE login = ? AND pwd = ?";
        boolean isValid = false;
        try (Connection connection= connecter();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isValid = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

       return isValid;
    }
	public static  Utilisateur authentifier(){ 
		
		String login;
		String pwd;
System.out.println("entrer votre login");
Connection connection=connecter();
            login= scanner.nextLine();
            System.out.println("entrer votre mot de passe");
           pwd= scanner.nextLine();
		if(validate(login, pwd)){
			return obtenirauthentification(login,pwd,connection);
		}
		else{
			return authentifier();
		}
		
		
		
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
			livre.setAuteur(rs.getString("auteur"));
			livre.setGenre(rs.getString("genre"));
			
		}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("le livre n'existe pas");
		}

		return null;
	}
	public void menuEtudiantEnseignant(Utilisateur user){		
		int choix;
			System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");
		System.out.println("4:consulter l'historique des livres empruntes");	
		System.out.println("entrer votre choix");
		 choix=scanner.nextInt();
			switch(choix){
	
	case 1:
	ConsulterCatalogue(connecter());
	break;
	case 2:
	rechercherunlivre();
	break;
	case 3:
	System.out.println(" affiche des details");
	break;
	case 4:
	gestionempruntretour(user);
	break;
	case 0:
	System.exit(1);

}
			if(choix!=0){
		menuEtudiantEnseignant(user);
	}
	}


	public void menuBibliothequaire(){
		System.out.println("1: Notification par e-mail pour les rappels de retour");
		System.out.println("2: Generation des rapports statistiques");


	}
private static Livre[] ConsulterCatalogue(Connection connection) {
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
		System.out.println("retour");
		break;
	}
}
public static void main(String[]args) {
	Bibliotheque biblio= new Bibliotheque();
	Utilisateur user=new Utilisateur();
	try {
		 Class.forName("com.mysql.cj.jdbc.Driver");
		user=authentifier();
		System.out.println(user.role);
		if(user.role.equals("etudiant")|| user.role.equals("enseignant")){
			biblio.menuEtudiantEnseignant(user);
			
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