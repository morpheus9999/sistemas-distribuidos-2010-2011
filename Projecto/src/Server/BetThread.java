/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import BetPackage.BetManager;
import BetPackage.IBetManager;
import Client_Server.Generic;
import Client_Server.Message;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojo
 */
 
class BetThread extends Thread{

    int numJogos;
    private int ronda;
    int tipo;
    BetThread(int numJogos) {
        this.numJogos = numJogos;
        tipo=0;
    }


    public void run() {
         ronda=Queries.rondaActual();
         tipo=Queries.tipoActual();

        while(true){
            if(tipo==0){
            IBetManager man = new BetManager(numJogos);
             System.out.println("ENTRA1");
            Queries.NewRound(man, ronda);
            tipo++;
            Queries.actualiza(ronda, 1);

            }else if(tipo==1){
                try {
                    long espera =Queries.espera();
                    System.out.println("ENTRA2"+espera);
                    if(espera>0)
                        BetThread.sleep(espera);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                ronda++;
                tipo++;
            Queries.actualiza(ronda, 2);
            }else if(tipo==2){
            
            //Hashtable<String, Generic> envia = new Hashtable();
            System.out.println("Actualiza valores das bets....");
            Vector<Message> m =Queries.updateBets(ronda-1);



            ClientThreadTCP tcp = (ClientThreadTCP)(Main.onlineUsers.get("Jorge"));
                try {
                    tcp.messageUser("Admin", "Jorge", "Ganhou a aposta!");
                } catch (IOException ex) {
                    Logger.getLogger(BetThread.class.getName()).log(Level.SEVERE, null, ex);
                }


            tipo=0;
            //DELETE jogos
            Queries.actualiza(ronda, 0);
            //Delete apostas

            }
        }
        
    }
    public int getRonda(){
        return this.ronda;
    }

    
}
