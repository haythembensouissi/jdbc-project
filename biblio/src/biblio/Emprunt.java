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

}
