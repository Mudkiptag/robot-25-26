package org.firstinspires.ftc.teamcode.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

public class Spindexer {
    NormalizedColorSensor intakeColorSensor;
    ServoImplEx drumServo;
    Servo flickServo;
    public BallState[] ballStates = {BallState.EMPTY, BallState.EMPTY, BallState.EMPTY};
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

        flickServo = hardwareMap.get(Servo.class, "flick");
        flickServo.setPosition(0);

        intakeColorSensor = hardwareMap.get(NormalizedColorSensor.class, "intakeColorSensor");
        intakeColorSensor.setGain(15);

    }
    public void colorSensor(){
        NormalizedRGBA colors = intakeColorSensor.getNormalizedColors();
        final float[] hsvValues = new float[3];
        Color.colorToHSV(colors.toColor(), hsvValues);
        String lastBallColor;
        if (hsvValues[0] < 180) {
            lastBallColor = "green";
        } else {
            lastBallColor = "purple";
        }
        if (detectBallIntake()){
            if (lastBallColor.equals("green")){
                ballStates[drumPocket] = BallState.GREEN;
            } else if (lastBallColor.equals("purple")){
                ballStates[drumPocket] = BallState.PURPLE;
            }
            setDrumNextIntake();
        }
    }

    public boolean detectBallIntake() {
        if (((DistanceSensor) intakeColorSensor).getDistance(DistanceUnit.CM) < 4){
            return true;
        }
        return false;
    }
    public void setDrumNextIntake(){
        drumPocket = drumPocket + 1;
        if (drumPocket > 2){
            drumPocket = 2;
        }
    }
    public void setDrumStateToBall(BallState ballState){
        if (ballState == BallState.PURPLE) {
            drumPocket = Arrays.asList(ballState).indexOf(BallState.PURPLE);
        } else if (ballState == BallState.GREEN){
            drumPocket = Arrays.asList(ballState).indexOf(BallState.GREEN);
        }
    }
    public void flickBall(){
        flickServo.setPosition(1);
        flickServo.setPosition(0);
    }

    public boolean drumIsFull(){
        if (ballStates[0] != BallState.EMPTY && ballStates[1] != BallState.EMPTY && ballStates[2] != BallState.EMPTY){
            return true;
        }
        return false;
    }
    public void setDrumState(Mode mode) {
        if (mode == Mode.INTAKE) {
            drumServo.setPosition(intakePos[drumPocket]);
        } else if (mode == Mode.OUTTAKE) {
            drumServo.setPosition(outtakePos[drumPocket]);
        }
    }
}
