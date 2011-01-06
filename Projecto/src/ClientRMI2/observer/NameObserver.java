/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2.observer;


import java.util.Observable;
import java.util.Observer;

public class NameObserver implements Observer {
 private String name;

 public NameObserver() {
  name = null;
  System.out.println("NameObserver created: Name is " + name);
 }

 public void update(Observable obj, Object arg) {
  if (arg instanceof String) {
   name = (String) arg;
   System.out.println("NameObserver: Name changed to " + name);
  } else {
   System.out.println("NameObserver: Some other change to subject!");
  }
 }
}
