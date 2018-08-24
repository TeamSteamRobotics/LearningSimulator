package amrelk.simulation.learning;

class HAL {
    static double leftMotorInput;
    static double rightMotorInput;

    static void setMotor(int port, double value) {
        if (port == 0) {
            leftMotorInput = value;
        } else {
            rightMotorInput = value;
        }
    }
}
