package Server;


import Client_Server.CallbackInterface;
import Client_Server.CallbackInterfaceTomcat;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.OnlineUsers;
import Server.IteratorPattern.RMI;
import Server.IteratorPattern.RMITomcat;
import Server.IteratorPattern.TCP;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */
public class ClientThreadTCP extends Thread{
    private boolean logout;
    private Socket socket;

    private Login lg = null;                   //  stores info about the logged user

    public ObjectOutputStream out;      //  streams made public so other threads can send annoucements
    public ObjectInputStream in;

    ClientThreadTCP(Socket clientSocket) {
        this.socket = clientSocket;
        this.logout = false;
    }

    /**
     * Thread main method
     */
    public void run() {
        Generic gen, temp;

        /*  opens comunication channels */
        this.openComChannels();

        try {
            while (!this.logout) {
                try {
                    /*  receives request from client    */
                    gen = (Generic) this.in.readObject();

                    /*  creates a new object with the received info to send to the client   */
                    temp = new Generic();
                    temp.setCode(gen.getCode());
                    temp.setObj(gen.getObj());
                    
                    /*  parses received object  */
                    switch (temp.getCode()) {
                        case Constants.creditCode:
                            temp = this.getCredito(temp);
                            break;
                        case Constants.resetCode:
                            temp = this.resetCredit(temp);
                            break;
                        case Constants.matchesCode:
                            temp = this.viewMatches(temp);
                            break;
                        case Constants.betCode:
                            temp = this.bet(temp);
                            break;
                        case Constants.onlineUsersCode:
                            temp = this.onlineUsers(gen);
                            break;
                        case Constants.messageCode:
                            temp = this.message(temp);
                            break;
                        case Constants.messageAllCode:
                            temp = this.messageAll(temp);
                            break;
                        case Constants.logoutCode:
                            temp = this.logout(temp);
                            break;
                        case Constants.loginCode:
                            temp = this.login(temp);
                            break;
                        case Constants.regCode:
                            temp = this.register(temp);
                            break;
                        case Constants.requestMessage:
                            temp = this.requestMessage(temp);
                            break;
                        default:
                            break;
                    }

                    /*  sends confirmation of the action to the client  */
                    this.out.writeObject(temp);
                    this.out.flush();
                } catch (ClassNotFoundException ex) {
                    //System.out.println("Class unindentified!");
                }
            }
        } catch  (IOException ex) {
            System.out.println("Error receiving/sending generic object!");
        }

        /*  removes user from onlineUsersTCP list  */
        if(lg != null)
            Main.onlineUsersTCP.remove(lg.getName());

        /*  close comunication channels */
        this.closeComChannels();
    }


    /**
     * open comunication channels
     */
    private void openComChannels() {
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
            this.in = new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException ex) {
            System.out.println("Error opening comunication channels: "+ex.getMessage());
        }
    }

    /**
     * close comunication channels
     */
    private void closeComChannels() {
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
        } catch (IOException ex) {
            System.out.println("Error closing comunication channels: "+ex.getMessage());
        }
    }

    /**
     * Login
     * */
    private  Generic login(Generic gen) throws IOException {
        /*  faz query   */
       if(Main.accountDAO.loginAccount(gen)) {
            /*  sets user is logged  */
            gen.setConfirmation(true);
            lg = (Login) gen.getObj();
            
            
            if(Main.onlineUsers.containsKey(lg.getName())){
                gen.setConfirmation(false);
                return gen;
            }else{
                
                Main.onlineUsers.put(lg.getName(), new TCP(this));
                
            }
            
            
//            
//            
//            if(Main.onlineUsersTCP.containsKey(lg.getName()))
//                gen.setConfirmation(false);
//            else if(Main.onlineUsersRMI.containsKey(lg.getName()))
//                gen.setConfirmation(false);
//            else if(Main.onlineUsersRMITomcat.contains(lg.getName()))
//                gen.setConfirmation(false);
//            else
//                Main.onlineUsersTCP.put(this.lg.getName(), this);
//            
            
            
            
            if(Main.calbackInterfaceTomcat!=null){
            
           
            
            Main.calbackInterfaceTomcat.UpdateUsersOnline();
            
            }
        }
        else
            gen.setConfirmation(false);

        return gen;
    }

    /**
     * Logout
     */
    private Generic logout(Generic gen) throws IOException {
        /*  sends confirmation of session ending    */
        gen.setConfirmation(true);
        /*  exits thread    */
        this.logout = true;
        Main.onlineUsers.remove(((Login)gen.getObj()).getName());
        
        if (Main.calbackInterfaceTomcat != null) {
            Main.calbackInterfaceTomcat.UpdateUsersOnline();
        }

        return gen;
    }

    /**
     * Register
     */
    private Generic register(Generic gen) throws IOException {
        if(Main.accountDAO.insertAccount(gen))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(false);

        return gen;
    }

     /**
     * Message a user
     * */
    public static void messageUser(String fromUser, String toUser, String message) {
        /*  creates individual message  */
        System.out.println("ENTRA NO TCP PARA ENVIAR MSG"+toUser);
        Message mes = new Message(toUser, message);
        mes.setAuthor(fromUser);

        /*  wrapes in a generic object  */
        Generic gen = new Generic();
        gen.setCode(Constants.receiveMessage);
        gen.setConfirmation(true);
        gen.setObj(mes);
        
        /*  checks if the user is online and sends  */
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
    }

    /**
     * Message to user
     * */
    private Generic message(Generic gen) throws IOException {
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
                messageUser(fromUser, toUser, message.nextElement());
        }

        gen.setConfirmation(true);

        return gen;
    }

    /**
     * Message to all users
     * */
    private Generic messageAll(Generic gen) throws IOException {
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
                    ClientThreadTCP.messageUser(fromUser, toUser, message.nextElement());
            }
        }
        
        gen.setConfirmation(true);

        return gen;
    }

    /**
     *
     * */
    private Generic onlineUsers(Generic gen) throws IOException {
        OnlineUsers list = new OnlineUsers();
        Enumeration<String> temp;

        Iterator<String> tempo = Main.onlineUsers.keySet().iterator();
        
        while(tempo.hasNext())
            list.addEntry(tempo.next());
        
        
        gen.setConfirmation(true);
        gen.setObj(list);

        return gen;
    }

    private Generic bet(Generic gen) {
        //meter a variavel da ronda....
        if(Main.customerDAO.newBetCustomer(gen,lg,Main.game.getRondaFootball()))
            gen.setConfirmation(true);
        else
            gen.setConfirmation(false);

        return gen;
    }

    private Generic getCredito(Generic temp) {
        return Main.accountDAO.getCreditAccount(temp, lg);

    }

    private Generic resetCredit(Generic temp) {
        return Main.accountDAO.resetCreditAccount(temp,lg);
    }

    private Generic viewMatches(Generic temp) {
        return Main.customerDAO.viewMatchesCustomer(temp,Main.game.getRondaFootball());
    }

    private Generic requestMessage(Generic temp) throws IOException {
        Message mes;

        for(mes = Main.accountDAO.getMensagensAccount(lg.getName()); mes != null; mes = Main.accountDAO.getMensagensAccount(lg.getName()))
            ClientThreadTCP.messageUser(mes.getAuthor(), mes.getTo(), mes.getText());

        temp.setCode(Constants.receiveMessage);
        temp.setConfirmation(true);

        return temp;
    }
    
}