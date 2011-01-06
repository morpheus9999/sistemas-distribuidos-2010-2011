/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ClientRMI2.observer;


/**
*
*/
public class TestObservers {
    
 public static void main(String args[]) {

  // Create the Subject and Observers.
  ConcreteSubject s = new ConcreteSubject("Corn Pops", 1.29f);
  NameObserver nameObs = new NameObserver();
  PriceObserver priceObs = new PriceObserver();

  // Add those Observers!
  s.addObserver(nameObs);
  s.addObserver(priceObs);

  // Make changes to the Subject.
  s.setName("Frosted Flakes");
  s.setPrice(4.57f);
  s.setPrice(9.22f);
  s.setName("Sugar Crispies");
 }
}
