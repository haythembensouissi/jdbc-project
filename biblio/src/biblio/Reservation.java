package biblio;

import java.sql.*;
import java.util.List;
import java.time.LocalDate;

public class Reservation {
    private int idReservation;
    private int idUtilisateur;
    private int idLivre;
    private LocalDate dateReservation;
    private String statut;

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public int getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(int idLivre) {
        this.idLivre = idLivre;
    }

    public LocalDate getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDate lc) {
        this.dateReservation = lc;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    //public void reserver(int id) {}
    public void faireReservation(Connection connection,Utilisateur user,int id) {
    	String statutEm = statutEmprunt(connection,user,id);
    	if(statutEm.equals("en cours")) {
    		//this.Reservation(0,user.getid(),LocalDate.now(),"comfirmé");
    		this.setIdUtilisateur(user.getid());
    		this.setIdLivre(id);
    		this.setDateReservation(LocalDate.now());
    		this.setStatut("confirmée");
    		
	        String qr = "INSERT INTO Reservation (id_utilisateur, id_livre, date_reservation,statut) VALUES (?, ?, ?, ?)";
	        try (PreparedStatement pr = connection.prepareStatement(qr, Statement.RETURN_GENERATED_KEYS)) {
	        	
	        	pr.setInt(1, this.idUtilisateur);
	        	pr.setInt(2, this.idLivre);
	        	pr.setObject(3, this.dateReservation);
	        	pr.setString(4,this.getStatut());
	
	        	pr.executeUpdate();
	            ResultSet generatedKeys = pr.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                this.idReservation = generatedKeys.getInt(1);
	                System.out.println("Réservation effectuée avec succès. ID de la réservation : " + this.idReservation);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
        }}
    }

    public String statutEmprunt(Connection connection,Utilisateur user,int id) {
    	String st="Non trouver";
    	String qr="SELECT status FROM emprunt WHERE id_utilisateur = ? AND id_livre = ? and status='en cours'";
    	try(PreparedStatement pr = connection.prepareStatement(qr)){
    		pr.setInt(1,user.getid());
            pr.setInt(2,id);
            try (ResultSet rs = pr.executeQuery()) {
                if (rs.next()) {
                    st = rs.getString("status");
                }
            }
    	}
    	catch (SQLException e) {
            e.printStackTrace();
        }
		return st;
	}
    public String statutRes(Connection connection, Utilisateur user,int id) {
    	String st="";
    	String qr="SELECT statut FROM reservation WHERE id_utilisateur = ? AND id_livre = ? and statut='confirmée'";
    	try(PreparedStatement pr = connection.prepareStatement(qr)){
    		pr.setInt(1,user.getid());
    		pr.setInt(2, id);
    		try(ResultSet rs = pr.executeQuery()){
    			if(rs.next()) {
    				st = rs.getString("statut");
    			}
    		}
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}
    	return st;
    	
    	
    }

	public void annulerReservation(Connection connection,Utilisateur user,int id) {
        if ("confirmée".equals(this.statutRes(connection,user,id))) {
            String qr = "UPDATE  Reservation SET statut='Annulée' WHERE id_livre = ? AND statut='confirmée' AND id_utilisateur=? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(qr)) {
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2,user.getid());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Réservation annulée avec succès.");
                } else {
                    System.out.println("La réservation n'a pas pu être annulée.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Impossible d'annuler une réservation qui n'est pas deja confirmée.");
        }
    }
}
