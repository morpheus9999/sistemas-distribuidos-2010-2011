/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.DAOFactoryPattern;

/**
 *
 * @author jojo
 */
// CloudscapeCustomerDAO implementation of the 
// CustomerDAO interface. This class can contain all
// Cloudscape specific code and SQL statements. 
// The client is thus shielded from knowing 
// these implementation details.

import Client_Server.Generic;
import Client_Server.Login;
import java.sql.*;
import java.util.Collection;
import java.util.Vector;
import javax.sql.RowSet;

public class CloudscapeCustomerDAO implements CustomerDAO {

    public Generic viewMatchesCustomer(Generic temp, int ronda) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean newBetCustomer(Generic generic, Login lg, int ronda) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
  
  
  
}