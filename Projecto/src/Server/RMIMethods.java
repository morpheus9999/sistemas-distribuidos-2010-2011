/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.CallbackInterface;
import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Client_Server.RMIInterface;
import Server.IteratorPattern.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.Unreferenced;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author JLA
 */
public class RMIMethods extends java.rmi.server.UnicastRemoteObject implements RMIInterface {

    static CallbackInterface call=null;

    public RMIMethods() throws java.rmi.RemoteException {
        
    }

    
    
    /**
     * getCallback - gets an interface to interact with the user
     * @param callback
     * @throws java.rmi.RemoteException
     */
    public void setCallback(CallbackInterface callback) throws java.rmi.RemoteException {
        this.call = callback;
    }
    public void setCallbackTomcat(CallbackInterfaceTomcat callback) throws java.rmi.RemoteException {
        Main.calbackInterfaceTomcat=callback;

    }

    /**
     * Login
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public  boolean login(Generic gen) throws java.rmi.RemoteException {
        System.out.println("Login:::");
        
        
        //accountDAO.loginAccount(gen);
        if (Main.accountDAO.loginAccount(gen)) {
            Login lg = (Login) gen.getObj();
            System.out.println("lg" + lg.getName());
            
            if(Main.onlineUsers.containsKey(lg.getName())){
                return false;
            }
            
            if (this.call == null) 
                Main.onlineUsers.put(lg.getName(), new RMITomcat());
            else
                Main.onlineUsers.put(lg.getName(), new RMI(this.call));
            
            
            
            

            
            
            /*
            if(Main.onlineUsersTCP.containsKey(lg.getName()))
                return false;
            else if(Main.onlineUsersRMI.containsKey(lg.getName()))
                return false;
            else if(Main.onlineUsersRMITomcat.contains(lg.getName()))
                return false;

            if(this.call==null)
                Main.onlineUsersRMITomcat.addElement(lg.getName());

            else
                Main.onlineUsersRMI.put(lg.getName(), this.call);
             * 
             * 
             */
            if(Main.calbackInterfaceTomcat!=null){
                Main.calbackInterfaceTomcat.UpdateUsersOnline();

            }
             
            return true;
        } else
            return false;
    }

    /**
     * Logout
     * @param lg
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public  boolean logout(Login lg) throws java.rmi.RemoteException {
        
        Main.onlineUsers.remove(lg.getName());
        
        
        /*
        if(Main.onlineUsersRMI.containsKey(lg.getName())) {
            Main.onlineUsersRMI.remove(lg.getName());
        }else if(Main.onlineUsersRMITomcat.contains(lg.getName())){
            Main.onlineUsersRMITomcat.remove(lg.getName());
        }
         * 
         */
        if (Main.calbackInterfaceTomcat != null) {
            Main.calbackInterfaceTomcat.UpdateUsersOnline();
        }
        
        return true;
    }

    /**
     * Register
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public  boolean register(Generic gen) throws java.rmi.RemoteException {
        //return accountDAO.insertAccount(gen);
        return Main.accountDAO.insertAccount(gen);
    }

    /**
     * getCredit
     * @param gen - a generic object that will store the solution
     * @param log - a object containing info about the user
     * @return gen - the solution to the problem
     * @throws java.rmi.RemoteException
     */
    public  Generic getCredit(Generic gen, Login log) throws java.rmi.RemoteException {
        return Main.accountDAO.getCreditAccount(gen, log);
    }

    /**
     * resetCredit
     * @param gen - a generic object that will store the solution
     * @param log - a object containing info about the user
     * @return gen - the solution to the problem
     * @throws java.rmi.RemoteException
     */
    public  Generic resetCredit(Generic gen, Login log) throws java.rmi.RemoteException {
        return Main.accountDAO.resetCreditAccount(gen, log);
    }

    /**
     * View current matches
     * @param gen
     * @return
     * @throws RemoteException
     */
    public  Generic viewMathces(Generic gen) throws RemoteException {
        return Main.customerDAO.viewMatchesCustomer(gen, Main.game.getRonda());
    }

    /**
     * Bet on a match
     * @param gen
     * @param lg
     * @return
     * @throws RemoteException
     */
    public  Generic bet(Generic gen, Login lg) throws RemoteException {
        //meter a variavel da ronda....
        if(Main.customerDAO.newBetCustomer(gen,lg,Main.game.getRonda()))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(false);

        return gen;
    }

    /**
     * Online users
     * @param gen
     * @return
     * @throws IOException
     */
    public  Generic onlineUsers(Generic gen) {
        OnlineUsers list = new OnlineUsers();
        
        Iterator<String> tempo = Main.onlineUsers.keySet().iterator();
        
        while(tempo.hasNext())
            list.addEntry(tempo.next());
        
            
        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }

    private  void message(String fromUser, String toUser, String message) throws RemoteException {
        /*  creates individual message  */
        Message mes = new Message(toUser, message);
        mes.setAuthor(fromUser);
        System.out.println("ENTRA!!!!2");

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);
        gen.setObj(mes);
        
        
        if(Main.onlineUsers.containsKey(toUser)){
            if (Main.onlineUsers.get(toUser) instanceof TCP) {
                ClientThreadTCP sock = (ClientThreadTCP) ((TCP) Main.onlineUsers.get(toUser)).getCallbackObject();
                try {
                    sock.out.writeObject(gen);
                } catch (IOException error) {
                    /*  if it throws an error, delete it    */
                    Main.onlineUsers.remove(toUser);
                    Main.accountDAO.setMensagensAccount(fromUser, toUser, message);

                }
            } else if (Main.onlineUsers.get(toUser) instanceof RMI) {

                CallbackInterface callback = (CallbackInterface) ((RMI) Main.onlineUsers.get(toUser)).getCallbackObject();

                try {
                    callback.printMessage(fromUser, message);
                } catch (Exception error) {
                    /*  if it throws an error, delete it    */
                    Main.onlineUsers.remove(toUser);
                    Main.accountDAO.setMensagensAccount(fromUser, toUser, message);
                }

            } else if (Main.onlineUsers.get(toUser) instanceof RMITomcat) {
                CallbackInterfaceTomcat callback = (CallbackInterfaceTomcat) ((RMITomcat) Main.onlineUsers.get(toUser)).getCallbackObject();

                try {
                    callback.printMessage(fromUser, message, toUser);
                } catch (Exception error) {
                    error.printStackTrace();
                    System.out.println(" ::" + callback);
                    /*  if it throws an error, delete it    */
                    Main.onlineUsersRMI.remove(toUser);
                    Main.accountDAO.setMensagensAccount(fromUser, toUser, message);
                }

            }
        } else { /*    or stores to send later accordingly    */
            Main.accountDAO.setMensagensAccount(fromUser, toUser, message);

            System.out.println(toUser + " esta offline");
        }  
        
        
