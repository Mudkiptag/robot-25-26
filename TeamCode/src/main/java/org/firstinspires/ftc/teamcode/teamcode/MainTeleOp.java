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

    @Override
    public void runOpMode() {
        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        spindexer = new Spindexer(hardwareMap);
        vision = new Vision(hardwareMap);

        mode = Mode.INTAKE;
        waitForStart();
        while (opModeIsActive()){
            vision.telemetryAprilTag(telemetry);
            //set power of drive train
            driveTrain.SetDriveTrainPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            vision.aprilTagDetection();
            spindexer.colorSensor();
            spindexer.setDrumState(mode);
            if (spindexer.drumIsFull()){
                mode = Mode.OUTTAKE;
                //Outtake Mode
                if (gamepad2.a){
                    outtake.ShootAllBalls(vision.motif, spindexer);
                }
                intake.setIntakeState(gamepad2, telemetry);
            } else {
                mode = Mode.INTAKE;
                intake.setIntakeState(gamepad2, telemetry);
            }
        }
    }


}
