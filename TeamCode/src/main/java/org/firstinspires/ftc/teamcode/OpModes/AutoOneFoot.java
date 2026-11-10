package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.MecanumDrive;

// Simple time-based autonomous: drives straight forward for a short burst, then stops.
// This doesn't use encoders to measure distance — it just runs the motors for a fixed
// amount of time.

@Autonomous(name = "AutoOneFoot", group = "LinearOpMode")
public class AutoOneFoot extends LinearOpMode {

    @Override
    public void runOpMode() {
        // Set up the drivetrain the same way the TeleOp does.
        MecanumDrive drive = new MecanumDrive();
        drive.init(hardwareMap);

        // Tracks how long we've been driving, so we know when to stop.
        ElapsedTime runtime = new ElapsedTime();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Waits here until the driver presses the Play/Start button on the Driver Station.
        waitForStart();

        // opModeIsActive() stays true until the match timer ends or Stop is pressed
        if (opModeIsActive()) {
            double drivePower = 0.8;        // 80% power — modest speed for a short, controlled move
            double driveTimeSeconds = 0.5;  // starting guess for time

            // Reset the stopwatch right before we start driving, so the timing is accurate.
            runtime.reset();

            // Keep calling drive() every loop until either the time runs out or the
            // OpMode gets stopped — drive() has to be called repeatedly like this
            // because setPower() only holds until the next command, it doesn't "remember."
            while (opModeIsActive() && runtime.seconds() < driveTimeSeconds) {
                drive.drive(drivePower, 0, 0); // forward only — no strafe, no rotation
            }

            // Stop all motors once the timer's up, so the robot doesn't keep going forward.
            drive.drive(0, 0, 0);
        }

        telemetry.addData("Status", "Done");
        telemetry.update();
    }
}