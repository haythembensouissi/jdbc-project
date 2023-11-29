package biblio;

import java.sql.*; 
import java.util.*;
public class Utilisateur {
    public int id;
   public String nom,prenom,login,pwd,role;
   	static Scanner scanner=new Scanner(System.in);

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
}
