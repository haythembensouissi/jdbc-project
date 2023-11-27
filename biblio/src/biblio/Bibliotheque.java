package biblio;
import java.sql.*;
import java.util.*;

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
	public boolean rechercherunlivre(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("entrer l'id");
		int id=scanner.nextInt();
		Connection connection=connecter();
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM LIVRE WHERE id_livre=?")) {
			ps.setInt(1, id);
		ResultSet rs=ps.executeQuery();
		if(rs.next()){
			System.out.println("le livre existe");
			return true;
			
		}
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("le livre n'existe pas");
		}

		return false;
	}
	public void menuEtudiantEnseignant(){		
		int choix;
			System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");
		System.out.println("4:consulter l'historique des livres empruntes");	
		System.out.println("entrer votre choix");
		 choix=scanner.nextInt();
			switch(choix){
	
	case 1:
	System.out.println("consultation du catalogue");
	break;
	case 2:
	rechercherunlivre();
	break;
	case 3:
	System.out.println(" affiche des details");
	break;
	case 4:
	System.out.println("historique des emprunts");
	break;
	case 0:
	System.exit(1);

}
			if(choix!=0){
		menuEtudiantEnseignant();
	}
	}

	
	public void menuBibliothequaire(){
		System.out.println("1: Notification par e-mail pour les rappels de retour");
		System.out.println("2: Generation des rapports statistiques");


	}
public void afficherdetails(){
		Scanner scanner=new Scanner(System.in);
		System.out.println("entrer l'id");
		int id=scanner.nextInt();
		Connection connection=connecter();
		try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM LIVRE WHERE id_livre=?")) {
			ps.setInt(1, id);
		ResultSet rs=ps.executeQuery();
		if(rs.next()){
			System.out.println("titre : "+rs.getString("titre"));
			System.out.println("auteur : "+rs.getString("auteur"));
			System.out.println("genre : "+rs.getString("genre"));
			System.out.println("disponibilité : "+rs.getString("disponibilité"));
			
		}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		
	}
public void historiquedesemprunts(){
	Connection connection=connecter();
	try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM emprunt")) {
		ResultSet rs=ps.executeQuery();
		
				while (rs.next()) {
					try (PreparedStatement ps2=connection.prepareStatement("SELECT * from utilisateur WHERE id_utilisateur= ?")) {
			ps2.setInt(1,rs.getInt(5)); 
				ResultSet rs2=ps2.executeQuery();
				if(rs2.next()){
					System.out.println("id emprunt :"+rs.getInt(1));
			System.out.println("date emprunt :"+rs.getDate(2));
			System.out.println("date retour :"+rs.getDate(3));
			System.out.println("status:"+rs.getString(4));
			System.out.println("id utilisateur :"+rs.getInt(5));
			System.out.println("id livre :"+rs.getInt(6));
			System.out.println("emprunte par :"+rs2.getString("nom"));
				}
			
		
		}

		} 
		
	} catch (SQLException e) {
		
		e.printStackTrace();
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
			biblio.menuEtudiantEnseignant();
			
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