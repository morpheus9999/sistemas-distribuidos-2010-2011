/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import BetPackage.IBetManager;
import Client_Server.Message;
import java.util.Observable;
import java.util.Vector;

/**
 *
 * @author jojo
 */
public interface ConsistencyDAO{
    
    public int rondaActualConsistency();
    public int tipoActualConsistency();
    public void actualizaConsistency(int idRonda,int tipo);
    public long esperaConsistency();
    public boolean NewRoundConsistency(IBetManager man, int ronda);
    public Vector<Message> updateBetsConsistency(int ronda) ;
}
