package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/*
 * Launcher mechanism: one high-speed flywheel motor (closed-loop velocity control via
 * DcMotorEx) and one continuous-rotation "windmill" servo that feeds elements into the wheel.
 */

public class Launcher {

    // Hardware names: must match the names in the robot configuration on the Driver Station.
    private static final String MOTOR_NAME = "launcher";
    private static final String FEEDER_NAME = "windmill"; // we may not use a windmill

    // Velocities are in encoder ticks per second (28 ticks/rev, so RPM = ticks / 28 * 60).
    public static final double TARGET_VELOCITY = 1250; // change velocity based on Desmos calcs
    public static final double MIN_VELOCITY = 1245; // change to be in 1-5% within range

    // PIDF tuned for a flywheel. Tune these on your robot.
    public static final double P = 40;
    public static final double I = 0;
    public static final double D = 0;
    public static final double F = 12.5;

    private DcMotorEx flywheel;
    private CRServo feeder;

    /** Call once from the OpMode's init(). */
    public void init(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, MOTOR_NAME);
        feeder = hwMap.get(CRServo.class, FEEDER_NAME);

        // RUN_USING_ENCODER enables closed-loop velocity control.
        // If velocity jumps way past the target, check the encoder cable and motor polarity.
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,
                new PIDFCoefficients(P, I, D, F));
        // Let the flywheel coast down instead of braking hard.

        flywheel.setZeroPowerBehavior(FLOAT);

        feeder.setDirection(DcMotorSimple.Direction.REVERSE);
        feeder.setPower(0);
    }

    /**
     * Call every loop. Spins the flywheel up while {@code shoot} is true, and runs the feeder
     * only once the flywheel is fast enough for a good shot.
     *
     * @return true if the feeder is currently running
     */
    public boolean update(boolean shoot) {
        if (shoot) {
            spinUp();
        } else {
            stop();
        }

        boolean feeding = shoot && isReady();
        feeder.setPower(feeding ? 1 : 0);
        return feeding;
    }

    public void spinUp() {
        flywheel.setVelocity(TARGET_VELOCITY);
    }

    public void stop() {
        flywheel.setVelocity(0);
        feeder.setPower(0);
    }

    /** True when the flywheel is fast enough to make a successful shot. */
    public boolean isReady() {
        return flywheel.getVelocity() > MIN_VELOCITY;
    }

    public double getVelocity() {
        return flywheel.getVelocity();
    }

    public double getRpm() {
        return flywheel.getVelocity() / 28.0 * 60.0;
    }
}