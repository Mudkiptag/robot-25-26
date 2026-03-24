package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    DriveTrain driveTrain;
    Intake intake;
    Outtake outtake;
    Spindexer spindexer;
    Vision vision;
    PrintTelemetry printTelemetry;
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
        printTelemetry = new PrintTelemetry();

        waitForStart();
        while (opModeIsActive()){
            //print telemetry
            vision.telemetryAprilTag(telemetry);
            //set power of drive train
            driveTrain.SetDriveTrainPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            //set stuff so its always on
            outtake.setContinuousFunctions(gamepad2, pidfController, spindexer.mode, spindexer, vision);
            pidfController.changeKs(gamepad2, gamepad1);
            pidfController.PIDFMath(outtake.targetVelocity, outtake.currentVelocity);
            vision.aprilTagDetection();
            spindexer.setContinuousFunctions(gamepad2, outtake);
            printTelemetry.printTelemetry(telemetry, spindexer, outtake, vision, pidfController);
            intake.setIntakeState(gamepad2, telemetry);
        }
    }
}
