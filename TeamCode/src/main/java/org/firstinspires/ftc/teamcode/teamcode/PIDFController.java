package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;

public class PIDFController {
    public double kP, kI, kD, kF;
    private double lastError = 0;
    private double integralSum = 0;
    private long lastTimeStamp = System.nanoTime();
    //set PIDF values
    public PIDFController(){
        this.kF = 1;
        this.kP = 1;
        this.kI = 1;
        this.kD = 1;
    }
    //maths
    public double PIDFMath(double targetVelocity, double currentVelocity){
        long currentTimeStamp = System.nanoTime();
        double dt = (currentTimeStamp - lastTimeStamp) / 1e9; // Delta time in seconds
        lastTimeStamp = currentTimeStamp;

        double error = targetVelocity - currentVelocity;
        //proportional
        double proportional = error * kP;
        //integral
        integralSum += error * dt;
        double integral = integralSum * kI;

        //derivative
        double derivativeMath = (error - lastError) / dt;
        double derivative = derivativeMath * kD;

        //feedforward
        double feedforward = targetVelocity * kF;
        lastError = error;
        return proportional + integral + derivative + feedforward;
    }
    public void changeKs(Gamepad gamepad2, Gamepad gamepad1){
        if (gamepad2.dpad_up){
            kP = kP + 0.1;
        } else if (gamepad2.dpad_down){
            kP = kP - 0.1;
        }
        if (gamepad2.dpad_left){
            kD = kD + 0.1;
        } else if (gamepad2.dpad_right){
            kD = kD - 0.1;
        }
        if (gamepad1.dpad_up){
            kI = kI + 0.1;
        } else if (gamepad1.dpad_down){
            kI = kI - 0.1;
        }
        if (gamepad1.dpad_left){
            kF = kF + 0.1;
        } else if (gamepad1.dpad_right){
            kF = kF - 0.1;
        }
    }
}
