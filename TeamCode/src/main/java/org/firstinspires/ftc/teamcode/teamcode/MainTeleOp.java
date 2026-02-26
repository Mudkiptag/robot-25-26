package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    DriveTrain driveTrain;
    Intake intake;
    Outtake outtake;
    Spindexer spindexer;
    public Mode mode;
    Vision vision;
    PIDFController pidfController;

    @Override
    public void runOpMode() {
        //init
        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        spindexer = new Spindexer(hardwareMap);
        vision = new Vision(hardwareMap);
        pidfController = new PIDFController();

        mode = Mode.INTAKE;
        waitForStart();
        while (opModeIsActive()){
            //print telemetry
            vision.telemetryAprilTag(telemetry);
            //set power of drive train
            driveTrain.SetDriveTrainPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            //set stuff so its always on
            pidfController.PIDFMath(outtake.targetVelocity, outtake.currentVelocity);
            vision.aprilTagDetection();
            spindexer.colorSensor();
            spindexer.setDrumState(mode);
            //change modes
            if (spindexer.drumIsFull()){
                mode = Mode.OUTTAKE;
                //Outtake Mode
                //if outtake mode, press a to shoot balls
                if (gamepad2.aWasPressed()){
                    outtake.ShootAllBalls(vision.motif, spindexer, pidfController);
                }
                intake.setIntakeState(gamepad2, telemetry);
                if (gamepad2.bWasPressed()){
                    spindexer.resetDrum();
                }
            } else {
                mode = Mode.INTAKE;
                intake.setIntakeState(gamepad2, telemetry);
                if (gamepad2.bWasPressed()){
                    spindexer.resetDrum();
                }
            }
        }
    }


}
