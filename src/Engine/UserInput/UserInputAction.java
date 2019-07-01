package Engine.UserInput;

public class UserInputAction {

    public enum UserInputActionType {
        NORMAL,
        INITIAL_PRESS_ONLY
    }
    public enum UserInputActionState {
        RELEASED,
        PRESSED,
        WAITING_FOR_RELEASE
    }

    private final String name;
    private final UserInputActionType behavior;
    private UserInputActionState state;
    /**
     * The amount of this action has been executed. For example, if a user scrolls the mouse wheel really fast, this
     * number will be high while a slow scroll will be low. This also be used to keep track of how many times a key
     * has been pressed before released.
     */
    private int amount;

    public UserInputAction(final String name){
        this(name, UserInputActionType.NORMAL);
    }

    public UserInputAction(final String name, final UserInputActionType behavior){
        this.name = name;
        this.behavior = behavior;
        reset();
    }

    public void reset(){
        state = UserInputActionState.RELEASED;
        amount = 0;
    }

    public String getName(){
        return name;
    }

    public synchronized void tap(){
        press();
        release();
    }

    public synchronized void press(){
        press(1);
    }

    public synchronized void press(int amount){
        if(state != UserInputActionState.WAITING_FOR_RELEASE){
            this.amount += amount;
            state = UserInputActionState.PRESSED;
        }
    }

    public synchronized void release(){
        state = UserInputActionState.RELEASED;
    }

    public synchronized boolean isPressed(){
        return (getAmount() != 0);
    }

    public synchronized int getAmount(){
        int val = amount;
        if(val != 0) {
            if (state == UserInputActionState.RELEASED) {
                amount = 0;
            } else if (behavior == UserInputActionType.INITIAL_PRESS_ONLY) {
                state = UserInputActionState.WAITING_FOR_RELEASE;
                amount = 0;
            }
        }
        return val;
    }

}
