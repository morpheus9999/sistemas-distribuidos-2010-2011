/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI;

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


        while(!Main.exit) {
            Generic gen = new Generic();
            Credit cred = new Credit();
            OnlineUsers online = new OnlineUsers();
            Login log = new Login();

            if(Main.login) {
                log.setName(Main.lg.getName());
                log.setPassword(Main.lg.getPassword());
            }

            try {
                if(!Main.connected) {
                    /*  gets stub   */
                    this.obj = (RMIInterface) Naming.lookup(Constants.clientPrimaryServerRMI);
                    /*  sets callback object    */
                    CallbackMethods callback = new CallbackMethods();
                    this.obj.setCallback((CallbackInterface) callback);

                    /*  sets connected to true  */
                    Main.connected = true;
                }

                switch(Main.opt.getOption()) {
                    case Constants.loginCode:
                        if(this.obj.login(Main.gen)) {
                            System.out.println("Login sucessfull");
                            Main.login = true;
                        }
                        else
                            System.out.println("Login failed");

                        Main.opt.setOption(Constants.loginCode);
                    break;
                    case Constants.regCode:
                        if(this.obj.register(Main.gen))
                            System.out.println("Register sucessfull");
                        else
                            System.out.println("Register failed");
                    break;
                    case Constants.creditCode:
                        /*  credit  */
                        gen = this.obj.getCredit(Main.gen, log);
                        cred = (Credit) gen.getObj();

                        System.out.println("Your credit is: "+cred.getCredit());
                    break;
                    case Constants.resetCode:
                        /*  reset credit    */
                        gen = this.obj.resetCredit(Main.gen, log);
                        cred = (Credit) gen.getObj();

                        System.out.println("Your credit (reseted) is: "+cred.getCredit());
                    break;
                    case Constants.matchesCode:
                        /*  view matches    */
                        gen = this.obj.viewMathces(Main.gen);
                        Vector <ViewMatch> matches= (Vector<ViewMatch>) gen.getObj();
                        System.out.println("MATCHES:");
                        for(int x=0;x<matches.size();x++){
                            System.out.println("["+matches.elementAt(x).getIdJogo()+"]"+ matches.elementAt(x).getHome()+" VS "+matches.elementAt(x).getFora());
                        }
                    break;
                    case Constants.betCode:
                        /*  bet */
                        gen = this.obj.bet(Main.gen, log);

                        if(gen.getConfirmation())
                            System.out.println("Bet placed");
                        else
                            System.out.println("Bet failed");
                    break;
                    case Constants.onlineUsersCode:
                        /*  online users    */
                        gen = this.obj.onlineUsers(Main.gen);
                        online = (OnlineUsers)gen.getObj();
                        online.printOnlineUsers();
                    break;
                    case Constants.messageCode:
                        /*  send a message to a single person   */
                        if(this.obj.messageUser(Main.gen)) {
                            System.out.println("Message sent");
                            Interface.buffer.clearHashtable();
                        }
                        else
                            System.out.println("Message failed");
                    break;
                    case Constants.messageAllCode:
                        /*  send a message to everyone  */
                        if(this.obj.messageAll(Main.gen)) {
                            System.out.println("Message sent");
                            Interface.bufferAll.clearHashtable();
                        }
                        else
                            System.out.println("Message failed");
                    break;
                    case Constants.logoutCode:
                        /*  logout  */
                        this.obj.logout(log);
                    break;

                    case Constants.requestMessage:
                        /*  requests messages from server   */
                        this.obj.getMessage(log);
                    break;
                    default:
                        System.out.println("Wrong code (in RMI thread)");
                    break;
                }

            } catch (NotBoundException ex) {
            } catch (MalformedURLException ex) {
            } catch (RemoteException ex) {
                reconnect();
            } catch (Exception ex) {
                System.out.println("Weird Exception: "+ex.getMessage());
            }
        }
    }




    public boolean reconnect() {
        int serverFlag = 0;
        Main.connected = false;
        System.out.println("Connection lost");

        try {
            while (!Main.exit) {
                System.out.print("Trying to connect");
                for(int i = 1; i <= Constants.tries && !Main.exit; i++, Thread.sleep(Constants.reconnectTime)) {
                    try {
                        System.out.print(".");

                        /*  alternate between primary server and backup server until it connets */
                        if(serverFlag%2 == 0)
                            this.obj = (RMIInterface) Naming.lookup(Constants.clientPrimaryServerRMI);
                        else
                            this.obj = (RMIInterface) Naming.lookup(Constants.clientBackupServerRMI);

                        /*  sets callback object    */
                        CallbackMethods callback = new CallbackMethods();
                        this.obj.setCallback((CallbackInterface)callback);

                        /*  sets connected to true  */
                        Main.connected = true;

                        /*  if the user was logged, it does the login automatically */
                        if(Main.login) {
                            Generic gen = new Generic();
                            Login lg = new Login();
                            lg.setName(Main.lg.getName());
                            lg.setPassword(Main.lg.getPassword());

                            gen.setCode(Constants.loginCode);
                            gen.setObj(lg);

                            this.obj.login(gen);
                        }

                        if(Main.connected && Main.login)
                            Main.opt.setOption(Constants.requestMessage);
                        
                        System.out.println("\nConnection recovered :)");
                        return true;
                    } catch (NotBoundException ex) {

                    } catch (MalformedURLException ex) {

                    } catch (RemoteException ex) {

                    }
                }
                System.out.println("");
                
                /*  changes the flag of the server to connect   */
                serverFlag = ++serverFlag%2;
                
                System.out.print("Changing server: ");
                if(serverFlag == 0)
                    System.out.println("Primary");
                else
                    System.out.println("Backup");
            }
        } catch (InterruptedException ex) {
            //System.out.println("Error in sleeping thread");
        }

        System.out.println("");
        return false;
    }

}
