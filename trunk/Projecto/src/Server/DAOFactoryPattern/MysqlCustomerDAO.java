/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAOFactoryPattern;

/**
 *
 * @author jojo
 */
// CloudscapeCustomerDAO implementation of the 
// CustomerDAO interface. This class can contain all
// Cloudscape specific code and SQL statements. 
// The client is thus shielded from knowing 
// these implementation details.
import Server.DAOFactoryPattern.CustomerDAO;
import Client_Server.Bet;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.ViewMatch;
import java.sql.*;
import java.util.Vector;

public class MysqlCustomerDAO implements CustomerDAO {

    public MysqlCustomerDAO() {
        // initialization 
    }

    public boolean newBetCustomer(Generic generic, Login lg, int ronda) {


        Bet bet = (Bet) generic.getObj();


        //System.out.println("recebido");
        //System.out.println("name: " + lg.getName());
        //System.out.println("bet: "+ lg.getBetGame());
        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());


        Connection con = MysqlDAOFactory.createConnection();
        try {


            Statement stmt = con.createStatement();
            ResultSet credit = stmt.executeQuery("SELECT Credito from Cliente where Nome='" + lg.getName() + "'");

            credit.next();
            int credito = credit.getInt("Credito");
            if (credito < bet.getBet()) {
                System.out.println("NAO TEM CREDITO Nome=" + lg.getName());
                stmt.close();
                return false;
            }



            ResultSet m = stmt.executeQuery("SELECT idJogo from Jogo WHERE ronda=" + ronda);
            while (m.next()) {
                if (m.getInt("idJogo") == bet.getIdGame()) {
                    boolean flag = stmt.execute("INSERT INTO  `mydb`.`Aposta` (`idAposta` ,`bet` ,`Aposta_equipa` ,`Jogo_idJogo` ,`Cliente_Nome`)VALUES (NULL ,'" + bet.getBet() + "','" + bet.getAposta() + "','" + bet.getIdGame() + "','" + lg.getName() + "'" + ")");
                    if (!flag) {
                        boolean rs = stmt.execute("UPDATE  `mydb`.`Cliente` SET  `Credito` = '" + (credito - bet.getBet()) + "' WHERE `Cliente`.`Nome`= '" + lg.getName() + "'");
                        stmt.close();
                        return true;
                    } else {
                    }

                }
            }



            stmt.close();
            return false;
        } catch (SQLException e) {

            System.out.println("SQL Exception (new Bet): " + e.toString());
            return false;
        }




    }

    public Generic viewMatchesCustomer(Generic temp, int ronda) {
        Statement stmt = null;
        Connection con = MysqlDAOFactory.createConnection();

        try {

            stmt = con.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery("SELECT idJogo, Casa, Fora FROM Jogo WHERE Ronda='" + ronda + "'");
            Vector<ViewMatch> m = new Vector<ViewMatch>();
            while (rs.next()) {
                m.addElement(new ViewMatch(rs.getInt("idJogo"), rs.getString("Casa"), rs.getString("Fora")));


            }
            temp.setObj(m);
            temp.setConfirmation(true);
            stmt.close();
            return temp;
        } catch (SQLException e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return null;
        }

    }
}