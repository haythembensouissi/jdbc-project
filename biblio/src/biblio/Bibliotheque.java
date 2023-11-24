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
	public String[] obtenirauthentification(String login, String pwd,Connection connection){
		String[] auth=new String[3];
		try (PreparedStatement ps = connection.prepareStatement("SELECT nom,prenom,role FROM utilisateur WHERE login = ? AND pwd = ?")) {
			ps.setString(1, login);
			ps.setString(2, pwd);
			try (ResultSet rs=ps.executeQuery()){
				if (rs.next()) {
					auth[0]=rs.getString("nom");
			auth[1]=rs.getString("prenom");
			auth[2]=rs.getString("role");
				}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		
		return auth;
	}

	Scanner scanner=new Scanner(System.in);
	  public boolean validate(String username, String password) {
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
	public String[] authentifier(){ 
		
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
	public void menu(){
		System.out.println("1:consulter le catalogue");
		System.out.println("2:Rechercher un livre");
		System.out.println("3:Afficher les details d'un livre");

	}
public static void main(String[]args) {
	
	Bibliotheque biblio= new Bibliotheque();
	
	String[] arr=new String[3];
	try {
		 Class.forName("com.mysql.jdbc.Driver");
		arr=biblio.authentifier();
		System.out.println(arr[0]);
		System.out.println(arr[1]);
		System.out.println(arr[2]);

		
		

		
		
	}
	catch(Exception e) {
		System.out.println(e);
	}
}
}