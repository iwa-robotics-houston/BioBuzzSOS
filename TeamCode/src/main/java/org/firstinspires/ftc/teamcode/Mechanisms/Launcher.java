package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

// Launcher mechanism: one motor driving the flywheel (closed-loop velocity control via
// DcMotorEx).

public class Launcher {

    // Hardware name: must match the name in the robot configuration on the Driver Station.
    private static final String MOTOR_NAME = "launcher";

    // Velocities are in encoder ticks per second (28 ticks/rev, so RPM = ticks / 28 * 60).
    public static final double TARGET_VELOCITY = 1250; // change velocity based on Desmos calcs
    public static final double MIN_VELOCITY = 1225; // ~2% band under target — tune on-robot

    // Velocity used to actively slow the flywheel down faster than it would coast on its
    // own, by briefly spinning it the opposite direction. Tune: strong enough to slow it
    // quickly, not so strong it's basically trying to spin up backwards.
    public static final double REVERSE_VELOCITY = -400;

    // PIDF tuned for a flywheel. Tune these.
    public static final double P = 40;
    public static final double I = 0;
    public static final double D = 0;
    public static final double F = 12.5;

    private DcMotorEx flywheel;

    /** Call once from the OpMode's init(). */
    public void init(HardwareMap hwMap) {
        flywheel = hwMap.get(DcMotorEx.class, MOTOR_NAME);

        // RUN_USING_ENCODER enables closed-loop velocity control.
        // If velocity jumps way past the target, check the encoder cable and motor polarity.
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidf = new PIDFCoefficients(P, I, D, F);
        flywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidf);

        // Let the flywheel coast down instead of braking hard.
        flywheel.setZeroPowerBehavior(FLOAT);
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
    }

    /** Briefly spins the flywheel backward to slow it down faster than coasting would. */
    public void reverse() {
        flywheel.setVelocity(REVERSE_VELOCITY);
    }

    public void stop() {
        flywheel.setVelocity(0);
    }

    /** True when the flywheel is fast enough to make a successful shot. */
    public boolean isReady() {
        return getVelocity() > MIN_VELOCITY;
    }

    public double getVelocity() {
        return flywheel.getVelocity();
    }

    public double getRpm() {
        return getVelocity() / 28.0 * 60.0;
    }
}