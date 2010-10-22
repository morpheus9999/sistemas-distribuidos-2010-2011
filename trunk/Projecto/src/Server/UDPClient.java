/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

/**
 *
 * @author jojo
 */
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class UDPClient{
    public static void main(String args[]){
        DatagramSocket bSocket = null;
        DatagramSocket aSocket = null;
        int estado=0;
        int x=0;
        String texto = "";
        String s;


        try {
            aSocket = new DatagramSocket();
            bSocket = new DatagramSocket(6790);
            InetAddress aHost = InetAddress.getByName("localhost");

            while(true){
                try{
                    if(estado == 0)
                        texto = "IN ALIVE";
                    else
                        texto = "IM MASTER";

                } catch(Exception e){
                }

                /*  a timeout before sending/receiving new packets  */
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }

                /*  envia packet    */
                byte [] m = texto.getBytes();
                DatagramPacket request = new DatagramPacket(m,m.length,aHost,6789);
                aSocket.send(request);

                System.out.println("ENVIA:"+texto);

                byte [] buffer = new byte[1000];
                DatagramPacket request2 = new DatagramPacket(buffer, buffer.length);
                try{
                    /*  receive packet   */
                    bSocket.setSoTimeout(3000);
                    bSocket.receive(request2);
                    s = new String(request2.getData(), 0, request2.getLength());

                    System.out.println("Server Recebeu: " + s);

                    /*  check to take master/slave position */
                    if(s.contains("IM MASTER"))
                        estado = 0;
                    else {
                        if(x != 0)
                            estado = 1;
                        x = 1;
                    }
                }catch (IOException e){
                    System.out.println("IO: " + e.getMessage());
                    estado = 1;
                }
            }
        }   catch (SocketException e)   {
            System.out.println("Socket: " + e.getMessage());
        }   catch (IOException e)   {
            System.out.println("IO: " + e.getMessage());
        }   finally {
            if(aSocket != null)
                aSocket.close();
        }
    }
}