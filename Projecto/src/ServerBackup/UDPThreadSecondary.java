/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ServerBackup;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 *
 * @author JLA
 */
public class UDPThreadSecondary extends Thread {

    public void run() {
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
                        texto = "IM SLAVE";
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

               // System.out.println("ENVIA:"+texto);

                byte [] buffer = new byte[1000];
                DatagramPacket request2 = new DatagramPacket(buffer, buffer.length);
                try{
                    /*  receive packet   */
                    bSocket.setSoTimeout(3000);
                    bSocket.receive(request2);
                    s = new String(request2.getData(), 0, request2.getLength());

                   //System.out.println("Server Recebeu: " + s);

                    /*  check to take master/slave position */
                    if(s.contains("IM MASTER"))
                        estado = 0;
                    else {
                        /*
                         *  since it's the secondary server
                         *  it only assumes master on the second run if possible
                         */
                        if(x != 0) {
                            estado = 1;
                            /*  tells main to continue execution    */
                            Main.opt.setOption(1);
                        }
                        x = 1;
                    }
                }catch (IOException e){
                    //System.out.println("IO: " + e.getMessage());
                    estado = 1;
                    /*  tells main to continue execution    */
                    Main.opt.setOption(1);
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
