package org.firstinspires.ftc.teamcode.teamcode;

public class PIDFController {
    private double kP, kI, kD, kF;
    private double lastError = 0;
    private double integralSum = 0;
    private long lastTimeStamp = System.nanoTime();
    //set PIDF values
    public PIDFController(){
        this.kF = 0;
        this.kP = 0;
        this.kI = 0;
        this.kD = 0;
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
}
