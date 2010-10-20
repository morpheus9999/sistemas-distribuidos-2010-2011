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

        while(!Main.exit) {
            try {
                Generic gen = new Generic();
                Message mes = new Message();
                Login log = new Login();
                User reg = new User();
                Bet bet = new Bet();

                int opt = Main.opt.getOption();
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
                        log.setName(Main.log.getName());
                        log.setPassword(Main.log.getPassword());

                        /*  lists current matches   */
                        gen.setCode(Constants.matchesCode);
                        gen.setObj(log);
                        break;
                    case Constants.betCode:
                        /*  new Bet object  */
                        bet.setAposta(Main.bet.getAposta());
                        bet.setBet(Main.bet.getBet());
                        bet.setIdGame(Main.bet.getIdGame());

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
                        mes.setHashtable(Main.buffer.getHashtable());
                        mes.setAuthor(Main.buffer.getAuthor());

                        gen.setCode(Constants.messageCode);
                        gen.setObj(mes);
                        break;
                    case Constants.messageAllCode:
                        /*  messages all users  */
                        mes.setHashtable(Main.bufferAll.getHashtable());
                        mes.setAuthor(Main.bufferAll.getAuthor());

                        gen.setCode(Constants.messageAllCode);
                        gen.setObj(mes);
                        break;
                    case Constants.logoutCode:
                        /*  logout and exit */
                        gen.setCode(Constants.logoutCode);

                        /*  only sends if connection is on  */
                        if(Main.connected) {
                            /*  sends the request to the server */
                            Main.out.writeObject(gen);
                            Main.out.flush();
                        }
                        /*  ends the thread  */
                        return;
                    case Constants.loginCode:
                        /*  new Login object    */
                        log.setName(Main.log.getName());
                        log.setPassword(Main.log.getPassword());
                        /*  login   */
                        gen.setCode(Constants.loginCode);
                        gen.setObj(log);
                        break;
                    case Constants.regCode:
                        /*  new user profile object */
                        reg.setName(Main.reg.getName());
                        reg.setMail(Main.reg.getMail());
                        reg.setPassword(Main.reg.getPassword());
                        reg.setCredit(Main.reg.getCredit());
                        /*  register    */
                        gen.setCode(Constants.regCode);
                        gen.setObj(reg);
                        break;
                    default:
                        System.out.println("Wrong option :X");
                        break;
                }

                /*  only sends if connection is on  */
                if(Main.connected) {
                    /*  sends the request to the server */
                    Main.out.writeObject(gen);
                    Main.out.flush();
                }
            } catch (IOException ex) {
                //  System.out.println("IOException sending object: "+ex.getMessage());
                //  System.out.println("Error ocurred. Ending sending thread");
            }
        }


        System.out.println("sender thread down!!");
    }
}