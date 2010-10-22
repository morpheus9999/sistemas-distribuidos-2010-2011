/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import BetPackage.IBetManager;
import BetPackage.IMatch;
import Client_Server.User;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Credit;
import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Message;
import Client_Server.ViewMatch;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Queries {

    static Generic resetCredit(Generic temp, Login lg) {
        //System.out.println("reset login:"+lg.getName());
        Statement stmt = null;
        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                        + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                stmt = con.createStatement();
                
                boolean rs = stmt.execute("UPDATE  `mydb`.`Cliente` SET  `Credito` = '" + Constants.resetCredito + "' WHERE `Cliente`.`Nome`= '" + lg.getName() + "'");
                Credit m = (Credit) temp.getObj();
                m.setCredit(Constants.resetCredito);
                temp.setObj(m);
                temp.setConfirmation(true);
                stmt.close();
                return temp;
            } catch (Exception e) {
                System.out.println("Erro iniciar queries:" + e.toString());

            }

        }
    }
    static int rondaActual(){
        //System.out.println("RONDA ACTUAL ");
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                    + "user=root&password=root";
            Connection con = DriverManager.getConnection(connectionUrl);
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

        } catch (Exception e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }

    }
    static int tipoActual(){
        //System.out.println("TIPO ACTUAL ");
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                    + "user=root&password=root";
            Connection con = DriverManager.getConnection(connectionUrl);
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

        } catch (Exception e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }

    }
    static void actualiza(int idRonda,int tipo){
         //System.out.println("ACTUALIZA ");
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                    + "user=root&password=root";
            Connection con = DriverManager.getConnection(connectionUrl);
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

        } catch (Exception e) {
                    e.printStackTrace();
            
        }

    }
    static long espera(){
        //System.out.println("ESPERA ");
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                    + "user=root&password=root";
            Connection con = DriverManager.getConnection(connectionUrl);
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
        } catch (Exception e) {
            System.out.println("Erro iniciar queries:" + e.toString());
            return 0;
        }
        
    }


    static Generic viewMatches(Generic temp, int ronda) {
        Statement stmt = null;
        
        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                        + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                stmt = con.createStatement();
                ResultSet rs;
                rs = stmt.executeQuery("SELECT idJogo, Casa, Fora FROM Jogo WHERE Ronda='" + ronda + "'");
                Vector<ViewMatch> m= new Vector<ViewMatch>();
                while(rs.next()){
                    m.addElement(new ViewMatch(rs.getInt("idJogo"), rs.getString("Casa"), rs.getString("Fora")));
                     
                    
                }
                temp.setObj(m);
                temp.setConfirmation(true);
                stmt.close();
                return temp;
            } catch (Exception e) {
                System.out.println("Erro iniciar queries:" + e.toString());

            }

        }
    }

    private Connection connection;
    private Statement statement;

    public Queries() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root");
        } catch (Exception e) {
            System.out.println("Erro iniciar queries:" + e.toString());

        }
    }

    static boolean login(Generic generic) {
        Login lg = (Login) generic.getObj();

        /*  aqui vao as queries */
//        System.out.println("recebido");
//        System.out.println("name: " + lg.getName());
//        System.out.println("pass: " + lg.getPassword());
        Statement stmt=null;
        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?"
                        + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                stmt = con.createStatement();
                ResultSet rs;

                int rowCount = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(Nome) FROM Cliente WHERE Nome='" + lg.getName() + "' and Password= '" + lg.getPassword() + "';");
                rs.next();
                rowCount = rs.getInt(1);

                if (rowCount == 1) {

                    //e falta adicionar a lista de clientes logados
                    stmt.close();
                    return true;


                } else {
                    stmt.close();
                    return false;
                }
            } catch (SQLException e) {

                System.out.println("SQL Exception: " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                
                return false;

            } finally {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        }

    }

    static boolean register(Generic generic) {
        User lg = (User) generic.getObj();

//        System.out.println("Regista:");
//        System.out.println("name: " + lg.getName());
//        System.out.println("pass: " + lg.getPassword());
//        System.out.println("pass: " + lg.getMail());
//        System.out.println("bet: " + lg.getCredit());

        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                // INSERT INTO `mydb`.`Cliente` (`Nome`, `Password`, `Mail`, `Credito`) VALUES ('Jorge', '123', 'morpheus@gmail.com', '70');

                boolean flag = stmt.execute("INSERT INTO Cliente VALUES ('" + lg.getName() + "','" + lg.getPassword() + "','" + lg.getMail() + "'," + lg.getCredit() + ")");
                if (!flag) {
                    stmt.close();
                    return true;
                }else{
                    
                stmt.close();
                return false;
                }
            }catch (MySQLIntegrityConstraintViolationException e) {
                return false;
                //System.out.println("SQL Exception: " + e.toString());

            }catch (SQLException e) {
                
                System.out.println("SQL Exception: " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return false;
            }


        }

    }

    static boolean newBet(Generic generic, Login lg,int ronda) {

         
        Bet bet = (Bet) generic.getObj();
        

        //System.out.println("recebido");
        //System.out.println("name: " + lg.getName());
        //System.out.println("bet: "+ lg.getBetGame());
        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());


        while (true) {
            try {
                
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet credit =stmt.executeQuery("SELECT Credito from Cliente where Nome='"+lg.getName()+"'");
                
                credit.next();
                int credito=credit.getInt("Credito");
                    if(credito<bet.getBet()){
                        System.out.println("NAO TEM CREDITO Nome="+lg.getName());
                        stmt.close();
                        return false;
                        }
                


                ResultSet m=stmt.executeQuery("SELECT idJogo from Jogo WHERE ronda="+ronda);
                while(m.next()){
                    if(m.getInt("idJogo")==bet.getIdGame()){
                        //INSERT INTO  `mydb`.`Aposta` (`idAposta` ,`bet` ,`Aposta_equipa` ,`Jogo_idJogo` ,`Cliente_Nome`)VALUES (NULL ,  '2',  '2',  '18',  'Jorge');
                        boolean flag = stmt.execute("INSERT INTO  `mydb`.`Aposta` (`idAposta` ,`bet` ,`Aposta_equipa` ,`Jogo_idJogo` ,`Cliente_Nome`)VALUES (NULL ,'"+ bet.getBet() + "','" + bet.getAposta() + "','" + bet.getIdGame()+"','" +lg.getName()+"'"+ ")");
                        if (!flag) {
                            //UPDATE  `mydb`.`Cliente` SET  `Credito` =
                            boolean rs= stmt.execute("UPDATE  `mydb`.`Cliente` SET  `Credito` = '"+ (credito-bet.getBet()) +"' WHERE `Cliente`.`Nome`= '"+lg.getName()+"'");
                            stmt.close();
                            return true;
                        }else{
                        
                        }
                
                        }
                }

                
                
                stmt.close();
                return false;
            } catch (SQLException e) {

                System.out.println("SQL Exception (new Bet): " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return false;
            }


        }
    }

    static boolean NewRound(IBetManager man, int ronda) {

        //User lg = (User)generic.getObj();


        /*System.out.println("recebido");
        System.out.println("name: "+ lg.getName());
        System.out.println("pass: "+ lg.getPassword());
        System.out.println("pass: "+ lg.getMail());
        System.out.println("bet: "+ lg.getCredit());
         *
         * */

        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
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

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return false;
            }
        }

    }

    static Generic getCredit(Generic gen, Login lg) {
         // nao deverá ser user
        Credit cr = (Credit) gen.getObj();
        
       // System.out.println("recebido GETCREDITO");
       // System.out.println("name: " + lg.getName());
        

        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs1;
                    rs1 = stmt.executeQuery("SELECT Credito FROM Cliente WHERE Nome = '" + lg.getName()+"'" );
                    rs1.next();
                    int bet = rs1.getInt("Credito");
                    //System.out.println(" credito:"+bet);
                    cr.setCredit(bet);
                    gen.setConfirmation(true);
                    gen.setObj(cr);
                    stmt.close();
                    return gen;
            } catch (SQLException e) {

                System.out.println("SQL Exception: " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                gen.setConfirmation(false);
                gen.setObj(cr);
                return gen;
                
            }


        }
    }

    static Vector<String> getUsers() {
        // nao deverá ser user

        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());
        Vector <String> m= new Vector<String>();
        String nome;
        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs1;
                rs1 = stmt.executeQuery("SELECT Nome FROM Cliente");
                while (rs1.next()) {
                    nome = rs1.getString("Nome");
                    m.addElement(nome);
                }
                stmt.close();
                return m;
            } catch (SQLException e) {

                System.out.println("SQL Exception: " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return null;


            }


        }
    }

    static Message getMensagens(String nome) {
        // nao deverá ser user

        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());
        Message k;
        int id;
        
        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs1;
                rs1 = stmt.executeQuery("SELECT id,Mensagens,DE FROM Mensagens where Cliente_Nome='"+nome+"'");
                if(rs1.next()){
                    String mensagens=rs1.getString("Mensagens");
                    String de=rs1.getString("DE");
                    id= rs1.getInt("id");
                    k= new Message(de, mensagens, nome);
                    stmt.execute("DELETE FROM `mydb`.`Mensagens` WHERE `Mensagens`.`id` = '"+id+"'");



                }else{
                    k=null;
                }
                    //m.addElement();
                
                stmt.close();
                return k;
            } catch (SQLException e) {
                e.printStackTrace(System.out);
                System.out.println("SQL Exception (getMensagem): " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return null;


            }


        }
    }

    static boolean setMensagens(String de, String para ,String mensagem) {
        // nao deverá ser user

        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());\
        //System.out.println("NOME SET MSG:"+para);
        Message k;


        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                
                boolean m = stmt.execute("INSERT INTO `mydb`.`Mensagens` (`id`, `Cliente_Nome`, `Mensagens`, `DE`) VALUES (NULL, '"+para+"', '"+mensagem+"', '"+de+"');");
                

                
                    //m.addElement();

                stmt.close();
                return m;
            } catch (MySQLIntegrityConstraintViolationException e) {
                return false;
                //System.out.println("SQL Exception (setMensagens): " + e.toString());

            }catch (SQLException e) {
                e.printStackTrace(System.out);
                System.out.println("SQL Exception (setMensagens): " + e.toString());

            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return false;


            }


        }
    }

    static Vector<Message> updateBets(int ronda) {


        //Bet bet = (Bet) generic.getObj();


        //System.out.println("recebido : updateBets-->");
        //SELECT Cliente_Nome, bet
        //FROM Aposta, Jogo
        //WHERE Jogo_idJogo = idJogo
        //AND ronda =1
        Vector<Message> m = new Vector();


        while (true) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost:8889/mydb?" + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
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
            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());

                continue;
            }


        }
    }

}
