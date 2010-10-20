/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author JLA
 */
public class OnlineUsers implements Serializable{
    private Vector<String> list = null;

    public OnlineUsers() {
        this.list = new Vector<String>();
    }

    public void setList(Vector<String> temp) {
        this.list = temp;
    }

    public Vector<String> getList() {
        return this.list;
    }

    public void addEntry(String temp) {
        this.list.add(temp);
    }

    public void addVector(Vector<String> temp) {
        this.list.addAll(temp);
    }

    public void printOnlineUsers() {
        if(this.list.isEmpty())
            System.out.println("There are no users online! (is this even possible?)");

        for(int i = 0; i < this.list.size(); i++)
            System.out.println("> "+this.list.get(i));
    }
}
