package org.firstinspires.ftc.teamcode.Components.Autonomous;

import static org.firstinspires.ftc.teamcode.Components.Claw.ClawStates.CLAW_CLOSED;
import static org.firstinspires.ftc.teamcode.Components.Lift.LiftConstants.LIFT_HIGH_JUNCTION;
import static org.firstinspires.ftc.teamcode.Robots.BasicRobot.logger;

import static java.lang.Math.toRadians;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Robots.PwPRobot;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

@Config
@Autonomous(name = "BlueRightAutoCycleTuned")


public class BlueRightAutoCycleTuned extends LinearOpMode {
    private SampleMecanumDrive roadrun;

    public static double dummyP = 3;

    public static double dropX = -30, dropY =3.7, dropA = toRadians(140), dropET = toRadians(320);

    public static double pickupX1 = -46, pickupY1 = 10, pickupA1 = toRadians(180), pickupET1 = toRadians(180);
    public static double pickupX2 = -63.2, pickupY2 = 10, pickupA2 = toRadians(180), pickupET2 = toRadians(180);




    //    public static double dummyxi = -12.5, dummyyi = 55;
//    public static double dummyxi2 = -12.5, dummyyi2 = 13;
//
//
    public static double dummyx = -23.5, dummyy = 6, dummya = 270;
    public static double dummyx2 = -23.5, dummyy2 =11, dummya2 = 270;
//    public static double dummyxd = -23.5, dummyyd = 6, dummyad = 270;
//    public static double dummyx2i = -23.5, dummyy2i =11, dummya2i = 270;
//    public static double dummyx3i = -23.5, dummyy3i =9, dummya3i = 270;
//    public static double dummyx3 = -38, dummyy3 =10.1, dummya3 = 180;
//    public static double dummyx4 = -63.5, dummyy4 =10.1, dummya4 = 180;
//
//    public static double dummyX = -12, dummyY = 11, dummyA = 180;
//
    public static double dummyX2 = -35, dummyY2 = 11, dummyA2 = 180;
//
//    public static double dummyX3 = -55, dummyY3 = 11, dummyA3 = 180;
    double[] stackPos = {490, 370, 240, 120,0};

    public void runOpMode() {
        PwPRobot robot = new PwPRobot(this, false);
        robot.roadrun.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Pose2d startPose = new Pose2d(-29.6, 62.25, toRadians(90));
        robot.roadrun.setPoseEstimate(startPose);

        //detectSignal();
        //store in variable
        robot.cv.observeSleeve();
        TrajectorySequence initialtrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(-29.6, 62.25, toRadians(90)))
                .setReversed(true).splineToSplineHeading(new Pose2d(-36, 40, toRadians(70)), toRadians(250))
                .splineToSplineHeading(new Pose2d(-36, 26, toRadians(105)), toRadians(285))
                .splineToSplineHeading(new Pose2d(-28,7.9, toRadians(120)), toRadians(290))
                .build();
        TrajectorySequence pickupTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(-28,7.9, toRadians(120)))
                .splineToSplineHeading(new Pose2d(pickupX1, pickupY1, pickupA1), pickupET1)
                .splineToSplineHeading(new Pose2d(pickupX2, pickupY2, pickupA2), pickupET2)
                .build();
        TrajectorySequence dropTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(pickupX2, pickupY2, pickupA2)).setReversed(true)
                .splineToSplineHeading(new Pose2d(dropX,dropY, dropA), dropET)
                .build();
        TrajectorySequence pickupTrajectory2 = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .splineToSplineHeading(new Pose2d(-45.5, 10, toRadians(180)), toRadians(180))
                .splineToSplineHeading(new Pose2d(-63.2, 10, toRadians(180)), toRadians(180))
                .build();
        TrajectorySequence parkTrajectory = robot.roadrun.trajectorySequenceBuilder(new Pose2d(dropX,dropY, dropA))
                .splineToSplineHeading(new Pose2d(-36, 33, toRadians(90)), toRadians(90))
                .build();
        Trajectory park1trajectory = robot.roadrun.trajectoryBuilder(new Pose2d(dropX,dropY, dropA))
                .lineToConstantHeading(new Vector2d(-12, 33))
                .build();

        Trajectory park2trajectory = robot.roadrun.trajectoryBuilder(new Pose2d(dropX,dropY, dropA))
                .splineToSplineHeading(new Pose2d(-35,13, Math.toRadians(90)), Math.toRadians(90))
                .build();

        Trajectory park3trajectory = robot.roadrun.trajectoryBuilder(new Pose2d(dropX,dropY, dropA))
                .lineToLinearHeading(new Pose2d(-57, 33, toRadians(90)))
                .build();
        while (!isStarted()) {
            telemetry.addData("pos", robot.cv.getPosition());
            telemetry.addData("CLAW_CLOSED:", CLAW_CLOSED.getStatus());
            telemetry.update();
            robot.updateLiftArmStates();

        }
        resetRuntime();
        dummyP = robot.cv.getPosition();

        if (isStopRequested()) return;


        while (opModeIsActive() && !isStopRequested() && getRuntime() < 28) {
            logger.loopcounter++;
            robot.raiseLiftArmToOuttake(false);
            robot.delay(0.7);
            robot.liftToPosition(LIFT_HIGH_JUNCTION);
            robot.delay(0.2);
            robot.followTrajectorySequenceAsync(initialtrajectory);
            robot.waitForFinish();
            robot.openClaw(false);
            robot.delay(0.2);
            robot.cycleLiftArmToCycle(true);
            robot.delay(0.2);
            robot.liftToPosition((int) stackPos[0]);
            robot.delay(1.3);
            robot.wideClaw();
            robot.followTrajectorySequenceAsync(pickupTrajectory);
            robot.waitForFinish();
            robot.closeClaw(false);
            robot.waitForFinish();
            robot.delay(0.8);
            robot.liftToPosition(LIFT_HIGH_JUNCTION);
            robot.raiseLiftArmToOuttake(true);
            robot.delay(0.6);
            robot.followTrajectorySequenceAsync(dropTrajectory);
            robot.waitForFinish();
            robot.openClaw();
            for(int i=0;i<2;i++) {
                robot.delay(0.2);
                robot.cycleLiftArmToCycle(true);
                robot.delay(0.4);
                robot.liftToPosition((int) stackPos[i+1]);
                robot.delay(1.3);
                robot.wideClaw();
                robot.followTrajectorySequenceAsync(pickupTrajectory2);
                robot.waitForFinish();
                robot.closeClaw(false);
                robot.waitForFinish();
                robot.delay(0.8);
                robot.liftToPosition(LIFT_HIGH_JUNCTION);
                robot.raiseLiftArmToOuttake(true);
                robot.delay(0.6);
                robot.followTrajectorySequenceAsync(dropTrajectory);
                robot.waitForFinish();
                robot.openClaw();
            }
//            robot.followTrajectorySequenceAsync(parkTrajectory);
//            robot.delay(1);
            robot.liftToPosition(0);
            robot.lowerLiftArmToIntake(true);
//
//            if (dummyP == 1) {
//                robot.followTrajectoryAsync(park1trajectory);
//            } else if (dummyP == 3) {
//                robot.followTrajectoryAsync(park3trajectory);
//            } else {
                robot.followTrajectoryAsync(park2trajectory);
//
//            }

            robot.setFirstLoop(false);
            robot.liftToTargetAuto();
            robot.roadrun.update();
            robot.updateClawStates();
            robot.updateLiftArmStates();

        }
        robot.stop();
        if (getRuntime() > 29.8) {
            stop();
        }
    }
}
