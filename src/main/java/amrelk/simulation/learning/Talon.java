package amrelk.simulation.learning;

public class Talon {
    private int port;

    public Talon(int port) {
        this.port = port;
    }

    public void set(double value) {
        HAL.setMotor(port, Math.max(Math.min(value, 1), -1));
    }
}
