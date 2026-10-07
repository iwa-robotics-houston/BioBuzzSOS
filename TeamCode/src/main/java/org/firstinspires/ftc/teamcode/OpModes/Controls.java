package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.hardware.Gamepad;

public class Controls {
    private final Gamepad gamepad1;
    private final Gamepad gamepad2;

    public Controls(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
    }

    // Driving && Joystick Controls so we can reuse
    public double getForward() { return -gamepad1.left_stick_y; }
    public double getStrafe()  { return  gamepad1.left_stick_x; }
    public double getRotate()  { return  gamepad1.right_stick_x; }

    // Intake Controls
    public boolean getIntake()  { return gamepad1.left_trigger > 0.1; }
    public boolean getRelease() { return gamepad1.left_bumper; }

    // Launch Controls
    public boolean getLaunch() { return gamepad1.right_bumper; }

}