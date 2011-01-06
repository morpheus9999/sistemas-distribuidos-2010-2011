package ClientRMI2.StatePattern;

import ClientRMI2.Client;
import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;

public class ClientOffline implements ClientState {

    public void constructUserMessage(Message m)
            throws ForbiddenActionException {

        throw new ForbiddenActionException();
    }

    public void constructLoginMessage(Login m)
            throws ForbiddenActionException {

        Login lg = new Login();
        lg.setName(m.getName());
        lg.setPassword(m.getPassword());
        Client.log.setName(m.getName());
        Client.log.setPassword(m.getPassword());
        Client.gen = new Generic();
        Client.gen.setObj(lg);
        Client.gen.setConfirmation(false);
        Client.opt.setOption(Constants.loginCode);

    }

    public void constructRegisterMessage(User m)
            throws ForbiddenActionException {


        Client.gen = new Generic();
        Client.gen.setObj(m);
        Client.gen.setConfirmation(false);
        Client.opt.setOption(Constants.regCode);


    }

    public void constructCreditMessage(Login m)
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructUserMessageAll(Message messageAllUsers)
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructResetCredit(Login log)
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructViewMatches() 
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructBet(Bet bet) 
            throws ForbiddenActionException {

        throw new ForbiddenActionException();
        
        
    }
}
