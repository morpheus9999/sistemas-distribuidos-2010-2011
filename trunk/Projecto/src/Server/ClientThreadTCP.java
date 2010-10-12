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
import java.net.SocketException;
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
    private boolean stop;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private OutputStream outStream;
    private InputStream inStream;

    ClientThreadTCP(Socket clientSocket) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void run() {

        while (!stop) {
            if (socket == null) {
                ;
            } else {
                try {
                    executa();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        
    }
    private void executa() {

        try {
            this.socket.setTcpNoDelay(true);

        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
            in = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            DataInputStream ids = new DataInputStream(this.socket.getInputStream());
            DataOutputStream ods = new DataOutputStream(this.socket.getOutputStream());

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        while (!stop) {

            try {
                Object obj = in.readObject();
                System.out.println("lê objecto");
                Generic gen = (Generic) in.readObject();
                gen.getCode();
                
                System.out.println("faz query");
                Boolean teste = Queries.login(gen);

                System.out.println("envia resposta");
                Generic envia =new Generic(100);
                envia.setConfirmation(true);
                out.writeObject(envia);
            } catch (IOException e) {
                try {
                    socket.close();
                    in.close();
                    out.close();
                    stop = true;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }

        }

    }

    private void envia(){


        

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
