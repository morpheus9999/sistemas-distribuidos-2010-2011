package Server;


import Client_Server.Generic;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
class ClientThreadTCP {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private OutputStream outStream;
    private InputStream inStream;

    ClientThreadTCP(Socket clientSocket) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void run() {
        try {
            /*  outputStreams   */
            
            out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            /*  inputStreams    */
            
            in = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            


            while(true) {
                out.writeObject(Queries.login((Generic) in.readObject()));
                out.flush();
                
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThreadTCP.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ClientThreadTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closed() {
        try {
            socket.close();
            in.close();
            out.close();
            // acabar o run

        } catch (IOException e) {
            System.err.println("[TwitterTCP-exectuta]:Erro ao fechar streams");
            
        }
    }

}
