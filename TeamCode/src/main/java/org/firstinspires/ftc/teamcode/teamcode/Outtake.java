package org.firstinspires.ftc.teamcode.teamcode;

import com.arcrobotics.ftclib.util.InterpLUT;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.teamcode.pidtuning.VelocityPIDFController;

public class Outtake {
    public Servo flick;
    public DcMotorEx outtakeMotor1;
    public DcMotorEx outtakeMotor2;
    public Servo hoodServo;
    public Outtake(HardwareMap hardwareMap) {
    }
}
