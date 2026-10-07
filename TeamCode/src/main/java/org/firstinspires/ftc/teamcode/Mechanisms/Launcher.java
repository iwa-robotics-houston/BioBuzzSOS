package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/*
 * Launcher mechanism: two motors driving one flywheel (closed-loop velocity control via
 * DcMotorEx on both).
 */

public class Launcher {

    // Hardware names: must match the names in the robot configuration on the Driver Station.
    private static final String MOTOR_NAME = "launcher";
    private static final String MOTOR2_NAME = "launcher2"; // second flywheel motor

    // Velocities are in encoder ticks per second (28 ticks/rev, so RPM = ticks / 28 * 60).
    public static final double TARGET_VELOCITY = 1250; // change velocity based on Desmos calcs
    public static final double MIN_VELOCITY = 1225; // ~2% band under target — tune on-robot

    // PIDF tuned for a flywheel. Tune these on your robot.
    // NOTE: if the two motors/gearboxes aren't identical, each may need its own PIDF —
    // start with the same values on both and only split them if one consistently lags.
    public static final double P = 40;
    public static final double I = 0;
    public static final double D = 0;
    public static final double F = 12.5;

    private DcMotorEx flywheel;
    private DcMotorEx flywheel2;

    /** Call once from the OpMode's init(). */
    public void init(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, MOTOR_NAME);
        flywheel2 = hwMap.get(DcMotorEx.class, MOTOR2_NAME);

        // RUN_USING_ENCODER enables closed-loop velocity control.
        // If velocity jumps way past the target, check the encoder cable and motor polarity.
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);
        flywheel2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        // If the two motors are mounted facing each other / driving the wheel from
        // opposite sides, one needs to spin the opposite physical direction to add
        // torque instead of fighting the other motor. Flip this if the wheel stalls
        // or spins slower with both motors on than with one.
        flywheel2.setDirection(DcMotorSimple.Direction.REVERSE);

        // Let the flywheel coast down instead of braking hard.
        flywheel.setZeroPowerBehavior(FLOAT);
        flywheel2.setZeroPowerBehavior(FLOAT);
    }

    /**
     * Call every loop. Spins the flywheel up while {@code shoot} is true.
     *
     * @return true if the flywheel is up to speed and ready to fire
     */
    public boolean update(boolean shoot) {
        if (shoot) {
            spinUp();
        } else {
            stop();
        }

        return shoot && isReady();
    }

    public void spinUp() {
        flywheel.setVelocity(TARGET_VELOCITY);
        flywheel2.setVelocity(TARGET_VELOCITY);
    }

    public void stop() {
        flywheel.setVelocity(0);
        flywheel2.setVelocity(0);
    }

    /** True when the flywheel is fast enough to make a successful shot. */
    public boolean isReady() {
        return getVelocity() > MIN_VELOCITY;
    }

    /** Average of both motors' reported velocity — more stable than reading just one. */
    public double getVelocity() {
        return (flywheel.getVelocity() + flywheel2.getVelocity()) / 2.0;
    }

    public double getRpm() {
        return getVelocity() / 28.0 * 60.0;
    }
}