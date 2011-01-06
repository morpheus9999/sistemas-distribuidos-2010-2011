/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.DAOFactoryPattern;

import BetPackage.IBetManager;
import BetPackage.IMatch;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.*;

import java.util.List;
import java.util.Vector;

/**
 *
 * @author jojo
 */
public class MysqlAccountDAO implements AccountDAO {

    public MysqlAccountDAO() {
        
    }

    public boolean loginAccount(Generic generic) {

        Connection con = MysqlDAOFactory.createConnection();

        Login lg = (Login) generic.getObj();

        try {

            Statement stmt = con.createStatement();
            ResultSet rs;

            int rowCount = -1;
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(Nome) FROM Cliente WHERE Nome='" + lg.getName() + "' and Password= '" + lg.getPassword() + "';");
            rs.next();
            rowCount = rs.getInt(1);

            if (rowCount == 1) {

                stmt.close();
                return true;


            } else {
                stmt.close();
                return false;
            }
        } catch (SQLException e) {
            //ex.printStackTrace();

            return false;
        }

    }

    public boolean insertAccount(Generic generic) {

        Connection con = MysqlDAOFactory.createConnection();

        User lg = (User) generic.getObj();

        try {

            Statement stmt = con.createStatement();

            boolean flag = stmt.execute("INSERT INTO Cliente VALUES ('" + lg.getName() + "','" + lg.getPassword() + "','" + lg.getMail() + "'," + lg.getCredit() + ")");
            if (!flag) {
                stmt.close();
                return true;
            } else {
                stmt.close();
                return false;
            }

        } catch (SQLException ex) {
            //ex.printStackTrace();
            return false;

        }

    }

    public Vector<String> getUsernameListAccount() {

        Vector<String> m = new Vector<String>();
        String nome;

        Connection con = MysqlDAOFactory.createConnection();
        
        try {

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
            //e.printStackTrace();
        }
        return null;

    }
    
    public Generic getCreditAccount(Generic gen, Login lg) {
         // nao deverá ser user
        Credit cr = (Credit) gen.getObj();
        
        System.out.println("recebido GETCREDITO");
        System.out.println("name: " + lg.getName());
        Connection con = MysqlDAOFactory.createConnection();
            try {
                
                Statement stmt = con.createStatement();
                ResultSet rs1;
                    rs1 = stmt.executeQuery("SELECT Credito FROM Cliente WHERE Nome = '" + lg.getName()+"'" );
                    rs1.next();
                    int bet = rs1.getInt("Credito");
                    System.out.println(" credito:"+bet);
                    cr.setCredit(bet);
                    gen.setConfirmation(true);
                    gen.setObj(cr);
                    stmt.close();
                    return gen;
            } catch (SQLException e) {

                System.out.println("SQL Exception: " + e.toString());
                gen.setConfirmation(false);
                gen.setObj(cr);
                return gen;
            }


        }
    
    
    public Generic resetCreditAccount(Generic temp, Login lg) {
        //System.out.println("reset login:"+lg.getName());
        Statement stmt = null;
        Connection con = MysqlDAOFactory.createConnection();
            try {
                
                stmt = con.createStatement();
                
                boolean rs = stmt.execute("UPDATE  `mydb`.`Cliente` SET  `Credito` = '" + Constants.resetCredito + "' WHERE `Cliente`.`Nome`= '" + lg.getName() + "'");
                Credit m = (Credit) temp.getObj();
                m.setCredit(Constants.resetCredito);
                temp.setObj(m);
                temp.setConfirmation(true);
                stmt.close();
                return temp;
            } catch (SQLException e) {
                System.out.println("Erro iniciar queries:" + e.toString());
                return null;
            }

        
    }
    public Message getMensagensAccount(String nome) {
        // nao deverá ser user
        Connection con = MysqlDAOFactory.createConnection();
        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());
        Message k;
        int id;
        
       
            try {
                
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
                return null;
        }
    }

    public boolean setMensagensAccount(String de, String para ,String mensagem) {
        // nao deverá ser user

        //n sei que nome dar
        //System.out.println("bet: "+ lg.getBetXpto());\
        
        Connection con = MysqlDAOFactory.createConnection();
        System.out.println("NOME SET MSG:"+para);
        Message k;

            try {
                
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
                return false;
            }


        }
    
    
    
}
