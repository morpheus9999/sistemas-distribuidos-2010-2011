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
import java.rmi.RemoteException;
import java.util.Enumeration;

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
        return Queries.login(gen);
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
        Enumeration<String> temp = Main.onlineUsers.keys();

        while(temp.hasMoreElements())
            list.addEntry(temp.nextElement());

        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }

    private void message(String fromUser, String toUser, String message) {
        /*  creates individual message  */
        Message mes = new Message();
        mes.setAuthor(fromUser);
        mes.addEntry(toUser, message);

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);
        gen.setObj(mes);

        /*  checks if the user is online and sends  */
        if(Main.onlineUsers.containsKey(toUser)) {

            // ######################   alterar onlineusers ###################

        }
        else { /*    or stores to send later accordingly    */


            // ###################QUERIES AQUI########################

            System.out.println(toUser+" esta offline");
        }
    }

    /**
     * Messages a single user
     * @param gen
     * @return
     * @throws RemoteException
     */
    public boolean messageUser(Generic gen) throws RemoteException {
        String fromUser = null, toUser = null, message = null;
        Message mes = (Message) gen.getObj();

/*
        System.out.println("From: "+mes.getAuthor());
        System.out.println("To: "+mes.getKeysEnumeration().nextElement());
        System.out.println("Message: "+mes.getEntry(mes.getKeysEnumeration().nextElement()));
*/
        /*  runs through the received buffer and sends/stores messages  */
        Enumeration<String> enumerator = mes.getKeysEnumeration();
        fromUser = mes.getAuthor();

        while(enumerator.hasMoreElements()) {
            toUser = enumerator.nextElement();
            message = mes.getEntry(toUser);

            this.message(fromUser, toUser, message);
        }

        return true;
    }

    public boolean messageAll(Generic gen) throws RemoteException {
        String fromUser = null, toUser = null, message = null;
        Message mes = (Message) gen.getObj();

/*
        System.out.println("From: "+mes.getAuthor());
        System.out.println("Message: "+mes.getEntry(mes.getKeysEnumeration().nextElement()));
*/

        /*
         *  runs through the received buffer and sends/stores messages
         *  to all people registered
         */
        Enumeration<String> enumerator = mes.getKeysEnumeration();
        fromUser = mes.getAuthor();

        while(enumerator.hasMoreElements()) {
            /*
             *  since message is for all
             *  the key doesn't matter, only for obtaining each message
             */
            toUser = enumerator.nextElement();
            message = mes.getEntry(toUser);

            /*  first send to online users  */
            Enumeration<String> onlineEnumerator = Main.onlineUsers.keys();
            while(onlineEnumerator.hasMoreElements()) {
                toUser = onlineEnumerator.nextElement();

                this.message(fromUser, toUser, message);
            }

            /*  after that it stores in database
             *  to send later to the rest of the users
             */
            //  ###################QUERIES AQUI########################
            //  nao esquecer da proteccao de dados (nao guardar para utilizadores que estejam online e recebido)
        }


        return true;
    }
}
