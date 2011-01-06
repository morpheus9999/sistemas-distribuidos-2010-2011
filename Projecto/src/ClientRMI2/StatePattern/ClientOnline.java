package ClientRMI2.StatePattern;

import ClientRMI2.Client;
import Client_Server.Bet;
import Client_Server.Constants;
import Client_Server.Credit;
import Client_Server.Generic;
import Client_Server.Login;
import Client_Server.Message;
import Client_Server.User;

public class ClientOnline implements ClientState {

    public void constructUserMessage(Message message)
            throws ForbiddenActionException {

        Client.gen = new Generic();
        Client.gen.setObj(message);
        Client.gen.setConfirmation(false);
        Client.opt.setOption(Constants.messageCode);
        System.out.println("ENTRAONLINE");



    }

    public void constructUserMessageAll(Message messageAllUsers)
            throws ForbiddenActionException {
        Client.gen = new Generic();
        Client.gen.setObj(messageAllUsers);
        Client.gen.setConfirmation(false);
        Client.opt.setOption(Constants.messageAllCode);
    }

    public void constructLoginMessage(Login m)
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructRegisterMessage(User m)
            throws ForbiddenActionException {
        throw new ForbiddenActionException();
    }

    public void constructCreditMessage(Login m) throws ForbiddenActionException {
        Client.gen.setObj(new Credit());
        Client.opt.setOption(Constants.creditCode);
    }

    public void constructResetCredit(Login log) throws ForbiddenActionException {

        Client.gen.setObj(new Credit());
        Client.opt.setOption(Constants.resetCode);


    }

    public void constructViewMatches() 
            throws ForbiddenActionException {
                        Client.opt.setOption(Constants.matchesCode);
    }

    public void constructBet(Bet bet)
            throws ForbiddenActionException {
                
                Client.gen.setCode(Constants.betCode);
        Client.gen.setObj(bet);
        Client.opt.setOption(Constants.betCode);
    }
}
