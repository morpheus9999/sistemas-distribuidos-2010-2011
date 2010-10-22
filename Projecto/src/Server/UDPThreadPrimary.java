/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author JLA
 */
public class UDPThreadPrimary extends Thread {


    public void run() {
        int estado = 0;
        DatagramSocket bSocket = null;
        DatagramSocket aSocket = null;
        String texto;
        String s;


        try{
            /*  opens channels  */
            bSocket = new DatagramSocket();
            aSocket = new DatagramSocket(6789);
            InetAddress bHost = InetAddress.getByName("localhost");

            //System.out.println("Socket Datagram ‡ escuta no porto 6789");

            while(true){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }

                if(estado == 0)
                    texto = "IM SLAVE";
                else
                    texto = "IM MASTER";

                /*  envia packet    */
                byte [] m = texto.getBytes();
                DatagramPacket request2 = new DatagramPacket(m,m.length,bHost,6790);
                bSocket.send(request2);

                //System.out.println("ENVIA: "+texto);

                byte[] buffer = new byte[1000];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                try{
                    /*  receive packet  */
                    aSocket.setSoTimeout(3000);
                    aSocket.receive(request);
                    s = new String(request.getData(), 0, request.getLength());

                   // System.out.println("Server Recebeu: " + s);

                    /*  check to take master/slave position */
                    if(s.contentEquals("IM MASTER")) {
                        estado = 0;
                    } else {
                        estado = 1;
                        /*  tells main to continue execution    */
                        Main.opt.setOption(1);
                    }

                }catch (IOException e){
                    //System.out.println("IO: " + e.getMessage());
                    estado = 1;
                    /*  tells main to continue execution    */
                    
                    Main.opt.setOption(1);
                }
            }
        }   catch (SocketException e){
            System.out.println("Socket: " + e.getMessage());
        }   catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }   finally {
            if(aSocket != null)
                aSocket.close();
        }
    }
}
