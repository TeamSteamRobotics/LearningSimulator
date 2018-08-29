package amrelk.simulation.learning;

public class Joystick {
    private boolean isUseless = false;

    public Joystick(int port) {
        if (port != 0)
            isUseless = true;
    }

    public double getX() {
        return HAL.joystickX;
    }

    public double getY() {
        return HAL.joystickY;
    }
}
