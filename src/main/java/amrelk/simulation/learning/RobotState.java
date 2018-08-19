package amrelk.simulation.learning;

class RobotState {
    // constants. U:<unit>
    private final double
            kTopWheelSpeed = 100, // U:px/s
            kBaseWheelAcceleration = 100, // U:px/s/s
            kFriction = 1000, // U:px/s/s
            kWheelbase = 30; // U:px

    // these variables are internal to the simulation
    Vector2 robotPos; // U:px
    double rot; // U:rad
    private double
            leftWheelVelocity, // U:px/s
            rightWheelVelocity; // U:px/s
    double
            leftWheelInput,
            rightWheelInput;
    private Vector2
            robotVelocity; // U:px/s

    private long lastLoopTime; // u:ns
    private long timeSinceLastLoop; // u:ns

    RobotState() {
        robotPos = new Vector2();
        rot = 0;
        leftWheelInput = 0;
        leftWheelVelocity = 0;
        rightWheelInput = 0;
        rightWheelVelocity = 0;
        robotVelocity = new Vector2();
        lastLoopTime = System.nanoTime();
    }

    public void update() {
        // figure out how long it's been since last update
        timeSinceLastLoop = System.nanoTime() - lastLoopTime;
        lastLoopTime = System.nanoTime();

        // determine the speed of the wheels
        leftWheelVelocity += leftWheelInput * nsAdapter( kBaseWheelAcceleration, timeSinceLastLoop ) * ( kTopWheelSpeed - Math.abs( leftWheelVelocity ) );
        rightWheelVelocity += rightWheelInput * nsAdapter( kBaseWheelAcceleration, timeSinceLastLoop ) * ( kTopWheelSpeed - Math.abs( rightWheelVelocity ) );

        // determine rotation
        rot += nsAdapter( ( rightWheelVelocity - leftWheelVelocity ), timeSinceLastLoop ) * ( 2 / kWheelbase );

        // account for friction
        robotVelocity.damp( nsAdapter( kFriction, timeSinceLastLoop ) );

        // account for wheels
        robotVelocity = robotVelocity.add( new Vector2( (leftWheelVelocity + rightWheelVelocity) / 2, rot ) );

        // apply velocity to position
        robotPos = robotPos.add(robotVelocity);

    }

    static double nsAdapter( double unitWithSeconds, long nanoseconds ) {
        return unitWithSeconds * (nanoseconds / 1e9);
    }
    static Vector2 nsAdapter( Vector2 unitWithSeconds, long nanoseconds ) {
        return unitWithSeconds.mult(nanoseconds / 1e9);
    }
}
