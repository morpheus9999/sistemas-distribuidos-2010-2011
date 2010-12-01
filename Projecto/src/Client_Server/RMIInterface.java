/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import Client_Server.Generic;
import Client_Server.Login;

/**
 *
 * @author JLA
 */
public interface RMIInterface extends java.rmi.Remote{

    public void setCallback(CallbackInterface callback) throws java.rmi.RemoteException;
    public boolean login(Generic gen) throws java.rmi.RemoteException;
    public boolean logout(Login lg) throws java.rmi.RemoteException;
    public boolean register(Generic gen) throws java.rmi.RemoteException;
    public Generic getCredit(Generic gen, Login log) throws java.rmi.RemoteException;
    public Generic resetCredit(Generic gen, Login log) throws java.rmi.RemoteException;
    public Generic viewMathces(Generic gen) throws java.rmi.RemoteException;
    public Generic bet(Generic gen, Login lg) throws java.rmi.RemoteException;
    public Generic onlineUsers(Generic gen) throws java.rmi.RemoteException;
    public boolean messageUser(Generic gen) throws java.rmi.RemoteException;
    public boolean messageAll(Generic gen) throws java.rmi.RemoteException;
    public void getMessage(Login lg) throws  java.rmi.RemoteException;

    public void setCallbackTomcat(CallbackInterfaceTomcat callbackInterfaceTomcat)throws java.rmi.RemoteException;
}
