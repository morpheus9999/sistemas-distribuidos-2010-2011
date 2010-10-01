/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Client_Server;

import java.io.*;

/**
 *
 * @author JLA
 */
public class FileIO {

    private String path;
    private FileInputStream inputStream;
    private FileOutputStream outputStream;
    private ObjectInputStream read;
    private ObjectOutputStream write;

    /*  constructor: it receives the file path  */
    public FileIO(String path) {
        this.path = path;
    }

    /*  opens the writing stream    */
    public boolean openWrite() {
        try {
            outputStream = new FileOutputStream(path);
            write = new ObjectOutputStream(outputStream);
        }catch (Exception error) {
            System.out.println("Error opening write channel: "+ error.getMessage());
            return false;
        }
        return true;
    }

    /*  closes the writing stream   */
    public boolean closeWrite() {
        try {
            if(write != null) write.close();
            if(outputStream != null) outputStream.close();
        } catch (Exception error) {
            System.out.println("Erro closing write channelr: "+ error.getMessage());
            return false;
        }
        return true;
    }

    /*  opens the reading stream    */
    public boolean openRead() {
         try {
            inputStream = new FileInputStream(path);
            read = new ObjectInputStream(inputStream);
        }catch (Exception error) {
            System.out.println("Error opening read channel: "+ error.getMessage());
            return false;
        }
        return true;
    }

    /*  opens the reading stream    */
    public boolean closeRead() {
        try {
            if(read != null) read.close();
            if(inputStream != null) inputStream.close();
        } catch (Exception error) {
            System.out.println("Error closing read channel: "+ error.getMessage());
            return false;
        }
        return true;
    }

    /*  writes an object to the file    */
    public boolean writeObject(Object data) {
        try {
            write.writeObject(data);
        } catch (Exception error) {
            System.out.println("Error writing to file: "+ error.getMessage());
            return false;
        }
        return true;
    }

    /*  reads an object from the file (null = error)   */
    public Object readObject() {
        Object temp = null;
        
        try {
            temp = read.readObject();
        } catch (Exception error) {
            System.out.println("Error reading from file: "+ error.getMessage());
        }
        
        return temp;
    }

    /*  checks if the designated file exists    */
    public boolean fileExists() {
        File f = new File(this.path);

        if(f.exists() == true)
            return true;
        else
            return false;
    }
}
