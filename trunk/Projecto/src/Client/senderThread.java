/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import java.io.IOException;

/**
 *
 * @author JLA
 */
public class senderThread extends Thread{

    /*
     * Constructor
     */
    public senderThread() {
    }

    /*
     * Run
     * the main method of the sender thread
     */
    public void run() {
        Generic gen = new Generic();

        while(true) {
            try {
                switch(Main.opt.getOption()) {
                    case 1:
                        /*  get credit balance  */
                        gen.setCode(Constants.creditCode);
                        gen.setObj(new Credit());
                        break;
                    case 2:
                        /*  resets credit balance   */
                        gen.setCode(Constants.regCode);

                        /*  adicionar o objecto com a informacao do registo  */

                        break;
                    case 3:
                        /*  lists current matches   */
                        gen.setCode(Constants.matchesCode);

                        /*  adicionar o objecto com a informacao dos jogos  */

                        break;
                    case 4:
                        /*  bets on a match */
                        gen.setCode(Constants.betCode);

                        
                        /*  !!!!!!!!!!preciso de saber as variaveis para fazer a classe !!!!!!!!!!!!!!!*/

                        break;
                    case 5:
                        /*  lists online users  */
                        gen.setCode(Constants.onlineUsersCode);
                        break;
                    case 6:
                        /*  messages a user */
                        gen.setCode(Constants.messageCode);
                        gen.setObj(Main.message);
                        break;
                    case 7:
                        /*  messages all users  */
                        gen.setCode(Constants.messageAllCode);
                        gen.setObj(Main.message);
                        break;
                    case 8:
                        /*  logout and exit */
                        gen.setCode(Constants.logoutCode);
                        return;
                    case 100:
                        /*  login   */
                        gen.setCode(Constants.loginCode);
                        gen.setObj(Main.log);
                        break;
                    case 101:
                        /*  register    */
                        gen.setCode(Constants.regCode);
                        gen.setObj(Main.reg);
                        break;
                    default:
                        System.out.println("Wrong option :X");
                        break;
                }
                
                /*  sends the request to the server */
                Main.out.writeObject(gen);
            } catch (IOException ex) {
                System.out.println("IOException sending object: "+ex.getMessage());
            }
        }
    }
}
