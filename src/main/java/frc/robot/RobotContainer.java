// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import java.util.List;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.PS4Controller;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.RollerSub;
import frc.robot.subsystems.IntakeSubsystem;
//import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;


/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  private final DriveSubsystem m_robotDrive = new DriveSubsystem();
  private final ClimbSubsystem m_climb =new ClimbSubsystem();
  private final IntakeSubsystem m_intake= new IntakeSubsystem();
  private final RollerSub m_intake2 =new RollerSub();
  // The driver's controller
  PS4Controller m_driverController = new PS4Controller(OIConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    // Configure default commands
    // Set the default drive command to split-stick arcade drive
    m_robotDrive.setDefaultCommand(
        // A split-stick arcade command, with forward/backward controlled by the left
        // hand, and turning controlled by the right.
        new RunCommand(
            () ->
                m_robotDrive.drive(
                    //-m_driverController.getLeftY(),
                    //-m_driverController.getRightX(),
                                                                                                                                                                       //-m_driverController.getLeftX(),
                    -m_driverController.getLeftX(),
                    m_driverController.getLeftY(),
                    m_driverController.getRawAxis(2),                                    
                    false),
            m_robotDrive));
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling passing it to a
   * {@link JoystickButton}.
   */
  private void configureButtonBindings() {
    // Drive at half speed when the right bumper is held
    new JoystickButton(m_driverController, 10)
        .onTrue(new InstantCommand(() -> m_robotDrive.setMaxOutput(0.2)))
        .onFalse(new InstantCommand(() -> m_robotDrive.setMaxOutput(0.8)));

    new JoystickButton(m_driverController, 5)
    .whileTrue(new InstantCommand (()->m_climb.climbActive()))
    .whileFalse(new InstantCommand(()->m_climb.climbStop())); 
    
    new JoystickButton(m_driverController, 6)
    .whileTrue(new InstantCommand (()->m_climb.climbDisable()))
    .whileFalse(new InstantCommand(()->m_climb.climbStop())); 


      new JoystickButton(m_driverController, 4)
      .onTrue(new InstantCommand(()->m_intake.change_Pneumatic_Intake()))
      .onFalse(new InstantCommand(()->m_intake.IntakeStopke()));


     new JoystickButton(m_driverController, 2)
     .onTrue(new InstantCommand(()->m_intake2.IntakeRoll()))
    .onFalse(new InstantCommand(()->m_intake2.IntakeStop()));
  new JoystickButton(m_driverController, 3)
     .onTrue(new InstantCommand(()->m_intake2.IntakeUnroll()))
    .onFalse(new InstantCommand(()->m_intake2.IntakeStop()));
    
 }



  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
 /* *//* */ 
public Command getAutonomousCommand(){
      TrajectoryConfig config =
        new TrajectoryConfig(
                AutoConstants.kMaxSpeedMetersPerSecond,
                AutoConstants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(DriveConstants.kDriveKinematics);

    // An example trajectory to follow.  All units in meters.
    Trajectory exampleTrajectory =
        TrajectoryGenerator.generateTrajectory(
            // Start at the origin facing the +X direction
            
            // Pass through these two interior waypoints, making an 's' curve path

            List.of(
                new Pose2d(0, 0, new Rotation2d(0)),
                new Pose2d(0, 3, new Rotation2d(0))),
                config
        );
  
    MecanumControllerCommand mecanumControllerCommand =
        new MecanumControllerCommand(
            exampleTrajectory,
            m_robotDrive::getPose,
            DriveConstants.kFeedforward,
            DriveConstants.kDriveKinematics,

            // Position contollers
            new PIDController(AutoConstants.kPXController, 0, 0),
            new PIDController(AutoConstants.kPYController, 0, 0),
            new ProfiledPIDController(
                AutoConstants.kPThetaController, 0, 0, AutoConstants.kThetaControllerConstraints),

            // Needed for normalizing wheel speeds
            AutoConstants.kMaxSpeedMetersPerSecond,

            // Velocity PID's
            new PIDController(DriveConstants.kPFrontLeftVel, 0, 0),
            new PIDController(DriveConstants.kPRearLeftVel, 0, 0),
            new PIDController(DriveConstants.kPFrontRightVel, 0, 0),
            new PIDController(DriveConstants.kPRearRightVel, 0, 0),
            m_robotDrive::getCurrentWheelSpeeds,
            m_robotDrive::setDriveMotorControllersVolts, // Consumer for the output motor voltages
            m_robotDrive);

    // Reset odometry to the starting pose of the trajectory.
    m_robotDrive.resetOdometry(exampleTrajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return new RunCommand(m_climb::climbActive, m_climb).withTimeout(1.5).andThen(new InstantCommand(m_climb::climbStop,m_climb)).andThen(new InstantCommand(m_intake::IntakeUp,m_intake).withTimeout(0.5).andThen(new InstantCommand(m_intake2::IntakeRoll,m_intake2)).withTimeout(2).andThen(mecanumControllerCommand).andThen(new InstantCommand(m_intake2::IntakeStop,m_intake2).andThen(() -> m_robotDrive.drive(0, 0, 0, false))));
    //return new RunCommand(m_climb::climbActive, m_climb).withTimeout(1.5).andThen(new InstantCommand(m_climb::climbStop,m_climb)).andThen(new InstantCommand(m_intake::IntakeUp,m_intake).withTimeout(0.5).andThen(new InstantCommand(m_intake2::IntakeRoll,m_intake2)).withTimeout(2).andThen(m_intake2::IntakeStop,m_intake2).andThen(mecanumControllerCommand2).andThen(() -> m_robotDrive.drive(0, 0, 0, false)));
  
}
  }


