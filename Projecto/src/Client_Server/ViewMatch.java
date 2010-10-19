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
public class ViewMatch implements Serializable{
    int idJogo;
    String home;
    String fora;
    public ViewMatch(int idJogo,String home,String fora){
        this.idJogo=idJogo;
        this.home=home;
        this.fora=fora;
    }

    public ViewMatch() {
        this.idJogo=0;
        this.home=null;
        this.fora=null;
    }

    
    public int getIdJogo(){
        return this.idJogo;
    }
    public String getHome(){
        return this.home;
    }
    public String getFora(){
        return this.fora;
    }

    public void setIdJogo(int idJogo){
        this.idJogo=idJogo;
    }
    public void setHome(String home){
        this.home=home;
    }
    public void setFora(String fora){
        this.fora=fora;
    }

}
