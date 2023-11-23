package biblio;

import java.sql.*; 
import java.util.*;
public class Utilisateur {
   public String role;
   boolean estauthentifie;
    Utilisateur(String role, boolean estauthentifie){

this.role=role;
this.estauthentifie=estauthentifie;

    }
}
