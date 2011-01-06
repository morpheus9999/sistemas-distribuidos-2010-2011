/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server.TemplatePattern;

/**
 *
 * @author jojo
 */
abstract class Game {
    
    
    abstract void getInitialStats();
    abstract void newRoundOfGames();
    abstract void waitEndRound();
    abstract void endOfRoundGames();
    abstract void printWinner();
    abstract int getRonda();
    
    public final void playGame() {
        
        
        getInitialStats();
        while(true){
            newRoundOfGames();
            waitEndRound();
            endOfRoundGames();
            printWinner();
      
        }
    }
    
    

}
