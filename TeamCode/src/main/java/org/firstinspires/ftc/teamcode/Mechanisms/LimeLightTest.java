package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous
public class LimeLightTest extends OpMode {

    private Limelight3A limelight3A;

    @Override
    public void init(){
        hardwareMap.get(Limelight3A.class, "limelight");
        // make sure matches configuration file

        limelight3A.pipelineSwitch(0); // pollen_yellow pipeline = 0
    }

    @Override
    public void start(){
        limelight3A.start();
    }

    @Override
    public void loop(){
        LLResult llResult = limelight3A.getLatestResult();

        if (llResult != null && llResult.isValid()){
            telemetry.addData("Target X offset", llResult.getTx());
            telemetry.addData("Target Y offset", llResult.getTy());
            telemetry.addData("Target Area offset", llResult.getTa());
        }



    }


}
