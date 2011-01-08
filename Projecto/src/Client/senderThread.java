/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;
import Client_Server.ViewMatch;
import java.io.IOException;

/**
 *
 * @author JLA
 */
public class senderThread extends Thread{
    private static boolean instance_flag = false;

    /*
     * Constructor
     */
    public senderThread() {
        if (instance_flag) {
            throw new SingletonException("Sender already instanced");
        } else {
            instance_flag = true; //set flag for 1
        }
    }

    /*
     * Run
     * the main method of the sender thread
     */
    public void run() {

        while(!Client.exit) {
            try {
                Generic gen = new Generic();
                Message mes = new Message();
                Login log = new Login();
                User reg = new User();
                Bet bet = new Bet();

                int opt = Client.opt.getOption();
                switch(opt) {
                    case Constants.creditCode:
                        /*  get credit balance  */
                        gen.setCode(Constants.creditCode);
                        gen.setObj(new Credit());
                        break;
                    case Constants.resetCode:
                        /*  resets credit balance   */
                        gen.setCode(Constants.resetCode);
                        gen.setObj(new Credit());
                        break;
                    case Constants.matchesCode:
                        /*  new Login object    */
                        log.setName(Client.log.getName());
                        log.setPassword(Client.log.getPassword());

                        /*  lists current matches   */
                        gen.setCode(Constants.matchesCode);
                        gen.setObj(log);
                        break;
                    case Constants.betCode:
                        /*  new Bet object  */
                        bet.setAposta(Client.bet.getAposta());
                        bet.setBet(Client.bet.getBet());
                        bet.setIdGame(Client.bet.getIdGame());

                        /*  bets on a match */
                        gen.setCode(Constants.betCode);
                        gen.setObj(bet);
                        break;
                    case Constants.onlineUsersCode:
                        /*  lists online users  */
                        gen.setCode(Constants.onlineUsersCode);
                        break;
                    case Constants.messageCode:
                        /*  messages a user */
                        mes.setHashtable(Client.buffer.getHashtable());
                        mes.setAuthor(Client.buffer.getAuthor());

                        gen.setCode(Constants.messageCode);
                        gen.setObj(mes);
                        break;
                    case Constants.messageAllCode:
                        /*  messages all users  */
                        mes.setHashtable(Client.bufferAll.getHashtable());
                        mes.setAuthor(Client.bufferAll.getAuthor());

                        gen.setCode(Constants.messageAllCode);
                        gen.setObj(mes);
                        break;
                    case Constants.logoutCode:
                        /*  logout and exit */
                        gen.setCode(Constants.logoutCode);

                        /*  only sends if connection is on  */
                        if(Client.connected) {
                            /*  sends the request to the server */
                            Client.out.writeObject(gen);
                            Client.out.flush();
                        }
                        /*  ends the thread  */
                        return;
                    case Constants.loginCode:
                        /*  new Login object    */
                        log.setName(Client.log.getName());
                        log.setPassword(Client.log.getPassword());
                        /*  login   */
                        gen.setCode(Constants.loginCode);
                        gen.setObj(log);
                        break;
                    case Constants.regCode:
                        /*  new user profile object */
                        reg.setName(Client.reg.getName());
                        reg.setMail(Client.reg.getMail());
                        reg.setPassword(Client.reg.getPassword());
                        reg.setCredit(Client.reg.getCredit());
                        /*  register    */
                        gen.setCode(Constants.regCode);
                        gen.setObj(reg);
                        break;
                    case Constants.requestMessage:
                        gen.setCode(Constants.requestMessage);
                        break;
                    default:
                        System.out.println("Wrong option :X");
                        break;
                }

                /*  only sends if connection is on  */
                if(Client.connected) {
                    /*  sends the request to the server */
                    Client.out.writeObject(gen);
                    Client.out.flush();
                }
            } catch (IOException ex) {
                //  System.out.println("IOException sending object: "+ex.getMessage());
                //  System.out.println("Error ocurred. Ending sending thread");
            }
        }


        System.out.println("sender thread down!!");
    }
}