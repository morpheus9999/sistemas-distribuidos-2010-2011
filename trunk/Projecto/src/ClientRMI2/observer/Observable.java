/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientRMI2.observer;

import java.util.Vector;

/**
 *
 * @author jojo
 */
public class Observable {

    private boolean changed = false;
    private Vector obs;

    public Observable() {
        obs = new Vector();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null) {
            throw new NullPointerException();
        }
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public void notifyObservers() {
        notifyObservers(null);
    }

    public void notifyObservers(Object arg) {

        Object[] arrLocal;

        synchronized (this) {
            if (!changed) {
                return;
            }
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length - 1; i >= 0; i--) {
            ((Observer) arrLocal[i]).update(this, arg);
        }
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    protected synchronized void setChanged() {
        changed = true;
    }
}
