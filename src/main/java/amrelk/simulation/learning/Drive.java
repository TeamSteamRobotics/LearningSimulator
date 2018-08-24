package amrelk.simulation.learning;

public class Drive {
    private Talon leftMotor;
    private Talon rightMotor;

    public Drive(Talon left, Talon right) {
        leftMotor = left;
        rightMotor = right;
    }

    public void drive(double forward, double turn) {

        double leftMotorOutput;
        double rightMotorOutput;
        double maxInput = Math.copySign(Math.max(Math.abs(forward), Math.abs(turn)), forward);

        if (forward >= 0.0) {
            // First quadrant, else second quadrant
            if (turn >= 0.0) {
                leftMotorOutput = maxInput;
                rightMotorOutput = forward - turn;
            } else {
                leftMotorOutput = forward + turn;
                rightMotorOutput = maxInput;
            }
        } else {
            // Third quadrant, else fourth quadrant
            if (turn >= 0.0) {
                leftMotorOutput = forward + turn;
                rightMotorOutput = maxInput;
            } else {
                leftMotorOutput = maxInput;
                rightMotorOutput = forward - turn;
            }
        }

        leftMotor.set(Math.max(Math.min(leftMotorOutput, 1), -1));
        rightMotor.set(Math.max(Math.min(rightMotorOutput, 1), -1));
    }
}
