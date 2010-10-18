/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.RMIInterface;

/**
 *
 * @author JLA
 */
public class RMIMethods extends java.rmi.server.UnicastRemoteObject implements RMIInterface {

    public RMIMethods() throws java.rmi.RemoteException {

    }

    /**
     * Login
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean login(Generic gen) throws java.rmi.RemoteException {
        return Queries.login(gen);
    }

    /**
     * Register
     * @param gen
     * @return true if sucessfull/false otherwise
     * @throws java.rmi.RemoteException
     */
    public boolean register(Generic gen) throws java.rmi.RemoteException {
        return Queries.register(gen);
    }

    /**
     * getCredit
     * @param gen - a generic object that will store the solution
     * @param log - a object containing info about the user
     * @return gen - the solution to the problem
     * @throws java.rmi.RemoteException
     */
    public Generic getCredit(Generic gen, Login log) throws java.rmi.RemoteException {
        return Queries.getCredit(gen, log);
    }

    
}
