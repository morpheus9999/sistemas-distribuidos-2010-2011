/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
  public static void main(String[] args) {
    MyObservable observable = new MyObservable();
    MyObserver1 observer1 = new MyObserver1();
    MyObserver2 observer2 = new MyObserver2();
    observable.addObserver(observer1);
    observable.addObserver(observer2);
    observable.start();
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class MyObserver1 implements Observer {
  public void update(Observable o, Object arg) {
      
    Integer count = (Integer) arg;
    System.out.println("ENTRA1     "+count);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyObserver1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ACABA ENTRA1");
  }
}

class MyObserver2 implements Observer {
  public void update(Observable o, Object arg) {
    Integer count = (Integer) arg;
      System.out.println("ENTRA2      "+count);
      
      try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MyObserver1.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("ACABA ENTRA2");
  }
}

class MyObservable extends Observable implements Runnable {
  public MyObservable() {
  }

  public void start() {
    new Thread(this).start();
  }

  public void run() {
    int count = 0;
    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    count++;
      System.out.println("ENTRA0  "+count);
    setChanged();
    System.out.println("ENTRA00   "+count);
    
    notifyObservers(new Integer(3));
    System.out.println("ACABA ENTRA000");
  }
}