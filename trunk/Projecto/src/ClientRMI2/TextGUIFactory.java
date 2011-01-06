
package ClientRMI2;

public class TextGUIFactory extends GUIFactory{
    @Override
    public GUI createGUI() {
        return new TextGUI();
    }
}