//
//        /*  checks if the user is online and sends  */
//        if(Main.onlineUsersTCP.containsKey(toUser)) {
//            ClientThreadTCP sock = Main.onlineUsersTCP.get(toUser);
//
//            try {
//                sock.out.writeObject(gen);
//            } catch (IOException error) {
//                /*  if it throws an error, delete it    */
//                Main.onlineUsersTCP.remove(toUser);
//            }
//        } else if(Main.onlineUsersRMI.containsKey(toUser)) {
//                    System.out.println("ENTRA!!!!3");
//
//            CallbackInterface callback = Main.onlineUsersRMI.get(toUser);
//
//            try {
//                callback.printMessage(fromUser, message);
//            } catch (Exception error) {
//                /*  if it throws an error, delete it    */
//                Main.onlineUsersRMI.remove(toUser);
//            }
//        } else if(Main.onlineUsersRMITomcat.contains(toUser)) {
//                    System.out.println("ENTRA!!!!4");
//
//            CallbackInterfaceTomcat callback = Main.calbackInterfaceTomcat;
//
//            try {
//                callback.printMessage(fromUser, message,toUser);
//            } catch (Exception error) {
//                error.printStackTrace();
//                System.out.println(" ::"+callback);
//                /*  if it throws an error, delete it    */
//                Main.onlineUsersRMI.remove(toUser);
//            }
//        }
//        else { /*    or stores to send later accordingly    */
//            Main.accountDAO.setMensagensAccount(fromUser, toUser, message);
//
//            System.out.println(toUser+" esta offline");
//        }
    }

    /**
     * Messages a single user
     * @param gen
     * @return
     * @throws RemoteException
     */
     public  boolean messageUser(Generic gen) throws RemoteException {
        String fromUser = null, toUser = null;
        Vector<String> messageVector;
        Enumeration<String> message;
        Message mes = (Message) gen.getObj();

        fromUser = mes.getAuthor();

        /*  runs through the received buffer and sends/stores messages  */
        Enumeration<String> enumerator = mes.getKeysEnumeration();


        /*  goes through all the messages for the desired user  */
        while(enumerator.hasMoreElements()) {
            toUser = enumerator.nextElement();
            messageVector = mes.getEntry(toUser);
            message = messageVector.elements();

            while(message.hasMoreElements())
                message(fromUser, toUser, message.nextElement());
        }

        return true;
    }

    /**
      * Messages all users
      * @param gen
      * @return true
      * @throws RemoteException
      * @throws IOException
      */
    public  boolean messageAll(Generic gen) throws RemoteException {
        String fromUser = null, toUser = null, id = null;
        Vector<String> messageVector, userVector;
        Message mes = (Message) gen.getObj();
        Enumeration<String> keys, userEnumerator, message;


        fromUser = mes.getAuthor();
        /*
         *  runs through the received buffer and sends/stores messages
         *  to all people registered
         */
        keys = mes.getKeysEnumeration();
        while(keys.hasMoreElements()) {
            /*
             *  since message is for all
             *  the key doesn't matter, only for obtaining each message
             */
            id = keys.nextElement();
            messageVector = mes.getEntry(id);

            userVector = Queries.getUsers();
            userEnumerator = userVector.elements();

            while(userEnumerator.hasMoreElements()) {
                toUser = userEnumerator.nextElement();
                message = messageVector.elements();

                while(message.hasMoreElements())
                    message(fromUser, toUser, message.nextElement());
            }
        }


        return true;
    }

    public  void getMessage(Login lg) throws  RemoteException {
        Message temp;
        System.out.println("ENTRA!!!!1");
        for(temp = Main.accountDAO.getMensagensAccount(lg.getName()); temp != null; temp = Main.accountDAO.getMensagensAccount(lg.getName()))
            message(temp.getAuthor(), temp.getTo(), temp.getText());
    }
}
