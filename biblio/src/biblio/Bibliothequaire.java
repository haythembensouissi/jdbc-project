package biblio;

import java.sql.*;

public class Bibliothequaire extends Utilisateur {
    Bibliothequaire(){
        super();
    }
    public Rapport genererdesrapports(){
        Connection connection=connecter();
        Rapport rapport=new Rapport();
        try (PreparedStatement ps =connection.prepareStatement("SELECT id_livre,COUNT(*) as rowcount FROM emprunt group by id_livre order by rowcount ")) {
            ResultSet rs=ps.executeQuery();
            int id=0;
           if(rs.next()){

            id=rs.getInt("id_livre");
           
  
           }
           try (PreparedStatement ps1=connection.prepareStatement("SELECT titre from livre where id_livre=?")) {
            ps1.setInt(1, id);
            ResultSet rs1= ps1.executeQuery();
            if(rs1.next()){
                rapport.setnomlivre(rs1.getString("titre"));
                System.out.println("le livre le plus emprunté est "+rapport.getnomlivre());
            }
           } catch (Exception e) {
            // TODO: handle exception
           }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        try (PreparedStatement ps=connection.prepareStatement("SELECT id_utilisateur,count(*) as occurences from emprunt group by id_utilisateur order by occurences ")) {
            ResultSet rs=ps.executeQuery();
            int id=0;
            if(rs.next()){
                id=rs.getInt("id_utilisateur");
            }
            try (PreparedStatement ps1=connection.prepareStatement("SELECT nom from utilisateur where id_utilisateur= ?")) {
                ps1.setInt(1, id);
                ResultSet rs1=ps1.executeQuery();
                if(rs1.next()){
                    rapport.setnomutilisateur(rs1.getString("nom"));
                    System.out.println("l'utilisateur qui a plus emprunté des livres est "+rapport.getnomutilisateur());
                }
                
            } catch (Exception e) {
                // TODO: handle exception
            }
            
        } catch (Exception e) {
            // TODO: handle exception
        }
        return rapport;
    }
     public static void notification(Connection connection) {
        System.out.println("entrer l'id de l'utilisateur");
        int id=scanner.nextInt();
    	String qr = "SELECT * FROM Emprunt WHERE id_utilisateur = ? AND status = 'en cours' AND date_retour < NOW()";
    	try(PreparedStatement pr=connection.prepareStatement(qr)){
    		pr.setInt(1,id);
    		try (PreparedStatement ps1=connection.prepareStatement("SELECT * from utilisateur where id_utilisateur=?")) {
                ps1.setInt(1, id);
                ResultSet rs1=ps1.executeQuery();
                Utilisateur user=new Utilisateur();
                if(rs1.next()){
                    user.setid(rs1.getInt(1));
                user.setlogin(rs1.getString("login"));
                user.setnom(rs1.getString("nom"));
                ResultSet rs = pr.executeQuery();
    			while(rs.next()) {
    				System.out.println("Rappellllee pour l'email : "+user.getlogin()+" le livre emprunté doit etre retourné");
                }

                }
                            } catch (Exception e) {
                // TODO: handle exception
            }
    		
    			} catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
    		}
    		
    		
    	
    }


