package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Mechanisms.Intake;
import org.firstinspires.ftc.teamcode.Mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.Mechanisms.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Passthrough;

@TeleOp(name = "MainTeleOp", group = "LinearOpMode")
public class MainTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive();
        drive.init(hardwareMap);

        Intake intake = new Intake();
        intake.init(hardwareMap);

        Launcher launcher = new Launcher();
        launcher.init(hardwareMap);

        Passthrough passthrough = new Passthrough();
        passthrough.init(hardwareMap);

        Controls controls = new Controls(gamepad1, gamepad2);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            drive.drive(
                    controls.getForward(),
                    controls.getStrafe(),
                    controls.getRotate()
            );

            if (controls.getIntake()) {
                intake.intake();
            } else if (controls.getRelease()) {
                intake.release();
            } else {
                intake.stop();
            }

            if (controls.getLaunch()) {
                launcher.spinUp();
            } else if (controls.getSlowDown()) {
                launcher.reverse();
            } else {
                launcher.stop();
            }

            if (controls.getPassthroughIn()) {
                passthrough.in();
            } else if (controls.getPassthroughOut()) {
                passthrough.out();
            } else {
                passthrough.stop();
            }

            telemetry.addData("Status", "Running");
            telemetry.addData("Launcher", "%.0f ticks/s, ready=%b", launcher.getVelocity(), launcher.isReady());
            telemetry.update();
        }
    }
}