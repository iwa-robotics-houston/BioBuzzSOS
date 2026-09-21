package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrive {
    /* The four drive motors on a mecanum chassis. Mecanum wheels have rollers
    set at 45 degrees, which lets combinations of wheel spin produce forward,
    strafe (sideways), and rotational motion. */

    private DcMotor frontLeftMotor, backLeftMotor, frontRightMotor, backRightMotor;

    public void init(HardwareMap hwMap){
        /* Pull each motor out of the hardware map using the NAMES CONFIGURED
        ON THE DRIVER'S STATION!! These strings must EXACTLY match what's set up here. */
        frontLeftMotor = hwMap.get(DcMotor.class,"frontLeft");
        backLeftMotor = hwMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hwMap.get(DcMotor.class, "frontRight");
        backRightMotor = hwMap.get(DcMotor.class, "backRight");

        /* Motors on opposite sides of the robot are physically mounted facing
        opposite directions, so one side needs its rotation direction reversed. */

        // For this year, our frontLeft and backRight motors are mounted on opposite
        // sides & have to be reversed

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        /* RUN_USING_ENCODER uses the motor's built-in encoder to run closed-loop
        velocity control, which makes power output more consistent regardless of battery
        voltage or load. */

        frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /* Robot-centric mecanum drive: forward/strafe/rotate are all relative to the
    robot's own frame — if the robot is facing forward, "forward" drives it forward,
    regardless of which way it's facing on the field. This was done at the preference of
    our drive team. */
    public void drive(double forward, double strafe, double rotate){

        double frontLeftPower = forward + strafe + rotate;
        double backLeftPower = forward - strafe + rotate;
        double frontRightPower = forward - strafe - rotate;
        double backRightPower = forward + strafe - rotate;

        double maxPower = 1.0; // 100% power -- controls speed of the robot

        maxPower = Math.max(maxPower, Math.abs(frontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(backLeftPower));
        maxPower = Math.max(maxPower, Math.abs(frontRightPower));
        maxPower = Math.max(maxPower, Math.abs(backRightPower));

        frontLeftMotor.setPower(frontLeftPower / maxPower);
        backLeftMotor.setPower(backLeftPower / maxPower);
        frontRightMotor.setPower(frontRightPower / maxPower);
        backRightMotor.setPower(backRightPower / maxPower);
    }
}