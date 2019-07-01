package Engine.UserInput;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class UserInputActionManager implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    //Mouse Codes
    public enum MouseButtonCode {
        MOVE_LEFT,
        MOVE_RIGHT,
        MOVE_UP,
        MOVE_DOWN,
        WHEEL_UP,
        WHEEL_DOWN,
        MOUSE_BUTTON_1,
        MOUSE_BUTTON_2,
        MOUSE_BUTTON_3,
        INVALID
    }
    private final EnumMap<MouseButtonCode, UserInputAction> mouseActions;
    private final Map<Integer, UserInputAction> keyActions;
    private final Component component;
    private boolean isRecentering;
    private Point currentMouseLocation;
    private Point centerLocation;

    public UserInputActionManager(final Component component) {
        mouseActions = new EnumMap<>(MouseButtonCode.class);
        keyActions = new HashMap<>();
        this.component = component;
        this.component.addKeyListener(this);
        this.component.addMouseListener(this);
        this.component.addMouseMotionListener(this);
        this.component.addMouseWheelListener(this);
        //Allow input of TAB key or others normally
        this.component.setFocusTraversalKeysEnabled(true);
        isRecentering = false;
        currentMouseLocation = new Point();
        centerLocation = new Point();
    }

    private UserInputAction getKeyAction(final KeyEvent e){
        final int keyCode = e.getKeyCode();
        return keyActions.get(keyCode);
    }

    private MouseButtonCode getMouseButtonCode(final MouseEvent e){
        switch(e.getButton()) {
            case MouseEvent.BUTTON1:
                return MouseButtonCode.MOUSE_BUTTON_1;
            case MouseEvent.BUTTON2:
                return MouseButtonCode.MOUSE_BUTTON_2;
            case MouseEvent.BUTTON3:
                return MouseButtonCode.MOUSE_BUTTON_3;
            default:
                return MouseButtonCode.INVALID;
        }
    }

    private UserInputAction getMouseKeyAction(final MouseEvent e) {
        final MouseButtonCode mouseButtonCode = getMouseButtonCode(e);
        return mouseActions.get(mouseButtonCode);
    }

    private void mouseHelper(final MouseButtonCode negativeCode, final MouseButtonCode positiveCode, int amount){
        final UserInputAction action =
                amount < 0 ? mouseActions.get(negativeCode)
                           : mouseActions.get(positiveCode);
        if(action != null){
            action.press(Math.abs(amount));
            action.release();
        }
    }

    public void setCursor(final Cursor cursor) {
        component.setCursor(cursor);
    }

    public void mapKeyActionToKey(final UserInputAction action, final int keyCode) {
        keyActions.put(keyCode, action);
    }

    public void mapMouseActionToKey(final UserInputAction action, final MouseButtonCode mouseButtonCode) {
        mouseActions.put(mouseButtonCode, action);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseHelper(MouseButtonCode.WHEEL_UP, MouseButtonCode.WHEEL_DOWN, e.getWheelRotation());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        final int mouseX = e.getX();
        final int mouseY = e.getY();
        if(isRecentering && centerLocation.x == mouseX && centerLocation.y == mouseY) {
            isRecentering = false;
        } else {
            final int dx = mouseX - currentMouseLocation.x;
            final int dy = mouseY - currentMouseLocation.y;
            mouseHelper(MouseButtonCode.MOVE_LEFT, MouseButtonCode.MOVE_RIGHT, dx);
            mouseHelper(MouseButtonCode.MOVE_UP, MouseButtonCode.MOVE_DOWN, dy);
            //TODO support recentering and relative movement mode
        }
        currentMouseLocation.x = mouseX;
        currentMouseLocation.y = mouseY;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Do nothing as we are already handling pressed and released
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        final UserInputAction action = getMouseKeyAction(e);
        if(action != null) action.press();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        final UserInputAction action = getMouseKeyAction(e);
        if(action != null) action.release();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final UserInputAction action = getKeyAction(e);
        if(action != null) action.press();
        e.consume();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final UserInputAction action = getKeyAction(e);
        if(action != null) action.release();
        e.consume();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        /* The difference between keyTyped and keyPressed is that for keyTyped, it fires when a key
         * can be converted into a unicode Character. For example, if Shift key is pressed, pressing
         * 'a' would result in KeyTyped be 'A' while KeyPress would be 'a'. Thus you cannot call
         * event.getKeyChar() in KeyPressed since no key char associated with the event.
         */
        e.consume();
    }

}
