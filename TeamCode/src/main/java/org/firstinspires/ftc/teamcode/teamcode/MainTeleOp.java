package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class MainTeleOp extends LinearOpMode {
    DriveTrain driveTrain;
    Intake intake;
    Outtake outtake;
    Spindexer spindexer;
    Mode mode = Mode.INTAKE;

    public String motif;

    //what the intake is doing
    public enum IntakeState {
        ON,
        OFF,
        REVERSE;
    }

    @Override
    public void runOpMode() {
        driveTrain = new DriveTrain(hardwareMap);
        intake = new Intake(hardwareMap);
        outtake = new Outtake(hardwareMap);
        spindexer = new Spindexer(hardwareMap);
        motif = "not found";
        waitForStart();
        while (opModeIsActive()){
            //set power of drive train
            driveTrain.SetDriveTrainPower(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x);
            IntakeState intakeState = IntakeState.OFF;
            spindexer.colorSensor();

            if (mode == Mode.INTAKE) {
                spindexer.drumServo.setPosition(spindexer.intakePos[spindexer.drumPocket]);
            } else if (mode == Mode.OUTTAKE){
                spindexer.drumServo.setPosition(spindexer.outtakePos[spindexer.drumPocket]);
            }
            if (spindexer.drumIsFull()){
                mode = Mode.OUTTAKE;
                //Outtake Mode
                if (gamepad2.a){
                    ShootAllBalls();
                }
                switch (intakeState){
                    case OFF:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.ON;
                        }
                    case ON:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.REVERSE;
                        }
                    case REVERSE:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.OFF;
                        }
                }
                if (intakeState == IntakeState.ON){
                    intake.runIntake();
                } else if (intakeState == IntakeState.OFF){
                    intake.stopIntake();
                } else if (intakeState == IntakeState.REVERSE){
                    intake.outtakeIntake();
                } else {
                    telemetry.addLine("ruh roh");
                }
            } else {
                mode = Mode.INTAKE;
                //switch what intake is doing by pressing x
                switch (intakeState){
                    case ON:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.REVERSE;
                        }

                    case OFF:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.ON;
                        }

                    case REVERSE:
                        if (gamepad2.xWasPressed()){
                            intakeState = IntakeState.OFF;
                        }

                }
                if (intakeState == IntakeState.ON){
                    intake.runIntake();
                } else if (intakeState == IntakeState.OFF){
                    intake.stopIntake();
                } else if (intakeState == IntakeState.REVERSE){
                    intake.outtakeIntake();
                } else {
                    telemetry.addLine("ruh roh");
                }
            }
        }
    }
    public void ShootAllBalls(){
        if (motif.equals("PPG")){
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
            spindexer.flickBall();
            //shoot
        } else if (motif.equals("PGP")){
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
        } else if (motif.equals("GPP")){
            spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
        } else {
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
            spindexer.flickBall();
            //shoot
            spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
            spindexer.flickBall();
            //shoot
        }
    }
}
