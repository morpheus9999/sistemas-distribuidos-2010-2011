package Server;


import Client_Server.Generic;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jorge
 */
class ClientThread {

    private Socket sock;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private OutputStream outStream;
    private InputStream inStream;

    ClientThread(Socket clientSocket) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void run() {
        try {
            /*  outputStreams   */
            outStream = this.sock.getOutputStream();
            out = new ObjectOutputStream(outStream);
            /*  inputStreams    */
            inStream = this.sock.getInputStream();
            in = new ObjectInputStream(inStream);

            while(true) {
                out.writeObject(Queries.login((Generic) in.readObject()));
                out.flush();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
