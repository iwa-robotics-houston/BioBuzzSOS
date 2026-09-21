package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    private DcMotor primaryIntakeMotor;
    private CRServo leftIntakeServo;
    private CRServo rightIntakeServo;

    public void init(HardwareMap hwMap){
        primaryIntakeMotor = hwMap.get(DcMotor.class, "primaryIntake");
        leftIntakeServo = hwMap.get(CRServo.class, "leftIntake");
        rightIntakeServo = hwMap.get(CRServo.class, "rightIntake");

        /* Like the drive motors, the two side servos are mounted facing opposite
        directions, so one needs its spin reversed for both to roll inward together.*/
        rightIntakeServo.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /* Runs the primary intake and both side rollers inward, pulling balls in. */
    public void intake(){
        primaryIntakeMotor.setPower(-1.0);
        leftIntakeServo.setPower(1.0);
        rightIntakeServo.setPower(1.0);
    }

    /* Reverses everything to eject balls back out. */
    public void release(){
        primaryIntakeMotor.setPower(1.0);
        leftIntakeServo.setPower(-1.0);
        rightIntakeServo.setPower(-1.0);
    }

    /* Stops all intake hardware. */
    public void stop(){
        primaryIntakeMotor.setPower(0);
        leftIntakeServo.setPower(0);
        rightIntakeServo.setPower(0);
    }
}