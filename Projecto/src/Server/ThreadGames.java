/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Server.DAOFactoryPattern.ConsistencyDAO;
import Server.TemplatePattern.*;
/**
 *
 * @author jojo
 */
public class ThreadGames extends Thread {
    int numJogos;
    
    int tipo;
    ConsistencyDAO consistencyDAO;
    Football football;
    ThreadGames(int numJogos, ConsistencyDAO consistencyDAO) {
        this.numJogos = numJogos;
        tipo = 0;
        this.consistencyDAO=consistencyDAO;
        football =new Football( numJogos, consistencyDAO);
        
        
        //throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void run() {
        football.playGame();
        
    }

}
