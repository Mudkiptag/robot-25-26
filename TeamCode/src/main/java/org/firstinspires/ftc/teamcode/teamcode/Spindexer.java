package org.firstinspires.ftc.teamcode.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

public class Spindexer {
    NormalizedColorSensor intakeColorSensor;
    ServoImplEx drumServo;
    Servo flickServo;
    ElapsedTime drumTimer = new ElapsedTime(10000);
    double drumCooldown = 0.5;
    ElapsedTime flickTimer = new ElapsedTime(10000);
    double flickCooldown = 0.3;
    public BallState[] ballStates = {BallState.EMPTY, BallState.EMPTY, BallState.EMPTY};
    public Mode mode;

    //color of ball in each pocket
    public enum BallState {
        GREEN,
        PURPLE,
        EMPTY
    }
    public double[] intakePos = {0, 0.3826, 0.7831};
    public double[] outtakePos = {0.5845, 0.98, 0.18};
    public int drumPocket = 0;
    public Spindexer(HardwareMap hardwareMap){
        drumServo = hardwareMap.get(ServoImplEx.class, "drumServo");
        drumServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        mode = Mode.INTAKE;

        flickServo = hardwareMap.get(Servo.class, "flick");
        flickServo.setPosition(0.57);
        //create color sensor and set gain (multiply values to eliminate variation
        intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, "intakeColorSensor");
        intakeColorSensor.setGain(15);

    }
    public void setContinuousFunctions(Gamepad gamepad2, Outtake outtake){
        //set flick position
        if (flickTimer.seconds() > flickCooldown) {
            flickServo.setPosition(0.57);
        } else {
            flickServo.setPosition(0.15);
        }
        //color sensor stuff
        NormalizedRGBA colors = intakeColorSensor.getNormalizedColors();
        final float[] hsvValues = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues);
        String lastBallColor;
        if (hsvValues[0] < 180) {
            lastBallColor = "green";
        } else {
            lastBallColor = "purple";
        }
        if (drumTimer.seconds() > drumCooldown) {
            //if you detect ball, remember ball color
            if (detectBallIntake()) {
                if (lastBallColor.equals("green")) {
                    ballStates[drumPocket] = BallState.GREEN;
                } else {
                    ballStates[drumPocket] = BallState.PURPLE;
                }
                setDrumNextPocket();
            }
        }
        //set drum states
        if (mode == Mode.INTAKE) {
            drumServo.setPosition(intakePos[drumPocket]);
        } else if (mode == Mode.OUTTAKE) {
            drumServo.setPosition(outtakePos[drumPocket]);
        }
        if (drumIsFull()){
            mode = Mode.OUTTAKE;
            //Outtake Mode
            //if outtake mode, press a to shoot balls
            if (gamepad2.aWasPressed()){
                outtake.outtakeTimer.reset();
                outtake.timeToShoot = true;
            }
        } else {
            mode = Mode.INTAKE;
        }
    }

    public boolean detectBallIntake() {
        return ((DistanceSensor) intakeColorSensor).getDistance(DistanceUnit.CM) < 4.5;
    }
    //set drum to next pocket, limit at 2
    public void setDrumNextPocket(){
        drumPocket = drumPocket + 1;
        if (drumPocket > 2){
            drumPocket = 2;
        }
        drumTimer.reset();
    }
//    public void resetDrum(){
//        drumPocket = 0;
//        drumTimer.reset();
//    }
    //for outtake, set to whichever color ball you want
    public void setDrumStateToBall(BallState ballState) {
        if (ballState == BallState.PURPLE) {
            drumPocket = Arrays.asList(ballStates).indexOf(BallState.PURPLE);
        } else if (ballState == BallState.GREEN) {
            drumPocket = Arrays.asList(ballStates).indexOf(BallState.GREEN);
        }
    }
    public void flickBall(){
        flickTimer.reset();
    }

    public boolean drumIsFull(){
        //if none of the drum pockets are empty, return true
        return ballStates[0] != BallState.EMPTY && ballStates[1] != BallState.EMPTY && ballStates[2] != BallState.EMPTY;
    }
}
