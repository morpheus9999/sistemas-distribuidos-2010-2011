/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.CallbackInterface;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Client_Server.RMIInterface;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author JLA
 */
public class RMIMethods extends java.rmi.server.UnicastRemoteObject implements RMIInterface {

    static CallbackInterface call;

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

    /**
     * Login
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean login(Generic gen) throws java.rmi.RemoteException {
        if(Queries.login(gen)) {
            Login lg = (Login) gen.getObj();
            Main.onlineUsersRMI.put(lg.getName(), this.call);
            return true;
        } else
            return false;
    }

    /**
     * Login
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean logout(Login lg) throws java.rmi.RemoteException {
        if(Main.onlineUsersRMI.containsKey(lg.getName())) {
            Main.onlineUsersRMI.remove(lg.getName());
        }
        return true;
    }

    /**
     * Register
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean register(Generic gen) throws java.rmi.RemoteException {
        return Queries.register(gen);
    }

    /**
     * getCredit
     * @param gen - a generic object that will store the solution
     * @param log - a object containing info about the user
     * @return gen - the solution to the problem
     * @throws java.rmi.RemoteException
     */
    public Generic getCredit(Generic gen, Login log) throws java.rmi.RemoteException {
        return Queries.getCredit(gen, log);
    }

    /**
     * resetCredit
     * @param gen - a generic object that will store the solution
     * @param log - a object containing info about the user
     * @return gen - the solution to the problem
     * @throws java.rmi.RemoteException
     */
    public Generic resetCredit(Generic gen, Login log) throws java.rmi.RemoteException {
        return Queries.resetCredit(gen, log);
    }

    /**
     * View current matches
     * @param gen
     * @return
     * @throws RemoteException
     */
    public Generic viewMathces(Generic gen) throws RemoteException {
        return Queries.viewMatches(gen, Main.game.getRonda());
    }

    /**
     * Bet on a match
     * @param gen
     * @param lg
     * @return
     * @throws RemoteException
     */
    public Generic bet(Generic gen, Login lg) throws RemoteException {
        //meter a variavel da ronda....
        if(Queries.newBet(gen,lg,Main.game.getRonda()))
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
    public Generic onlineUsers(Generic gen) {
        OnlineUsers list = new OnlineUsers();
        Enumeration<String> temp;

        /*  adds TCP users to the list  */
        temp = Main.onlineUsersTCP.keys();
        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());

        /*  adds RMI users to the list  */
        temp = Main.onlineUsersRMI.keys();
        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());

        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }

    private void message(String fromUser, String toUser, String message) throws RemoteException, IOException {
        /*  creates individual message  */
        Message mes = new Message(toUser, message);
        mes.setAuthor(fromUser);

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);
        gen.setObj(mes);

        /*  checks if the user is online and sends  */
        if(Main.onlineUsersTCP.containsKey(toUser)) {
            ClientThreadTCP sock = Main.onlineUsersTCP.get(toUser);

            try {
                sock.out.writeObject(gen);
            } catch (IOException error) {
                /*  if it throws an error, delete it    */
                Main.onlineUsersTCP.remove(toUser);
            }
        } else if(Main.onlineUsersRMI.containsKey(toUser)) {
            CallbackInterface callback = Main.onlineUsersRMI.get(toUser);
            
            try {
                callback.printMessage(fromUser, message);
            } catch (IOException error) {
                /*  if it throws an error, delete it    */
                Main.onlineUsersRMI.remove(toUser);
            }
        }
        else { /*    or stores to send later accordingly    */
            Queries.setMensagens(fromUser, toUser, message);
            
            System.out.println(toUser+" esta offline");
        }
    }

    /**
     * Messages a single user
     * @param gen
     * @return
     * @throws RemoteException
     */
     public boolean messageUser(Generic gen) throws RemoteException, IOException {
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
    public boolean messageAll(Generic gen) throws RemoteException, IOException {
        String fromUser = null, toUser = null, id = null;
        Vector<String> messageVector, userVector;
        Message mes = (Message) gen.getObj();
        Enumeration<String> keys, onlineEnumeratorTCP, onlineEnumeratorRMI, userEnumerator, message;


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

    public void getMessage(Login lg) throws  RemoteException, IOException {
        Message temp;

        for(temp = Queries.getMensagens(lg.getName()); temp != null; temp = Queries.getMensagens(lg.getName()))
            message(temp.getAuthor(), temp.getTo(), temp.getText());
    }
}
