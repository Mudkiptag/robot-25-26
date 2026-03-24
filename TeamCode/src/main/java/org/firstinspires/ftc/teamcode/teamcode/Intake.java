package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    DcMotor intake;
    IntakeState intakeState;
    //what the intake is doing
    public enum IntakeState {
        ON,
        OFF,
        REVERSE
    }
    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotor.class, "intake");
        intakeState = IntakeState.OFF;
    }
    public void runIntake(){
        intake.setPower(1);
    }
    public void stopIntake(){
        intake.setPower(0);
    }

    public void outtakeIntake(){
        intake.setPower(-1);
    }
    //switch what intake is doing by pressing x
    public void setIntakeState(Gamepad gamepad2, Telemetry telemetry) {
        switch (intakeState) {
            case OFF:
                if (gamepad2.dpadRightWasPressed()) {
                    intakeState = IntakeState.ON;
                }
            case ON:
                if (gamepad2.dpadRightWasPressed()) {
                    intakeState = IntakeState.OFF;
                }
            case REVERSE:
                if (gamepad2.dpadRightWasPressed()) {
                    intakeState = IntakeState.OFF;
                }
        }
        switch (intakeState) {
            case OFF:
                if (gamepad2.dpadLeftWasPressed()) {
                    intakeState = IntakeState.REVERSE;
                }
            case ON:
                if (gamepad2.dpadLeftWasPressed()) {
                    intakeState = IntakeState.REVERSE;
                }
            case REVERSE:
                if (gamepad2.dpadLeftWasPressed()) {
                    intakeState = IntakeState.OFF;
                }
        }
        if (intakeState == IntakeState.ON) {
            runIntake();
        } else if (intakeState == IntakeState.OFF) {
            stopIntake();
        } else if (intakeState == IntakeState.REVERSE) {
            outtakeIntake();
        } else {
            telemetry.addLine("ruh roh");
        }
    }
}
