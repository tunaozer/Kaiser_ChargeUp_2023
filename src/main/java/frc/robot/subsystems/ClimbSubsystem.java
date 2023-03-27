// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ClimbSubsystem extends SubsystemBase {
  /** Creates a new ClimbSub. */
  CANSparkMax climb1;
  CANSparkMax climb2;

  public ClimbSubsystem() {
    climb1 = new CANSparkMax(Constants.ClimbConstants.climbUstPort, MotorType.kBrushless);
    climb2 = new CANSparkMax(Constants.ClimbConstants.climbAltPort, MotorType.kBrushless);
    climb2.follow(climb1);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void climbActive(){
    climb1.set(0.5);
    climb2.set(0.5);
  }
  public void climbDisable(){
    climb1.set(-0.2);
    climb2.set(-0.2);
  }
  public void climbStop(){
    climb1.set(0.03);
    climb2.set(0.03);
  }
}

