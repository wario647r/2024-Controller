package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class autoDeclarations {
    // Declare OpMode members.
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFoward = null;
    public DcMotor rightReverse = null;
    public DcMotor leftReverse = null;
    public DcMotor rightFoward = null;
    public DcMotor intake = null;

    //Constructor
    public autoDeclarations() {
    }

    //Initialize standard Hardware interfaces
    public void init(HardwareMap hardwareMap) {

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFoward = hardwareMap.get(DcMotor.class, "left_foward_drive");
        rightReverse = hardwareMap.get(DcMotor.class, "right_reverse_drive");
        leftReverse = hardwareMap.get(DcMotor.class, "left_reverse_drive");
        rightFoward = hardwareMap.get(DcMotor.class, "right_foward_drive");
        intake = hardwareMap.get(DcMotor.class, "intake_intial");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFoward.setDirection(DcMotor.Direction.FORWARD);
        rightReverse.setDirection(DcMotor.Direction.REVERSE);
        leftReverse.setDirection(DcMotor.Direction.FORWARD);
        rightFoward.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.FORWARD);

        leftFoward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightReverse.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftReverse.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFoward.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFoward.setPower(0);
        rightReverse.setPower(0);
        leftReverse.setPower(0);
        rightFoward.setPower(0);
        intake.setPower(0);

        //Constants
        final double COUNTS_PER_MOTOR_REV = 1440; //Counts to rotations, testing later
        final double DRIVE_GEAR_REDUCTION = 1.0; //If gears are added
        final double WHEEL_DIAMETER_INCHES = 4.0; //Wheel size
        final double CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER_INCHES; //Circumference of wheel
        final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / CIRCUMFERENCE; //Converting counts to inches
        final double ROBOT_RADIUS = 7.5; //Get robot radius later

        //encoderDrive variable
        public void encoderDrive ( double speed, double leftInches, double rightInches, double timeoutS){
            int newLeftTarget;
            int newRightTarget;


//Checking Opmode
            if (opModeIsActive()) {
                //Find the new position
                newLeftTarget = leftFoward.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newRightTarget = rightReverse.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
                newLeftTarget = leftReverse.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
                newRightTarget = rightFoward.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

                //Turn on RUN_TO_POSITION
                leftFoward.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightReverse.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                leftReverse.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                rightFoward.setMode(DcMotor.RunMode.RUN_TO_POSITION);

                //Reset the timeout time and start motion
                runtime.reset();
                leftFoward.setPower(Math.abs(speed));
                rightReverse.setPower(Math.abs(speed));
                leftReverse.setPower(Math.abs(speed));
                rightFoward.setPower(Math.abs(speed));


                // keep looping while we are still active, and there is time left, and both motors are running.
                // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
                // its target position, the motion will stop.  This is "safer" in the event that the robot will
                // always end the motion as soon as possible.
                // However, if you require that BOTH motors have finished their moves before the robot continues
                // onto the next step, use (isBusy() || isBusy()) in the loop test.
                while (opModeIsActive() &&
                        (runtime.seconds() < timeoutS) &&
                        (leftFoward.isBusy() || rightReverse.isBusy() ||
                                leftReverse.isBusy() || rightFoward.isBusy())) {
                    //turning
                    if (leftInches < rightInches) {
                        leftFoward.setPower(-speed);
                        rightReverse.setPower(speed);
                        leftReverse.setPower(-speed);
                        rightFoward.setPower(speed);
                    } else {
                        leftFoward.setPower(speed);
                        rightReverse.setPower(-speed);
                        leftReverse.setPower(speed);
                        rightFoward.setPower(-speed);
                    }
                    //Display for the driver
                    telemetry.addData("Path1", "Running to %7d: %7d", newLeftTarget, newRightTarget);
                    telemetry.addData("Path2", "Running at %7d: %7d: %7d: %7d",
                            leftFoward.getCurrentPosition(),
                            rightReverse.getCurrentPosition(),
                            leftReverse.getCurrentPosition(),
                            rightFoward.getCurrentPosition());
                    telemetry.update();
                }
            }


            // Tell the driver that initialization is complete.
            telemetry.addData("Status", "Initialized");
        }

    }
}
