package amrelk.simulation.learning;

class HAL {
    static double leftMotorInput;
    static double rightMotorInput;

    static long leftEncoderTicks = 0;
    static long rightEncoderTicks = 0;
    private static double leftEncoderLeftover = 0;
    private static double rightEncoderLeftover = 0;
    private static final double degreesPerTick = 360.0/2048.0;

    static void setMotor(int port, double value) {
        if (port == 0) {
            leftMotorInput = value;
        } else {
            rightMotorInput = value;
        }
    }

    static void addLeftEncoder(double amount) {
        amount = Math.abs(amount);
        amount += leftEncoderLeftover;
        while (amount > degreesPerTick) {
            leftEncoderTicks++;
            amount -= degreesPerTick;
        }
        leftEncoderLeftover = amount;
    }

    static void addRightEncoder(double amount) {
        amount = Math.abs(amount);
        amount += rightEncoderLeftover;
        while (amount > degreesPerTick) {
            rightEncoderTicks++;
            amount -= degreesPerTick;
        }
        rightEncoderLeftover = amount;
    }
}
