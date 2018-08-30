package amrelk.simulation.learning;

class RobotState {
    // constants. U:<unit>
    private final double
            kWheelbase = 20, // U:px
            kWheelRadius = 5, // U:px
            kTopWheelSpeed = 1000, // U:px/s
            kBaseWheelAcceleration = 2500, // U:px/s/s
            kWheelFriction = 1, // U:px/s/s
            kRobotFriction = 10; // U:px/s/s

    // these variables are internal to the simulation
    Vector2 robotPos; // U:px
    double rot; // U:rad
    private double
            leftWheelVelocity, // U:px/s
            rightWheelVelocity; // U:px/s
    double
            leftMotorInput, // U:arbitrary
            rightMotorInput; // U:arbitrary
    private Vector2
            robotVelocity; // U:px/s

    private long lastLoopTime; // u:ns
    private long timeSinceLastLoop; // u:ns

    RobotState() {
        robotPos = new Vector2();
        rot = 0;
        leftMotorInput = 0;
        leftWheelVelocity = 0;
        rightMotorInput = 0;
        rightWheelVelocity = 0;
        robotVelocity = new Vector2();
        lastLoopTime = System.nanoTime();
    }

    public void update() {
        // figure out how long it's been since last update
        timeSinceLastLoop = System.nanoTime() - lastLoopTime;
        lastLoopTime = System.nanoTime();

        // apply friction to wheels
        leftWheelVelocity /= 1 + nsAdapter(kWheelFriction, timeSinceLastLoop);
        rightWheelVelocity /= 1 + nsAdapter(kWheelFriction, timeSinceLastLoop);

        // apply input to wheels
        leftWheelVelocity += leftMotorInput * nsAdapter( kBaseWheelAcceleration, timeSinceLastLoop ) * Math.max( Math.min( ( kTopWheelSpeed - Math.abs( leftWheelVelocity ) ) / kTopWheelSpeed, 1), -1 );
        rightWheelVelocity += rightMotorInput * nsAdapter( kBaseWheelAcceleration, timeSinceLastLoop ) * Math.max( Math.min( ( kTopWheelSpeed - Math.abs( rightWheelVelocity ) ) / kTopWheelSpeed, 1), -1 );
        System.out.println(leftWheelVelocity + "\t|\t" + rightWheelVelocity);

        // determine rotation
        HAL.addLeftEncoder( nsAdapter( leftWheelVelocity, timeSinceLastLoop ) );
        HAL.addRightEncoder( nsAdapter( rightWheelVelocity, timeSinceLastLoop ) );
        rot += nsAdapter( leftWheelVelocity - rightWheelVelocity, timeSinceLastLoop ) * ( 2 / kWheelbase );
        while ( rot > 2*Math.PI ) {
            rot -= 2*Math.PI;
        }
        while ( rot < -2*Math.PI) {
            rot += 2*Math.PI;
        }

        // account for friction
        robotVelocity.damp( 1 + nsAdapter( kRobotFriction, timeSinceLastLoop ) );

        // account for wheels
        robotVelocity = robotVelocity.add( new Vector2( nsAdapter((leftWheelVelocity + rightWheelVelocity) / 2, timeSinceLastLoop), rot ) );

        // apply velocity to position
        robotPos = robotPos.add( nsAdapter( robotVelocity, timeSinceLastLoop ) );

        //keep it on the screen
        robotPos.wrap( 0, 1920, 0, 1080 );

    }

    static double nsAdapter( double unitWithSeconds, long nanoseconds ) {
        return unitWithSeconds * ( nanoseconds / 1e9 );
    }
    static Vector2 nsAdapter( Vector2 unitWithSeconds, long nanoseconds ) {
        return unitWithSeconds.mult( nanoseconds / 1e9 );
    }
}
