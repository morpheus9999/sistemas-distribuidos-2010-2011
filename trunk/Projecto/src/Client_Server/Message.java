/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author JLA
 */
public class Message implements Serializable{
    private String author = null;
    private Hashtable<String, Vector<String>> messageBuffer = null;
    private String text;

    public Message() {
        this.author = null;
        this.messageBuffer = new Hashtable<String, Vector<String>>();
    }
    public Message(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getText() {
        return this.text;
    }

    public void setHashtable(Hashtable<String, Vector<String>> temp) {
        this.messageBuffer.putAll(temp);
    }

    public Hashtable<String, Vector<String>> getHashtable() {
        return this.messageBuffer;
    }

    public boolean addEntry(String key, String message) {
        Vector<String> temp;
        if(key != null && message != null) {
            if(this.messageBuffer.containsKey(key)) {
                /*  if entry exists, adds to list   */
                temp = this.messageBuffer.get(key);
                temp.add(message);
                this.messageBuffer.put(key, temp);
            } else {
                /*  otherwise, creates a new entry  */
                temp = new Vector<String>();
                temp.add(message);
                this.messageBuffer.put(key, temp);
            }
            return true;
        } else
            return false;
    }

    public Vector<String> getEntry(String key) {
        if(key != null)
            return this.messageBuffer.get(key);
        else
            return null;
    }

    public void clearHashtable() {
        this.messageBuffer.clear();
    }

    /*  to ease searches in the hashtable   */
    public Enumeration<Vector<String>> getMessagesEnumeration() {
        return this.messageBuffer.elements();
    }

    /*  to ease searches in the hashtable   */
    public Enumeration<String> getKeysEnumeration() {
        return this.messageBuffer.keys();
    }
}
