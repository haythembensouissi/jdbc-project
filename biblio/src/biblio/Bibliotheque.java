package biblio;
import java.sql.*;
import java.util.*;
public class Bibliotheque {
	public Connection connecter(){
		
			Connection connection;
			connection=null;
			try {
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","root");
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			return connection;
		
	}
	Scanner scanner=new Scanner(System.in);
	  public boolean validate(String username, String password,String role) {
        String query = "SELECT * FROM utilisateur WHERE login = ? AND pwd = ?";
		
        boolean isValid = false;

        try (Connection connection= connecter();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                  role=resultSet.getString("role");
                    isValid = true;
					System.out.println("welcome mr le "+role);
					
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

       return isValid;
    }
	public boolean authentifier(String role){ 
		
		System.out.println("entrer votre login");
           String login= scanner.nextLine();
            System.out.println("entrer votre mot de passe");
           String pwd= scanner.nextLine();
           System.out.println(role);
		while(!validate(login, pwd,role)){
			System.out.println("entrer votre login");
            login= scanner.nextLine();
            System.out.println("entrer votre mot de passe");
           pwd= scanner.nextLine();
          

		}
		return true;
		
	}
	public void menu(){
		System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");

	}
public static void main(String[]args) {
	Scanner scanner=new Scanner(System.in);
	Bibliotheque biblio= new Bibliotheque();
	String role="";
	try {
		 Class.forName("com.mysql.jdbc.Driver");
		boolean estAuthentifie=biblio.authentifier(role);
		
		
if(estAuthentifie){
	
	
	if (role=="etudiant"){
		biblio.menu();
	}
}
		
		
	}
	catch(Exception e) {
		System.out.println(e);
	}
}
}