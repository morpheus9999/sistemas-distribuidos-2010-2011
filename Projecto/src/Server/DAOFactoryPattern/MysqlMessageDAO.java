/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import Client_Server.Message;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.*;
/**
 *
 * @author jojo
 */
public class MysqlMessageDAO implements MessageDAO{

    public MysqlMessageDAO() {
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
