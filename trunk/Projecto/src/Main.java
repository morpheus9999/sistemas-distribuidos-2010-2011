/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jojo
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class Main {

    public static void main(String[] args) {

        try{
            ServerSocket listener = new ServerSocket(Constants.SERVERPORTTCP);
            
            while(true) {
                /*  accepts a request of connection */
                Socket clientSocket = listener.accept();

                /*  creates a unique thread for that connection */
                ClientThread client = new ClientThread(clientSocket);
                client.start();

            }

        }catch(IOException  error) {
            System.out.println("ServerSocket listener: " + error.getMessage());

        }
        
    }
}



