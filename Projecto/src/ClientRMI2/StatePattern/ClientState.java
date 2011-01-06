
package ClientRMI2.StatePattern;

import Client_Server.Bet;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;

public interface ClientState {

    public void constructUserMessage(Message m) 
            throws ForbiddenActionException;
    
    public void constructLoginMessage(Login m) 
            throws ForbiddenActionException;
    
    public void constructRegisterMessage(User m) 
            throws ForbiddenActionException;
    
    public void constructCreditMessage(Login m)
            throws ForbiddenActionException;

    public void constructUserMessageAll(Message messageAllUsers)
            throws ForbiddenActionException;

    public void constructResetCredit(Login log)
            throws ForbiddenActionException;

    public void constructViewMatches()
            throws ForbiddenActionException;

    public void constructBet(Bet bet)
            throws ForbiddenActionException;


}
