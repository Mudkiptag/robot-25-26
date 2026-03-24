package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

public class PrintTelemetry {
    public void printTelemetry(Telemetry telemetry, Spindexer spindexer, Outtake outtake, Vision vision, PIDFController pidfController){
        telemetry.addLine("Drum States: " + Arrays.toString(spindexer.ballStates));
        telemetry.addLine("Drum Position: " + spindexer.drumServo.getPosition());
        telemetry.addLine();
        telemetry.addLine("PIDF Values");
        telemetry.addLine("Current Velocity: " + outtake.currentVelocity);
        telemetry.addLine("Target Velocity: " + outtake.targetVelocity);
        telemetry.addLine("kP: " + pidfController.kP);
        telemetry.addLine("kI: " + pidfController.kI);
        telemetry.addLine("kD: " + pidfController.kD);
        telemetry.addLine("kF: " + pidfController.kF);
        telemetry.addLine();
        telemetry.addLine("Motif Detected: " + vision.motif);
        telemetry.addLine("Distance Sensor: " + ((DistanceSensor) spindexer.intakeColorSensor).getDistance(DistanceUnit.CM));
        telemetry.addLine();
        telemetry.addLine("Timers");
        telemetry.addLine("Outtake Timer: " + outtake.outtakeTimer);
        telemetry.addLine("Drum Timer: " + spindexer.drumTimer);
        telemetry.addLine("Flick Timer: " + spindexer.flickTimer);
        telemetry.addLine("Drum Timer for Outtake: " + outtake.drumIsSwitching);
        telemetry.update();
    }
}
