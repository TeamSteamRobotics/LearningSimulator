package amrelk.simulation.learning;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RobotFrame {
    // constants
    private final double
            kTopSpeed = 10,
            kWheelAcceleration = 10;
    private final long
            kLoopPeriod = 100; // in ms

    // these variables will be output
    private double
            posX,
            posY,
            rot;

    // these variables are internal to the simulation
    private double
            leftWheelVelocity,
            rightWheelVelocity,
            leftWheelInput,
            rightWheelInput;
    private Vector2
            robotVelocity;

    private ScheduledExecutorService service;
    public long counter;
    private Runnable mainLoop = new Runnable() {
        @Override
        public void run() {
            if (counter == 0) {
                backendInit();
                robotInit();
            }
            backendPeriodic();
            robotPeriodic();
            counter++;
        }
    };


    public RobotFrame() {
        counter = 0;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(mainLoop, 0, kLoopPeriod, TimeUnit.MILLISECONDS);
    }

    public void setPosX(double x) {
        posX = x;
    }

    public void robotInit(){};

    public void robotPeriodic(){
        System.out.println("Should be overridden");
    };

    private void backendInit() {
        posX = 0;
        posY = 0;
        rot = 0;
        leftWheelInput = 0;
        leftWheelVelocity = 0;
        rightWheelInput = 0;
        rightWheelVelocity = 0;
        robotVelocity = new Vector2(0,0);
    }

    private void backendPeriodic() {
        System.out.println("[" + posX + "," + posY + "," + rot + "]");
    }

}
