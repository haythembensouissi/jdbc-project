package biblio;
import java.sql.*;
public class Exemple {
public static void main(String[]args) {
	try {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con =DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque","root","root");
		Statement stat=con.createStatement();
		System.out.println("connected to the database");
		String query="INSERT into utilisateur values (2,\"haythem\",\"bensouissi\",\"haythembns\",\"haythem159357\",\"etudiant\")";
		
		stat.executeUpdate(query);
		query="Select * from utilisateur ";
		ResultSet rs=stat.executeQuery(query);
		   while(rs.next()){
	            //Display values
	            System.out.print("ID: " + rs.getInt("id_utilisateur"));
	            System.out.print(", Age: " + rs.getString("nom"));
	            System.out.print(", First: " + rs.getString("prenom"));
	            System.out.println(", login: " + rs.getString("login"));
	            System.out.println(", pwd: " + rs.getString("pwd"));
	            System.out.println(", role: " + rs.getString("role"));
	         }
		query="UPDATE utilisateur SET nom='mohamed' where id_utilisateur=1 ";
		stat.executeUpdate(query);
	}
	catch(Exception e) {
		System.out.println(e);
	}
}
}