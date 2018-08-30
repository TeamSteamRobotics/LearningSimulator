package amrelk.simulation.learning;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.jar.Manifest;


public abstract class RobotBase {

    // mode stuff
    private enum Mode {
        kNone,
        kDisabled,
        kAutonomous,
        kTeleop,
        kTest
    }
    private Mode m_lastMode = Mode.kNone;
    private Mode m_currentMode = Mode.kNone;
    // mode stuff

    // the robot simulation object
    private RobotState robotState;

    // server and its port - port is set in build.gradle
    private RobotServer server;
    private int robotPort;
    private String receivedString;

    private ScheduledExecutorService service;
    public long counter;
    private Runnable mainLoop = new Runnable() {
        @Override
        public void run() {
            if (counter == 0) {
                robotLoop("0,0,disabled");
            } else if (server.ready()) {
                robotLoop(server.readLine());
            }
        }
    };

    private void robotLoop(String inputs) {
            if (counter == 0) {
                backendInit();
                robotInit();
            }
            backendPeriodic(inputs.split(","));
            robotPeriodic();
            if (m_currentMode == Mode.kDisabled) {
                if (m_lastMode != Mode.kDisabled) {
                    disabledInit();
                    m_lastMode = Mode.kDisabled;
                }
                disabledPeriodic();
            } else if (m_currentMode == Mode.kAutonomous) {
                if (m_lastMode != Mode.kAutonomous) {
                    autonomousInit();
                    m_lastMode = Mode.kAutonomous;
                }
                autonomousPeriodic();
            } else if (m_currentMode == Mode.kTeleop) {
                if (m_lastMode == Mode.kTeleop) {
                    teleopInit();
                    m_lastMode = Mode.kTeleop;
                }
                teleopPeriodic();
            }
            counter++;
        }

    public void startMainLoop(int port) {
        counter = 0;
        robotPort = port;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(mainLoop, 0, 1, TimeUnit.MILLISECONDS);
    }

    public void robotInit(){}

    public void disabledInit(){}

    public void autonomousInit(){}

    public void teleopInit(){}

    public void robotPeriodic(){}

    public void disabledPeriodic(){}

    public void autonomousPeriodic(){}

    public void teleopPeriodic(){}

    private void backendInit() {
        try {
            server = new RobotServer(robotPort);
        } catch (IOException ex) {
            System.out.print("IOException while making the server: ");
            ex.printStackTrace();
        }
        robotState = new RobotState();
    }

    private void backendPeriodic(String[] inputs) {
        HAL.joystickX = Double.parseDouble(inputs[0]);
        HAL.joystickY = Double.parseDouble(inputs[1]);
        switch (inputs[2]) {
            case "auto":
                m_currentMode = Mode.kAutonomous;
                break;
            case "teleop":
                m_currentMode = Mode.kTeleop;
                break;
            default:
                m_currentMode = Mode.kDisabled;
        }
        robotState.leftMotorInput = HAL.leftMotorInput;
        robotState.rightMotorInput = HAL.rightMotorInput;
        // do the simulate
        robotState.update();
        // FULL SEND
        server.send(robotState.robotPos.x + "," + robotState.robotPos.y + "," + robotState.rot);
    }

    public static void main(String[] args) {
        String robotName = "";
        int robotPort = 3333;
        Enumeration<URL> resources = null;
        try {
            resources = RobotBase.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        while (resources != null && resources.hasMoreElements()) {
            try {
                Manifest manifest = new Manifest(resources.nextElement().openStream());
                robotName = manifest.getMainAttributes().getValue("Robot-Class");
                if (manifest.getMainAttributes().getValue("Robot-Port") != null) {
                    robotPort = Integer.getInteger(manifest.getMainAttributes().getValue("Robot-Port"));
                }
                if (robotName == null) {
                    robotName = "Robot";
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        System.out.println("********** Robot program starting **********");

        RobotBase robot;
        try {
            robot = (RobotBase) Class.forName(robotName).getConstructor().newInstance();
            robot.startMainLoop(robotPort);
        } catch (Throwable throwable) {
            Throwable cause = throwable.getCause();
            if (cause != null) {
                throwable = cause;
            }
            System.out.println("Unhandled exception instantiating robot " + robotName + " "
                    + throwable.toString());
            throwable.printStackTrace();
            System.out.println("Robots should not quit, but yours did!");
            System.out.println("Could not instantiate robot " + robotName + "!");
            System.exit(1);
        }
    }

}
