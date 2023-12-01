package biblio;

import java.time.LocalDate;
import java.util.*;
import java.sql.*;
import java.sql.Date;

public class Emprunt {
       	static Scanner scanner=new Scanner(System.in);

    private int id_emprunt;
	private int id_utilisateur;
    private int id_livre;
    private String status;
   private Date date_emprunt;
    private Date date_retour;
	public void setidemprunt(int id_emprunt){
		this.id_emprunt=id_emprunt;
	}
	public void setidutilisateur(int id_utilisateur){
		this.id_utilisateur=id_utilisateur;
	}
	public void setidlivre(int id_livre){
		this.id_livre=id_livre;
	}
	public void setstatus(String status){
		this.status=status;
	}
	public void setdateemprunt(Date date){
		this.date_emprunt= date;
	}
public void setdateretour(Date date){
		this.date_retour= date;
	}
	public int getidemprunt(){
		return this.id_emprunt;
	}
	public int getidutilisateur(){
		return this.id_utilisateur;
	}
	public int getidlivre(){
		return this.id_livre;
	}
	public String getsatus(){
		return this.status;
	}
	public Date getdateemprunt(){
		return this.date_emprunt;
	}
	public Date getdateretour(){
		return this.date_retour;
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
public void emprunter(Utilisateur user){
	Connection connection=connecter();
    boolean encours=false;
System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
try (PreparedStatement ps=connection.prepareStatement("SELECT * from livre where id_livre=?")) {
    ps.setInt(1, id);
    ResultSet rs=ps.executeQuery();
    if(!rs.next()||rs.getString("disponibilité").equals("non disponible")){
        System.out.println("le livre n'existe pas");
    }
    else{
        try (PreparedStatement ps3=connection.prepareStatement("SELECT * from emprunt where id_livre= ?")) {
            ps3.setInt(1, id);
            ResultSet rs3=ps3.executeQuery();
            while(rs3.next()){
                if(rs3.getString("status").equals("en cours")){
                    System.out.println("le livre est en cours d'emprunt");
                    encours=true;
                }
                
                
            }
         

            

        } catch (Exception e) {
            // TODO: handle exception
        }
        if(encours==false){
               try (PreparedStatement ps4 = connection.prepareStatement("INSERT INTO emprunt (date_emprunt,date_retour,status,id_utilisateur,id_livre) values (?,?,?,?,?) ")) {
		ps4.setObject(1, LocalDate.now());
		ps4.setObject(2, LocalDate.now().plusMonths(1));
		ps4.setString(3, "en cours");
		ps4.setInt(4, user.getid());
		ps4.setInt(5, id);
		ps4.executeUpdate();

	} catch (SQLException e) {
		
		System.out.println(e);
	}
        }
     
        
    }
} catch (Exception e) {
    // TODO: handle exception
}


}
public void retourner(Utilisateur user){
	Connection connection=connecter();
	System.out.println("entrer l'id de livre");
int id=scanner.nextInt();
boolean terminé=false;
try (PreparedStatement ps=connection.prepareStatement("SELECT * FROM emprunt WHERE  id_livre=? and status='en cours'")) {
	ps.setInt(1, id);
	ResultSet rs=ps.executeQuery();
    
if(!rs.next()||rs.getString("status").equals("terminé")){
    System.out.println(rs.getInt(1));
		System.out.println("le livre n'est pas emprunté");
	}

	
	else{
			int idemprunt=rs.getInt(1);
            System.out.println(idemprunt);
			try (PreparedStatement ps1=connection.prepareStatement("UPDATE emprunt SET status='terminé' WHERE id_emprunt= ?")) {
			
			ps1.setInt(1, idemprunt);
			ps1.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
} catch (SQLException e) {
	System.out.println("pas de livre emprunté");
}
}
public Emprunt[] historiquedesemprunts(Utilisateur user){
	Emprunt[] res=new Emprunt[100];
	Connection connection=connecter();
	try (PreparedStatement ps=connection.prepareStatement("SELECT * from emprunt where id_utilisateur=?")) {
		ps.setInt(1, user.getid());
		int i=0;
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			Emprunt emprunt=new Emprunt();
			emprunt.setidemprunt(rs.getInt("id_emprunt"));
			emprunt.setidlivre(rs.getInt("id_livre"));
			emprunt.setidutilisateur(rs.getInt("id_utilisateur"));;
			emprunt.setstatus(rs.getString("status"));
			emprunt.setdateemprunt(rs.getDate("date_emprunt"));
			emprunt.setdateretour(rs.getDate("date_retour"));
			res[i]=emprunt;
			i++;
		}
		for(int j=0;j<i;j++){
			System.out.println("l'id de l'emprunte: "+res[j].id_emprunt);
			System.out.println("l'id de livre "+res[j].id_livre);
			System.out.println("l'id d'utilisateur "+res[j].id_utilisateur);
			System.out.println("le status: "+res[j].status);
			System.out.println("emprunté le : "+res[j].date_emprunt);
			System.out.println("retourné le : "+res[j].date_retour);
		}
	} catch (SQLException e) {
		System.out.println("pas de livres ");
	}
	return res;
}
}
