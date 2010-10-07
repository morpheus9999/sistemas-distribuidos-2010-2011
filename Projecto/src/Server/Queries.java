/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.Generic;
import Client_Server.Login;

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

        return true;
    }


}
