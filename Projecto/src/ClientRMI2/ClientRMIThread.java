/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientRMI2;

import Client_Server.Bet;
import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.OnlineUsers;
import Client_Server.RMIInterface;
import Client_Server.ViewMatch;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojo
 */
public class ClientRMIThread extends Thread {

    private RMIInterface obj;

    public ClientRMIThread() {
    }

    public void run() {
        RMIInterface obj = null;


        while (!Client.exit) {
            Generic gen = new Generic();
            Credit cred = new Credit();
            OnlineUsers online = new OnlineUsers();
            Login log = new Login();

            if (Client.login) {
                log.setName(Client.log.getName());
                log.setPassword(Client.log.getPassword());
            }

            try {
                if (!Client.connected) {
                    /*  gets stub   */
                    this.obj = (RMIInterface) Naming.lookup(Constants.clientPrimaryServerRMI);
                    /*  sets callback object    */
                    CallbackMethods callback = new CallbackMethods();
                    this.obj.setCallback((CallbackInterface) callback);

                    /*  sets connected to true  */
                    Client.connected = true;
                }

                switch (Client.opt.getOption()) {

                    case Constants.loginCode:

                        if (this.obj.login(Client.gen)) {

                            Client.getGUI().parseRequest(Constants.loginCode, true, "Login sucessfull");


                            Client.login = true;
                        } else {
                            Client.getGUI().parseRequest(Constants.loginCode, false, "Login failed");
                        }

                        Client.opt.setOption(Constants.loginCode);
                        break;
                    case Constants.regCode:

                        if (this.obj.register(Client.gen)) {
                            Client.getGUI().parseRequest(Constants.regCode, true, "Register sucessfull");
                        } else {
                            Client.getGUI().parseRequest(Constants.regCode, false, "Register failed");
                        }
                        Client.opt.setOption(Constants.regCode);
                        break;
                    case Constants.creditCode:
                        /*  credit  */
                        //Client.getGUI().displayMessage("CREDITO");
                        gen = this.obj.getCredit(Client.gen, log);
                        cred = (Credit) gen.getObj();

                        Client.getGUI().parseRequest(Constants.creditCode, true, "Your credit is: " + cred.getCredit());
                        break;
                    case Constants.resetCode:
                        /*  reset credit    */
                        gen = this.obj.resetCredit(Client.gen, log);
                        cred = (Credit) gen.getObj();

                        Client.getGUI().parseRequest(Constants.resetCode, true, "Your credit (reseted) is: " + cred.getCredit());
                        break;
                    case Constants.matchesCode:
                        /*  view matches    */
                        gen = this.obj.viewMathces(Client.gen);
                        Vector<ViewMatch> matches = (Vector<ViewMatch>) gen.getObj();

                        Client.getGUI().parseRequest(Constants.matchesCode, true, gen.getObj());
                        //Client.getGUI().displayMessage("MATCHES:");
//                        for(int x=0;x<matches.size();x++){
//                            Client.getGUI().displayMessage("["+matches.elementAt(x).getIdJogo()+"]"+ matches.elementAt(x).getHome()+" VS "+matches.elementAt(x).getFora());
//                        }
                        break;
                    case Constants.betCode:
                        /*  bet */
                        gen = this.obj.bet(Client.gen, log);

                        if (gen.getConfirmation()) {
                            Client.getGUI().parseRequest(Constants.betCode, true, "Bet placed");
                        } else {
                            Client.getGUI().parseRequest(Constants.betCode, false, "Bet failed");
                        }
                        break;
                    case Constants.onlineUsersCode:
                        /*  online users    */
                        gen = this.obj.onlineUsers(Client.gen);
                        online = (OnlineUsers) gen.getObj();
                        Client.getGUI().parseRequest(Constants.onlineUsersCode, true, online);
                        //online.printOnlineUsers();
                        break;
                    case Constants.messageCode:
                        /*  send a message to a single person   */
                        if (this.obj.messageUser(Client.gen)) {
                            Client.getGUI().displayMessage("Message sent");
                            Interface.buffer.clearHashtable();
                        } else {
                            Client.getGUI().displayMessage("Message failed");
                        }
                        break;
                    case Constants.messageAllCode:
                        /*  send a message to everyone  */
                        if (this.obj.messageAll(Client.gen)) {
                            Client.getGUI().displayMessage("Message sent");
                            Interface.bufferAll.clearHashtable();
                        } else {
                            Client.getGUI().displayMessage("Message failed");
                        }
                        break;
                    case Constants.logoutCode:
                        /*  logout  */
                        this.obj.logout(log);
                        Client.getGUI().parseRequest(Constants.logoutCode, true, "logout");
                        break;

                    case Constants.requestMessage:
                        /*  requests messages from server   */
                        this.obj.getMessage(log);

                        Client.opt.setOption(Constants.requestMessage);
                        break;
                    default:
                        Client.getGUI().displayMessage("Wrong code (in RMI thread)");
                        break;

                }
                //System.out.println("OPTION:"+Main.opt.getOption());

            } catch (NotBoundException ex) {
            } catch (MalformedURLException ex) {
            } catch (RemoteException ex) {

                reconnect();
            } catch (Exception ex) {
                System.out.println("Weird Exception: " + ex.getMessage());
            }
        }
    }

