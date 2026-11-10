package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Passthrough {

    private DcMotor passthroughMotor;

    public void init(HardwareMap hwMap){
        passthroughMotor = hwMap.get(DcMotor.class, "passthrough");
    }

    // Spins the passthrough wheel inward, pulling whatever it's touching further in
    public void in(){
        passthroughMotor.setPower(1.0);
    }

    //  Reverses the passthrough to spit it back out
    public void out(){
        passthroughMotor.setPower(-1.0);
    }

    // Stops the passthrough motor.
    public void stop(){
        passthroughMotor.setPower(0);
    }
}