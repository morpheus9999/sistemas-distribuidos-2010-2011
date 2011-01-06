
package ClientRMI2;

public class SimpleGUIFactory extends GUIFactory{

    @Override
    public GUI createGUI() {
        return new SimpleGUI();
    }
}
