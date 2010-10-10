/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.Generic;
import Client_Server.Login;
import java.sql.*;


/**
 *
 * @author JLA
 */
public class Queries {

    static boolean login(Generic generic) {
        Login lg = (Login)generic.getObj();

        /*  aqui vao as queries */
        System.out.println("recebido");
        System.out.println("name: "+ lg.getName());
        System.out.println("pass: "+ lg.getPassword());


        try {
                Class.forName("com.mysql.jdbc.Driver");
                String connectionUrl = "jdbc:mysql://localhost/sd?"
                        + "user=root&password=root";
                Connection con = DriverManager.getConnection(connectionUrl);
                Statement stmt = con.createStatement();
                ResultSet rs;

                int rowCount = -1;
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT COUNT(nome) FROM users WHERE nome='" + lg.getName() + "'and password=" +  lg.getPassword());
                rs.next();
                rowCount = rs.getInt(1);

                if (rowCount == 1) {

                    //e falta adicionar a lista de clientes logados
                    return true;
                    

                } else {
                    return false;
                }
            } catch (SQLException e) {
                
                System.out.println("SQL Exception: " + e.toString());
                return false;
            } catch (ClassNotFoundException cE) {
                System.out.println("Class Not Found Exception: " + cE.toString());
                return false;
            }

       
    }


}
