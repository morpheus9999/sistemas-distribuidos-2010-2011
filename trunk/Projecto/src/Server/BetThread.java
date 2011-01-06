/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Server.DAOFactoryPattern.ConsistencyDAO;
import BetPackage.BetManager;
import BetPackage.IBetManager;
import Client_Server.Message;
import java.util.Vector;

/**
 *
 * @author jojo
 */
class BetThread extends Thread {

    int numJogos;
    private int ronda;
    int tipo;
    ConsistencyDAO consistencyDAO;
    BetThread(int numJogos) {
        this.numJogos = numJogos;
        tipo = 0;
    }

    BetThread(int numJogos, ConsistencyDAO consistencyDAO) {
        this.numJogos = numJogos;
        tipo = 0;
        this.consistencyDAO=consistencyDAO;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void run() {
        ronda = consistencyDAO.rondaActualConsistency();
        tipo = consistencyDAO.tipoActualConsistency();

        while (true) {
            if (tipo == 0) {
                IBetManager man = new BetManager(numJogos);
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
            } else if (tipo == 1) {
                try {
                    long espera = consistencyDAO.esperaConsistency();
                    System.out.println("ACABOU TEMPO DE APOSTAS PARA A RONDA " + ronda);
                    if (espera > 0) {
                        BetThread.sleep(espera);
                    }
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
                ronda++;
                tipo++;
                consistencyDAO.actualizaConsistency(ronda, 2);
            } else if (tipo == 2) {

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
        }

    }

    public int getRonda() {
        return this.ronda;
    }
}
