package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

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
        Queue<Spindexer.BallState> shootQueue = new PriorityQueue<>();
        Iterator<Spindexer.BallState> iterator = shootQueue.iterator();
        if (gamepad2.right_bumper){
            targetVelocity = targetVelocity + 10;
        } else if (gamepad2.left_bumper){
            targetVelocity = targetVelocity - 10;
        }
        if (mode == Mode.OUTTAKE) {
            setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
            if (timeToFlick){
                spindexer.flickBall();
            }
        }
        if (drumIsSwitching.seconds() > 0.6 && drumIsSwitching.seconds() < 0.65){
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

        if (gamepad2.aWasPressed()) {
            setQueue(vision.motif, shootQueue);
            while (iterator.hasNext()){
                if (shootQueue.size() == 3) {
                    shootQueue.remove();
                    if (iterator.next() == Spindexer.BallState.PURPLE) {
                        shootPurple(spindexer);
                    } else if (iterator.next() == Spindexer.BallState.GREEN) {
                        shootGreen(spindexer);
                    }
                }
            }
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
    public void setQueue(String motif, Queue<Spindexer.BallState> shootQueue){
        switch (motif) {
            case "PPG":
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.GREEN);
//                if (lastBallShot == Spindexer.BallState.EMPTY) {
//                    shootPurple(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.PURPLE){
//                    shootPurple(spindexer);
//                }
                break;
            case "PGP":
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.GREEN);
                shootQueue.add(Spindexer.BallState.PURPLE);
//                if (lastBallShot == Spindexer.BallState.EMPTY) {
//                    shootPurple(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.PURPLE){
//                    shootGreen(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.GREEN){
//                    shootPurple(spindexer);
//                }
                break;
            case "GPP":
                shootQueue.add(Spindexer.BallState.GREEN);
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.PURPLE);

//                if (lastBallShot == Spindexer.BallState.EMPTY) {
//                    shootGreen(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.PURPLE){
//                    shootPurple(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.GREEN){
//                    shootPurple(spindexer);
//                }
                break;
            default:
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.PURPLE);
                shootQueue.add(Spindexer.BallState.GREEN);
//                if (lastBallShot == Spindexer.BallState.EMPTY) {
//                    shootPurple(spindexer);
//                } else if (lastBallShot == Spindexer.BallState.PURPLE){
//                    shootPurple(spindexer);
//                }
                break;
        }
    }
}
