/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.TemplatePattern;

import BetPackage.BetManager;
import BetPackage.IBetManager;
import Client_Server.Message;
import Server.DAOFactoryPattern.ConsistencyDAO;
import Server.Main;
import java.util.Vector;
import Server.ClientThreadTCP;
/**
 *
 * @author jojo
 */
public class Football extends Game {

    public int numGames;
    public int ronda;
    public int tipo;
    public ConsistencyDAO consistencyDAO;

    public Football(int numGames, ConsistencyDAO consistencyDAO) {
        this.numGames = numGames;
        this.consistencyDAO = consistencyDAO;
    }

    @Override
    public void newRoundOfGames() {
        IBetManager man = new BetManager(numGames);
        //System.out.println("ENTRA1");
        consistencyDAO.NewRoundConsistency(man, ronda);

        tipo++;
        consistencyDAO.actualizaConsistency(ronda, 1);


        if (Main.calbackInterfaceTomcat != null) {
            try {
                //Main.calbackInterfaceTomcat.printMatches(Queries.viewMatches(new Generic(), ronda));
                Main.calbackInterfaceTomcat.UpdateMatchs();
            } catch (Exception ex) {
                System.out.println("erro callback");
            }

        }

    }

    @Override
    public void waitEndRound() {
        try {
            long espera = consistencyDAO.esperaConsistency();
            System.out.println("ACABOU TEMPO DE APOSTAS PARA A RONDA " + ronda);
            if (espera > 0) {
                Thread.sleep(espera);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        ronda++;
        tipo++;
        consistencyDAO.actualizaConsistency(ronda, 2);

    }

    @Override
    public void endOfRoundGames() {
        //Hashtable<String, Generic> envia = new Hashtable();
        System.out.println("Actualiza valores das bets....");
        Vector<Message> m = consistencyDAO.updateBetsConsistency(ronda - 1);
        ClientThreadTCP tcp;
        
        if (m != null) {
            for (int k = 0; k < m.size(); k++) {

                tcp = (ClientThreadTCP) (Main.onlineUsersTCP.get(m.elementAt(k).getAuthor()));
                tcp.messageUser("", m.elementAt(k).getAuthor(), m.elementAt(k).getText());
                if (Main.calbackInterfaceTomcat != null) {
                    try {
                        //Main.calbackInterfaceTomcat.printMatches(Queries.viewMatches(new Generic(), ronda));
                        Main.calbackInterfaceTomcat.UpdateCredit(m.elementAt(k).getAuthor());
                    } catch (Exception ex) {
                        System.out.println("erro callback");
                    }

                }

            }
            //DELETE jogos
            consistencyDAO.actualizaConsistency(ronda, 0);

            //Delete apostas


        }
        tipo = 0;

    }

    @Override
    public void printWinner() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRonda() {
        return ronda;
    
    }

    @Override
    public void getInitialStats() {
        ronda = consistencyDAO.rondaActualConsistency();
        tipo = consistencyDAO.tipoActualConsistency();
    
    }
}
