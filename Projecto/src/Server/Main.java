/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Login;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author JLA
 */
public class Main {

    private static OutputStream outStream;
    private static InputStream inStream;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;


    public static void main(String args[]) {
        try {
            /*  opens a port to check for requests  */
            ServerSocket listener = new ServerSocket(Constants.serverPort);
            
            Socket sock = listener.accept();

            //  outputStreams
            outStream = sock.getOutputStream();
            out = new ObjectOutputStream(outStream);

            //  inputStreams
            inStream = sock.getInputStream();
            in = new ObjectInputStream(inStream);

            

            Login lg = (Login) in.readObject();

            System.out.println("Login enviado pelo cliente:");
            System.out.println("name: "+lg.getName());
            System.out.println("password: "+lg.getPassword());

            out.writeUTF("login recebido");
            out.flush();
            
            System.out.println("confirmacao enviada");

            Credit teste = new Credit();
            teste.setCredit(200);

            System.out.println("valor do objecto generico: ");

        } catch (ClassNotFoundException ex) {
            System.out.println("nao encontra classe");
        } catch (IOException ex) {
            System.out.println("io exception");
        }


    }
}
