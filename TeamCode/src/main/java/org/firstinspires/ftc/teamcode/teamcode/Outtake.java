package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Outtake {
    public DcMotorEx outtakeMotor1;
    public DcMotorEx outtakeMotor2;
    public Servo hoodServo;
    public double targetVelocity;
    public double currentVelocity = getCurrentVelocity();
    ElapsedTime drumIsSwitching = new ElapsedTime(10000);
    ElapsedTime outtakeTimer = new ElapsedTime(10000);
    private final double outtakeCooldown;
    public Spindexer.BallState lastBallShot;
    public boolean timeToShoot;
    public boolean timeToFlick;
    public boolean ballWasShotRecently;

    public Outtake(HardwareMap hardwareMap) {
        outtakeMotor1 = hardwareMap.get(DcMotorEx.class, "outtake1");
        outtakeMotor2 = hardwareMap.get(DcMotorEx.class, "outtake2");
        outtakeMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        lastBallShot = Spindexer.BallState.EMPTY;
        targetVelocity = 1000;
        outtakeCooldown = 0.5;
        timeToShoot = false;
        timeToFlick = false;
        ballWasShotRecently = false;
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
    }
    public void setContinuousFunctions(Gamepad gamepad2, PIDFController pidfController, Mode mode, Spindexer spindexer, Vision vision){
        if (gamepad2.right_bumper){
            targetVelocity = targetVelocity + 10;
        } else if (gamepad2.left_bumper){
            targetVelocity = targetVelocity - 10;
        }
        if (mode == Mode.OUTTAKE) {
            setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
            if (timeToShoot && !ballWasShotRecently){
                shootNextBall(vision.motif, spindexer);
            }
            if (spindexer.flickTimer.seconds() > spindexer.flickCooldown && spindexer.flickTimer.seconds() < 2){
                outtakeTimer.reset();
                timeToShoot = false;
            }
            if (timeToFlick){
                spindexer.flickBall();
            }
        }
        if (drumIsSwitching.seconds() > 0.5 && drumIsSwitching.seconds() < 0.6){
            if (!timeToFlick) {
                timeToFlick = true;
            }
        }
        if (mode == Mode.INTAKE){
            lastBallShot = Spindexer.BallState.EMPTY;
        }
        if (drumIsSwitching.seconds() == 0){
            ballWasShotRecently = true;
        } else if (spindexer.flickTimer.seconds() > 0.4){
            ballWasShotRecently = false;
        }
    }
    //find current velocity for PIDF
    private double getCurrentVelocity(){
        if (outtakeMotor1 != null){
            return outtakeMotor1.getVelocity();
        }
        return 0;
    }
    //call to set run outtake
    private void setOuttakePower(double power){
        outtakeMotor1.setPower(power);
        outtakeMotor2.setPower(power);
    }

    //shoot a purple ball if there is one, if not shoot a green
    private void shootPurple(Spindexer spindexer){
        for (Spindexer.BallState ballState : spindexer.ballStates) {
            if (ballState == Spindexer.BallState.PURPLE) {
                spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
                drumIsSwitching.reset();
                timeToFlick = false;
            } else {
                spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
                drumIsSwitching.reset();
                timeToFlick = false;
            }
            lastBallShot = Spindexer.BallState.PURPLE;
            spindexer.ballStates[spindexer.drumPocket] = Spindexer.BallState.EMPTY;
            break;
        }
    }
    //shoot a green ball if there is one, if not shoot a purple
    private void shootGreen(Spindexer spindexer){
        for (Spindexer.BallState ballState : spindexer.ballStates) {
            if (ballState == Spindexer.BallState.GREEN) {
                spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
                drumIsSwitching.reset();
                timeToFlick = false;
            } else {
                spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
                drumIsSwitching.reset();
                timeToFlick = false;
            }
            lastBallShot = Spindexer.BallState.GREEN;
            spindexer.ballStates[spindexer.drumPocket] = Spindexer.BallState.EMPTY;
            break;
        }
    }
    //shoot balls in order based on motif
    public void shootNextBall(String motif, Spindexer spindexer){
        switch (motif) {
            case "PPG":
                if (lastBallShot == Spindexer.BallState.EMPTY) {
                    shootPurple(spindexer);
                } else if (lastBallShot == Spindexer.BallState.PURPLE){
                    shootPurple(spindexer);
                }
                break;
            case "PGP":
                if (lastBallShot == Spindexer.BallState.EMPTY) {
                    shootPurple(spindexer);
                } else if (lastBallShot == Spindexer.BallState.PURPLE){
                    shootGreen(spindexer);
                } else if (lastBallShot == Spindexer.BallState.GREEN){
                    shootPurple(spindexer);
                }
                break;
            case "GPP":
                if (lastBallShot == Spindexer.BallState.EMPTY) {
                    shootGreen(spindexer);
                } else if (lastBallShot == Spindexer.BallState.PURPLE){
                    shootPurple(spindexer);
                } else if (lastBallShot == Spindexer.BallState.GREEN){
                    shootPurple(spindexer);
                }
                break;
            default:
                if (lastBallShot == Spindexer.BallState.EMPTY) {
                    shootPurple(spindexer);
                } else if (lastBallShot == Spindexer.BallState.PURPLE){
                    shootPurple(spindexer);
                }
                break;
        }
    }
}
