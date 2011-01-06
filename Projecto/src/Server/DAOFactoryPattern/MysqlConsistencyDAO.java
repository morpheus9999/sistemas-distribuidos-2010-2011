/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;
import Server.DAOFactoryPattern.ConsistencyDAO;
import BetPackage.IBetManager;
import BetPackage.IMatch;
import Client_Server.Constants;
import Client_Server.Message;
import java.sql.*;
import java.util.Vector;
/**
 *
 * @author jojo
 */
public class MysqlConsistencyDAO implements ConsistencyDAO{
    
    
    MysqlConsistencyDAO(){
        
    }
    
    public int rondaActualConsistency(){
        //System.out.println("RONDA ACTUAL ");
        Connection con = MysqlDAOFactory.createConnection();
        
        Statement stmt = null;
        try {
            

            stmt = con.createStatement();

            ResultSet m = stmt.executeQuery("SELECT idRonda from Ronda");
            int ronda;

            if (m.next()) {

                ronda = m.getInt("idRonda");

            } else {
                ronda = 0;
            }
            stmt.close();

            return ronda;

        } catch (SQLException e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }

    }
    public int tipoActualConsistency(){
        //System.out.println("TIPO ACTUAL ");
        Statement stmt = null;
        Connection con = MysqlDAOFactory.createConnection();
        try {
            
            stmt = con.createStatement();

            ResultSet m = stmt.executeQuery("SELECT Tipo from Ronda");
            int ronda;

            if (m.next()) {

                ronda = m.getInt("Tipo");

            } else {
                ronda = 0;
            }
            stmt.close();

            return ronda;

        } catch (SQLException e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }

    }
    public void actualizaConsistency(int idRonda,int tipo){
        Statement stmt = null;
        try {
            Connection con = MysqlDAOFactory.createConnection();
            stmt = con.createStatement();
            ResultSet k = stmt.executeQuery("SELECT CURRENT_TIMESTAMP");
            k.next();
            Timestamp data2 = k.getTimestamp(1);
            int min=data2.getMinutes();
            data2.setMinutes(min+1);
            //System.out.println(data2.toString().substring(0,data2.toString().length()-2 ));
            String p=data2.toString().substring(0,data2.toString().length()-2 );
            boolean m = stmt.execute("UPDATE  `mydb`.`Ronda` SET  `idRonda` =  '"+idRonda+"',`Data` =  '"+p+"',`Tipo` =  '"+tipo+"' WHERE  `Ronda`.`id` =1 ;");
            stmt.close();

        } catch (SQLException e) {
                    e.printStackTrace();
            
        }

    }
    public long esperaConsistency(){
        Statement stmt = null;
        try {
            Connection con = MysqlDAOFactory.createConnection();
            stmt = con.createStatement();
            ResultSet k = stmt.executeQuery("SELECT CURRENT_TIMESTAMP");
            k.next();
            Timestamp data1 = k.getTimestamp(1);
            
            
            ResultSet l = stmt.executeQuery("SELECT Data from Ronda where id=1");

            l.next();
            Timestamp data2 = l.getTimestamp(1);

            long espera= data2.getTime()-data1.getTime();

            //ResultSet m = stmt.executeQuery("UPDATE  `mydb`.`Ronda` SET  `idRonda` =  '"+idRonda+"',`Data` =  '"+data2.toString()+"',`Tipo` =  '"+tipo+"' WHERE  `Ronda`.`id` =1 ;");
            stmt.close();
            return espera;
        } catch (SQLException e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }
        
    }
    
    
    public boolean NewRoundConsistency(IBetManager man, int ronda) {

        //User lg = (User)generic.getObj();


        /*System.out.println("recebido");
        System.out.println("name: "+ lg.getName());
        System.out.println("pass: "+ lg.getPassword());
        System.out.println("pass: "+ lg.getMail());
        System.out.println("bet: "+ lg.getCredit());
         *
         * */
        Connection con = MysqlDAOFactory.createConnection();
        
            try {
                
                Statement stmt = con.createStatement();


                for (IMatch m : man.getMatches()) {
                    int result=0;
                        switch (man.getResult(m)) {
                            case HOME:
                                result=1;
                                break;
                            case AWAY:
                                result=2;
                                break;
                            default:
                                result=0;
                                break;
                        }
                    //INSERT INTO `mydb`.`Jogo` (`idJogo`, `Resultado`, `Casa`, `Fora`, `Ronda`) VALUES (NULL, NULL, 'sdaf', 'sdfa', '1');
                    boolean flag = stmt.execute("INSERT INTO `mydb`.`Jogo` (`idJogo`, `Resultado`, `Casa`, `Fora`, `Ronda`) VALUES (NULL,"+ result+", '" + m.getHomeTeam() + "','" + m.getAwayTeam() + "'," + ronda + ")");
                }
                stmt.close();
                return true;
            } catch (SQLException e) {

                System.out.println("SQL Exception: " + e.toString());
                return false;
            
        }

    
    
    }
    
    
    
