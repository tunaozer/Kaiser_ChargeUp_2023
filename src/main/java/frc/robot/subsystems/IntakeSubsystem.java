/// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new ClimbSub. */
  WPI_TalonSRX intake;
  
  private boolean intake_pneumatic_state = false;
  //private boolean intake_motor_state = false;

  public IntakeSubsystem() {
    intake = new WPI_TalonSRX(Constants.IntakeConstants.IntakePort);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void change_Pneumatic_Intake() {
    if(intake_pneumatic_state){
      IntakeDown();
      }
      else IntakeUp();
  }


  public void IntakeUp(){
    intake.set(ControlMode.PercentOutput,0.3);  //değiştirilmeli
    intake_pneumatic_state = true;
  }
  public void IntakeDown(){
    intake.set(ControlMode.PercentOutput,-0.3);     //bu da değişcek
    intake_pneumatic_state = false;
  }
  public void IntakeStopke(){
    intake.set(ControlMode.PercentOutput,0);
  }
  
}

