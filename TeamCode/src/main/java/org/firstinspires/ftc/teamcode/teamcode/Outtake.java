package org.firstinspires.ftc.teamcode.teamcode;

import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamcode.pidtuning.VelocityPIDFController;

public class Outtake {
    public DcMotorEx outtakeMotor1;
    public DcMotorEx outtakeMotor2;
    public Servo hoodServo;
    public Outtake(HardwareMap hardwareMap) {
        outtakeMotor1 = hardwareMap.get(DcMotorEx.class, "outtakeMotor1");
        outtakeMotor2 = hardwareMap.get(DcMotorEx.class, "outtakeMotor2");

        hoodServo = hardwareMap.get(Servo.class, "hood");
    }
    public void ShootAllBalls(String motif, Spindexer spindexer){
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
