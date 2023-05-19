package cosc202.andie;

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

public class KeyBoardShortcuts {
/* You should also make it clear
from the interface what the keyboard shortcuts are – putting the shortcuts beside menu items
and in tool-tips that pop up over toolbar buttons are common approaches here. */
// CTRL + Z (undo)
KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.VK_CONTROL);
// bind the keystroke to an object
//inputMap.put(key, DefaultEditorKit.backwardAction);
public void keyTyped(KeyEvent e) {
    displayInfo(e, "KEY TYPED: ");
}
private void displayInfo(KeyEvent e, String keyStatus){
        
    //You should only rely on the key char if the event
    //is a key typed event.
    int id = e.getID();
    String keyString;
    if (id == KeyEvent.KEY_TYPED) {
        char c = e.getKeyChar();
        keyString = "key character = '" + c + "'";
    } else {
        int keyCode = e.getKeyCode();
        keyString = "key code = " + keyCode
                + " ("
                + KeyEvent.getKeyText(keyCode)
                + ")";
    }
    
    int modifiersEx = e.getModifiersEx();
    String modString = "extended modifiers = " + modifiersEx;
    String tmpString = KeyEvent.getModifiersExText(modifiersEx);
    if (tmpString.length() > 0) {
        modString += " (" + tmpString + ")";
    } else {
        modString += " (no extended modifiers)";
    }
    
    String actionString = "action key? ";
    if (e.isActionKey()) {
        actionString += "YES";
    } else {
        actionString += "NO";
    }
    
    String locationString = "key location: ";
    int location = e.getKeyLocation();
    if (location == KeyEvent.KEY_LOCATION_STANDARD) {
        locationString += "standard";
    } else if (location == KeyEvent.KEY_LOCATION_LEFT) {
        locationString += "left";
    } else if (location == KeyEvent.KEY_LOCATION_RIGHT) {
        locationString += "right";
    } else if (location == KeyEvent.KEY_LOCATION_NUMPAD) {
        locationString += "numpad";
    } else { // (location == KeyEvent.KEY_LOCATION_UNKNOWN)
        locationString += "unknown";
    }
    
    //...//Display information about the KeyEvent...
}

}
