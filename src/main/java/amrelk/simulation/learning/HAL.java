package amrelk.simulation.learning;

class HAL {
    // MOTOR CONTROLLERS
    static double leftMotorInput;
    static double rightMotorInput;
    // MOTOR CONTROLLERS

    // ENCODERS
    static long leftEncoderTicks = 0;
    static long rightEncoderTicks = 0;
    private static double leftEncoderLeftover = 0;
    private static double rightEncoderLeftover = 0;
    private static final double degreesPerTick = 360.0/2048.0;
    // ENCODERS

    // JOYSTICK
    static double joystickX = 0;
    static double joystickY = 0;
    // JOYSTICK


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
