package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Outtake {
    public DcMotorEx outtakeMotor1;
    public DcMotorEx outtakeMotor2;
    public Servo hoodServo;
    public double targetVelocity;
    public double currentVelocity = getCurrentVelocity();
    public Outtake(HardwareMap hardwareMap) {
        outtakeMotor1 = hardwareMap.get(DcMotorEx.class, "outtake1");
        outtakeMotor2 = hardwareMap.get(DcMotorEx.class, "outtake2");
        outtakeMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor1.setDirection(DcMotorSimple.Direction.REVERSE);

        targetVelocity = 0;

        hoodServo = hardwareMap.get(Servo.class, "hoodServo");
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
    private void shootPurple(Spindexer spindexer, PIDFController pidfController){
        for (Spindexer.BallState ballState : spindexer.ballStates) {
            if (ballState == Spindexer.BallState.PURPLE){
                spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
                spindexer.flickBall();
                setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
                break;
            } else {
                spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
                spindexer.flickBall();
                setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
                break;
            }
        }
    }
    //shoot a green ball if there is one, if not shoot a purple
    private void shootGreen(Spindexer spindexer, PIDFController pidfController){
        for (Spindexer.BallState ballState : spindexer.ballStates) {
            if (ballState == Spindexer.BallState.GREEN){
                spindexer.setDrumStateToBall(Spindexer.BallState.GREEN);
                spindexer.flickBall();
                setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
                break;
            } else {
                spindexer.setDrumStateToBall(Spindexer.BallState.PURPLE);
                spindexer.flickBall();
                setOuttakePower(pidfController.PIDFMath(targetVelocity, currentVelocity));
                break;
            }
        }
    }
    //shoot balls in order based on motif
    public void ShootAllBalls(String motif, Spindexer spindexer, PIDFController pidfController){
        if (motif.equals("PPG")) {
            shootPurple(spindexer, pidfController);
            shootPurple(spindexer, pidfController);
            shootGreen(spindexer, pidfController);
        } else if (motif.equals("PGP")) {
            shootPurple(spindexer, pidfController);
            shootGreen(spindexer, pidfController);
            shootPurple(spindexer, pidfController);
        } else if (motif.equals("GPP")) {
            shootGreen(spindexer, pidfController);
            shootPurple(spindexer, pidfController);
            shootPurple(spindexer, pidfController);
        } else {
            shootPurple(spindexer, pidfController);
            shootPurple(spindexer, pidfController);
            shootGreen(spindexer, pidfController);
        }
    }
}
