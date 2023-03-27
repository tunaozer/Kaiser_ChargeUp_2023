// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
public class LedSubsystem extends SubsystemBase {
  /** Creates a new LedSubsystem. */
  static final int kPotChannel = 1;
  private static AddressableLED m_led;
  private static AddressableLEDBuffer m_ledBuffer;
  static final double kFullHeightMeters = 1.5;
  static final double[] kSetpointsMeters = {0.2, 0.8, 1.4};
  private final AnalogPotentiometer m_potentiometer=
    new AnalogPotentiometer(kPotChannel, kFullHeightMeters);
    
  
  public LedSubsystem(int dataPort, int ledLength) {
    m_led = new AddressableLED(dataPort);
    m_ledBuffer = new AddressableLEDBuffer(ledLength);
    m_led.setLength(m_ledBuffer.getLength());
    m_led.start();
  }

  @Override
  public void periodic() {
    double position = m_potentiometer.get();
    
    if(position>250&&position<350){
      setColor(0, 0, 255);
    }
    if(position>450&&position<550){
      setColor(0, 255, 0);
    }
    else{
      setColor(255, 0, 0);
    }
  }
  public void turnOff() {
    setColor(0, 0, 0);
    m_led.setData(m_ledBuffer);
}
public static void setColor(int r, int g, int b) {
  for (int i = 0; i < m_ledBuffer.getLength(); i++) {
      m_ledBuffer.setRGB(i, r, g, b);
  }
  m_led.setData(m_ledBuffer);
}

}
