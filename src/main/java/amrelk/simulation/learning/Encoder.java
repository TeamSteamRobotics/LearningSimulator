package amrelk.simulation.learning;

public class Encoder {

    private boolean left;

    public Encoder(int a, int b) {
        left = (a == 2);
    }

    public long get() {
        if (left) {
            return HAL.leftEncoderTicks;
        } else {
            return HAL.rightEncoderTicks;
        }
    }
}