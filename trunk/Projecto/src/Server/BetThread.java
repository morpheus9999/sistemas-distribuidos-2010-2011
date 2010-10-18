/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import BetPackage.BetManager;
import BetPackage.IBetManager;
import Client_Server.Generic;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jojo
 */
 
class BetThread extends Thread{

    int numJogos;
    private int ronda;
    BetThread(int numJogos) {
        this.numJogos = numJogos;
        
    }


    public void run() {
         ronda=0;

        while(true){
            IBetManager man = new BetManager(numJogos);
            Queries.NewRound(man, ronda);
            try {
                BetThread.sleep(100000);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            ronda++;

            //Hashtable<String, Generic> envia = new Hashtable();
            System.out.println("Actualiza valores das bets....");
            Queries.updateBets(ronda-1);




        }
        
    }
    public int getRonda(){
        return this.ronda;
    }

    
}