    public Vector<Message> updateBetsConsistency(int ronda) {
        //Bet bet = (Bet) generic.getObj();

        Connection con = MysqlDAOFactory.createConnection();
        //System.out.println("recebido : updateBets-->");
        //SELECT Cliente_Nome, bet
        //FROM Aposta, Jogo
        //WHERE Jogo_idJogo = idJogo
        //AND ronda =1
        Vector<Message> m = new Vector();


        
            try {
                
                Statement stmt = con.createStatement();
                String Nome;
                int bet;
                int idJogo;
                ResultSet rr = stmt.executeQuery("SELECT Cliente_Nome, bet,idJogo FROM Aposta, Jogo WHERE Jogo_idJogo = idJogo AND ronda =" + ronda + " AND Aposta_equipa =Resultado");
                String game = "";
                while (rr.next()) {
                    //System.out.println("ENTRA 1aaaaaaaaaa");
                    Nome = rr.getString("Cliente_Nome");
                    idJogo=rr.getInt("idJogo");
                    bet = rr.getInt("bet");
                    //System.out.println("Vai fazer update da aposta do " + Nome + " " + bet);
                    stmt = con.createStatement();
                    ResultSet mm = stmt.executeQuery("SELECT Credito from Cliente WHERE Nome='" + Nome + "'");
                    mm.next();

                    int credito_antigo = mm.getInt("Credito");
                    

                    //tratar de sacar kal é o jogo
                    stmt.close();
                    stmt = con.createStatement();
                    ResultSet rs;
                    
                    rs = stmt.executeQuery("SELECT idJogo, Casa, Fora FROM Jogo WHERE Ronda='" + ronda + "' and idJogo='"+idJogo+"'");
                    rs.next();
                    game = rs.getString("Casa") +" VS "+ rs.getString("Fora");



                    m.addElement(new Message(Nome, "Ganhou apostou no jogo " + game + " com (" + bet + ") creditos, vai ganhar (" + (bet * Constants.reward) + ") Credito actual (" + (credito_antigo + (bet * Constants.reward)) + ")"));
                    stmt.close();
                    stmt = con.createStatement();
                    stmt.execute("UPDATE  `mydb`.`Cliente` SET  `Credito` = '" + (credito_antigo + (bet * Constants.reward)) + "' WHERE `Cliente`.`Nome` = '" + Nome + "'");

                }

                ResultSet rperdeu = stmt.executeQuery("SELECT Cliente_Nome, bet, idJogo FROM Aposta, Jogo WHERE Jogo_idJogo = idJogo AND ronda =" + ronda + " AND Aposta_equipa !=Resultado");

                while (rperdeu.next()) {
                    //System.out.println("ENTRA 2aaaaaaaaaa");
                    Nome = rperdeu.getString("Cliente_Nome");
                    bet = rperdeu.getInt("bet");
                    idJogo=rperdeu.getInt("idJogo");
                    stmt = con.createStatement();
                    ResultSet mp = stmt.executeQuery("SELECT Credito from Cliente WHERE Nome='" + Nome + "'");
                    mp.next();
                    int credito_antigo = mp.getInt("Credito");
                    game = "";

                    //tratar de sacar kal é o jogo
                    stmt.close();
                    stmt = con.createStatement();
                    ResultSet rc;
                    rc = stmt.executeQuery("SELECT idJogo, Casa, Fora FROM Jogo WHERE Ronda='" + ronda + "' and idJogo='"+idJogo+"'");
                    rc.next();
                    game = rc.getString("Casa") +" VS "+ rc.getString("Fora");
                    m.addElement(new Message(Nome, "Perdeu apostou no jogo " + game + " com (" + bet + ") creditos, vai ficar com (" + (credito_antigo ) + ") Creditos"));
                    stmt.close();

                }


                stmt.close();
                return m;

            } catch (SQLException e) {

                e.printStackTrace(System.out);
                System.out.println("SQL Exception (1): " + e.toString());
                return null;
            


        }
    
    }
}
