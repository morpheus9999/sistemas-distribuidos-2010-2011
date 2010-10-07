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

    public static Generic login(Generic gen) {
        Login lg = (Login) gen.getObj();

        /*  aqui faz-se as queries e as comparacoes */
        System.out.println("login name: "+lg.getName());
        System.out.println("login password: "+lg.getPassword());

        gen.setConfirmation(true);
        return gen;
    }
}
