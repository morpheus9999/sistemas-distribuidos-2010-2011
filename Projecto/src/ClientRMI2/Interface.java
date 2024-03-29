/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2;

import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Input;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 *
 * @author JLA
 */
public class Interface {

    public static Message buffer = new Message();
    public static Message bufferAll = new Message();

    /**
     * Welcome menu
     */
    public int welcomeMenu() {
        int opt = -1;
        int passagens = 0;
        
        
        do {
            /*  checks if it's not the first time asking for input  */
            if(++passagens > 1)
                System.out.println("Wrong input");

            System.out.println("#### Welcome ####");
            System.out.println("1 - Login");
            System.out.println("2 - Register");

            System.out.print("Option: ");
            opt = Input.readInt();


        } while (opt < 1 || opt > 2);

        if (opt == 1)
            opt = Constants.loginCode;
        else
            opt = Constants.regCode;

        return opt;
    }

    /**
     * Main menu
     */
    public int mainMenu() {
        int opt = -1;

        do {
            System.out.println("#### Main Menu ####");
            System.out.println("1 - Credit");
            System.out.println("2 - Reset Credit");
            System.out.println("3 - Current Matches");
            System.out.println("4 - Bet");
            System.out.println("5 - Online Users");
            System.out.println("6 - Message User");
            System.out.println("7 - Message All");
            System.out.println("8 - Logout");

            System.out.print("Option: ");
            opt = Input.readInt();
        } while (opt < 1 || opt > 8);

        return opt;
    }

    /**
     * Offline Menu
     */
    public int offlineMenu() {
        int opt = -1;

        do {
            System.out.println("#### Offline Menu ####");
            System.out.println("6 - Message User");
            System.out.println("7 - Message All");
            System.out.println("8 - Logout");

            System.out.print("Option: ");
            opt = Input.readInt();
        } while (opt < 6 || opt > 8);

        return opt;
    }

    /**
     * Login
     * @return a generic structure with login data
     */
    public Login login() {
        Generic gen = new Generic();
        Login lg = new Login();
        String temp;

        System.out.println("#### Login ####");

        /*  get username    */
        System.out.print("Username: ");
        temp = Input.readString().trim();
        lg.setName(temp);
//        Client.log.setName(temp);

        /*  get password    */
        System.out.print("Password: ");
        temp = Input.readString().trim();
        lg.setPassword(temp);
//        Client.log.setPassword(temp);
//        
//        gen.setObj(lg);
//        gen.setConfirmation(false);
//        
        return lg;
    }

    /**
     * Register menu (????precisara de alteracoes????)
     */
    public User register() {
        String temp;
        StringTokenizer token;
        User client = new User();
        Generic gen = new Generic();
        
        System.out.println("#### Register ####");

        while (true) {
            System.out.print("Introduce your email: ");

            try {
                temp = Input.readString().trim();
                /*  check if string is a email  */
                token = new StringTokenizer(temp);
                token.nextToken("@");
                token.nextToken(".");
                token.nextToken("\n");

                /*  adds mail to the object */
                client.setMail(temp);
                break;
            }
            catch (NoSuchElementException error) {
                System.out.println("String inserted is not a email");
            }
        }

        System.out.print("Introduce you username: ");
        temp = Input.readString().trim();
        client.setName(temp);

        System.out.print("Introduce your password: ");
        temp = Input.readString().trim();
        client.setPassword(temp);
        gen.setObj(client);
        gen.setConfirmation(false);
        return client;
    }

    /**
     * Bet
     * lets the user bet on any match
     */
    public Bet bet() {
        Generic gen = new Generic();
        Bet be = new Bet();
        
        System.out.println("#### Bet ####");
        System.out.print("ID Jogo: ");
        int idJogo = Input.readInt();
        System.out.print("Victoria Casa (1) Victoria fora (2) Empate (0): ");
        int aposta =Input.readInt();
        be.setAposta(aposta);
        //System.out.print("Quantos creditos pretende apostar: ");
        int bet=-1;
        while(bet<0){
            System.out.print("Quantos creditos pretende apostar: ");
            bet=Input.readInt();

        }
        be.setIdGame(idJogo);
        be.setBet(bet);

        
        
        return be;
    }

    /**
     * Messages a single user screen
     * @return
     */
    public Message messageSingleUser(Login lg) {
        String message = null;
        String toUser = null;
        Message mes = new Message();
        Generic gen = new Generic();

        System.out.println("#### Message to user ####");
        System.out.print("To: ");
        toUser = Input.readString();
        System.out.println("Message: ");
        System.out.print("> ");
        message = Input.readString();

        mes.setAuthor(lg.getName());
        mes.addEntry(toUser, message);

        gen.setCode(Constants.messageCode);
        gen.setObj(mes);

        /*  guarda no buffer a mensagem */
        Interface.buffer.setAuthor(lg.getName());
        Interface.buffer.addEntry(toUser, message);

        return mes;

    }

    /**
     * Messages all users screen
     */
    public Message messageAllUsers(Login lg) {
        String message;
        Generic gen = new Generic();
        Message mes = new Message();

        System.out.println("#### Message to all users ####");
        System.out.println("Message: ");
        System.out.print("> ");
        message = Input.readString();

        mes.setAuthor(lg.getName());
        mes.addEntry(Integer.toString(mes.getHashtable().size()).toString(), message);

        gen.setCode(Constants.messageAllCode);
        gen.setObj(mes);

        /*  guarda no buffer a mensagem */
        Interface.bufferAll.setAuthor(lg.getName());
        Interface.bufferAll.addEntry(Integer.toString(Interface.bufferAll.getHashtable().size()), message);

        return mes;
    }

    /**
     * Prepares to send the stored buffer
     * @return generic object
     */
    public static Generic messageSingleBuffer() {
        Generic gen = null;
        Message mes = new Message();

        if(Interface.buffer.getSize() > 0) {
            gen = new Generic();
            gen.setCode(Constants.messageCode);
            mes.setAuthor(Interface.buffer.getAuthor());
            mes.setHashtable(Interface.buffer.getHashtable());
            gen.setObj(mes);
            
            /*
             *  this is done because evoking message method
             *  will store the messages as backup until they are delivered
             */
            Interface.buffer.clearHashtable();
        }
        

        return gen;
    }

    /**
     * Prepares to send the stored buffer
     * @return generic object
     */
    public static Generic messageAllBuffer() {
        Generic gen = null;
        Message mes = new Message();

        if(Interface.bufferAll.getSize() > 0) {
            gen = new Generic();
            gen.setCode(Constants.messageAllCode);
            mes.setAuthor(Interface.bufferAll.getAuthor());
            mes.setHashtable(Interface.bufferAll.getHashtable());
            gen.setObj(mes);
            
            /*
             *  this is done because evoking message method
             *  will store the messages as backup until they are delivered
             */
            Interface.bufferAll.clearHashtable();
        }

        return gen;
    }
}
