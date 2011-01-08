/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

import Client_Server.Generic;
import Client_Server.Login;
import java.util.Vector;

/**
 *
 * @author jojo
 */
class CloudscapeAccountDAO implements AccountDAO{

    public CloudscapeAccountDAO() {
    }

    public boolean insertAccount(Generic generic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean loginAccount(Generic generic) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Generic getCreditAccount(Generic gen, Login lg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Vector<String> getUsernameListAccount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Generic resetCreditAccount(Generic temp, Login lg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
