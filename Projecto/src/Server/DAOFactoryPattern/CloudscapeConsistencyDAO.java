/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import BetPackage.IBetManager;
import Client_Server.Message;
import java.util.Vector;

/**
 *
 * @author jojo
 */
class CloudscapeConsistencyDAO implements ConsistencyDAO{

    public CloudscapeConsistencyDAO() {
    }

    public int rondaActualConsistency() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int tipoActualConsistency() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actualizaConsistency(int idRonda, int tipo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public long esperaConsistency() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean NewRoundConsistency(IBetManager man, int ronda) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector<Message> updateBetsConsistency(int ronda) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    

}
