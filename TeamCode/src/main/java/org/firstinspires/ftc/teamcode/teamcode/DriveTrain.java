package org.firstinspires.ftc.teamcode.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@TeleOp
public class DriveTrain {
    private DcMotor flMotor;
    private DcMotor blMotor;
    private DcMotor brMotor;
    private DcMotor frMotor;
    public DriveTrain(HardwareMap hardwareMap) {
        flMotor = hardwareMap.get(DcMotor.class, "fl");
        blMotor = hardwareMap.get(DcMotor.class, "bl");
        brMotor = hardwareMap.get(DcMotor.class, "br");
        frMotor = hardwareMap.get(DcMotor.class, "fr");

        flMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        blMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void SetDriveTrainPower(double xPower, double yPower, double rPower){
        //magic strafing code
        double flPower = yPower + xPower + rPower;
        double frPower = yPower - xPower - rPower;
        double blPower = yPower - xPower + rPower;
        double brPower = yPower + xPower - rPower;

        flMotor.setPower(flPower);
        frMotor.setPower(frPower);
        blMotor.setPower(blPower);
        brMotor.setPower(brPower);
    }

}
