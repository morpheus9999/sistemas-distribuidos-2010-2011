/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.Serializable;

/**
 *
 * @author jojo
 */
public class Bet implements Serializable{
    
    
    private int bet;
    private int aposta;
    private int id_game;

    
    
    public Bet() {
        
        this.bet = 0;
        this.aposta=0;
        this.id_game=0;

    }
    public Bet(int bet, int aposta,int id_game){
        this.bet=bet;
        
        this.aposta=aposta;
        this.id_game=id_game;
        
    }

    public int getBet(){
        return this.bet;
    }

    public void setName(int bet) {
        this.bet = bet;
    }

    public int getAposta(){
        return this.aposta;
    }

    public void setAposta(int aposta) {
        this.aposta = aposta;
    }

    public void setIdGame(int id_game){
        this.id_game=id_game;
    }

    public int getIdGame() {
        return this.id_game;

    }
    public void setBet(int bet){
        this.bet=bet;

    }

    

}
