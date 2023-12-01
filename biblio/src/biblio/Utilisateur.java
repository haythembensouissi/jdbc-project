package biblio;

import java.sql.*; 
import java.util.*;
public class Utilisateur {
    private int id;
   private String nom;
   private String prenom;
   private String login;
   private String pwd;
   private String role;
   	static Scanner scanner=new Scanner(System.in);
public void setid(int id){
	this.id=id;
}
public void setnom(String nom){
	this.nom=nom;
}
public void setprenom(String prenom){
	this.prenom=prenom;
}
public void setlogin(String login){
	this.login=login;
}
public void setpwd(String pwd){
	this.pwd=pwd;
}
public void setrole(String role){
	this.role=role;
}
public int getid(){
return this.id;
}
public String getlogin(){
	return this.login;
}
public String getpwd(){
	return this.pwd;
}
public String getrole(){
	return this.role;
}
public String getnom(){
	return this.nom;
}
public String getprenom(){
	return this.prenom;
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
				user.setid(rs.getInt(1));;
				user.setnom(rs.getString("nom"));
			user.setlogin(rs.getString("login"));
			user.setprenom(rs.getString("prenom"));
			user.setpwd(rs.getString("pwd"));
			user.setrole(rs.getString("role"));
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