    public boolean reconnect() {
        int serverFlag = 0;
        Client.connected = false;
        System.out.println("Connection lost");

        try {
            while (!Client.exit) {
                System.out.print("Trying to connect");
                for (int i = 1; i <= Constants.tries && !Client.exit; i++, Thread.sleep(Constants.reconnectTime)) {
                    try {
                        System.out.print(".");

                        /*  alternate between primary server and backup server until it connets */
                        if (serverFlag % 2 == 0) {
                            this.obj = (RMIInterface) Naming.lookup(Constants.clientPrimaryServerRMI);
                        } else {
                            this.obj = (RMIInterface) Naming.lookup(Constants.clientBackupServerRMI);
                        }

                        /*  sets callback object    */
                        CallbackMethods callback = new CallbackMethods();
                        this.obj.setCallback((CallbackInterface) callback);

                        /*  sets connected to true  */
                        Client.connected = true;

                        if (Client.login) {
                            /*  if the user was logged, it does the login automatically */
                            Generic gen = new Generic();
                            Login lg = new Login();
                            lg.setName(Client.lg.getName());
                            lg.setPassword(Client.lg.getPassword());

                            gen.setCode(Constants.loginCode);
                            gen.setObj(lg);

                            this.obj.login(gen);
                            gen = new Generic();
                            /*  asks for messages sent by other users while connection went off */
                            Client.opt.setOption(Constants.requestMessage);
                            /*  waits for the messages  */
                            Client.opt.getOption();

                            /*  sends messages to the server buffered while offline */
                            gen = new Generic();
                            gen = Interface.messageSingleBuffer();
                            if (gen != null) {
                                this.obj.messageUser(gen);
                            }
                            gen = new Generic();
                            gen = new Generic();
                            gen = Interface.messageAllBuffer();
                            if (gen != null) {
                                this.obj.messageAll(gen);
                            }
                        }

                        System.out.println("\nConnection recovered :)");
                        return true;
                    } catch (NotBoundException ex) {
                    } catch (MalformedURLException ex) {
                    } catch (RemoteException ex) {
                    }
                }
                System.out.println("");

                /*  changes the flag of the server to connect   */
                serverFlag = ++serverFlag % 2;

                System.out.print("Changing server: ");
                if (serverFlag == 0) {
                    System.out.println("Primary");
                } else {
                    System.out.println("Backup");
                }
            }
        } catch (InterruptedException ex) {
            //System.out.println("Error in sleeping thread");
        }

        System.out.println("");
        return false;
    }
}
