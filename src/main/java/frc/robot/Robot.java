// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  TalonSRX LwhealM = new TalonSRX(1);
  VictorSPX LwhealS = new VictorSPX(2);
  VictorSPX RwhealM = new VictorSPX(3);
  TalonSRX RwhealS = new TalonSRX(4);
  
  TalonSRX lz_tal = new TalonSRX(5);
  VictorSPX rz_vic = new VictorSPX(6);
  VictorSPX link_vic = new VictorSPX(7);
  TalonSRX rink_tal = new TalonSRX(8);
  
  Joystick jDriver = new Joystick(0);
  Joystick cDriver = new Joystick(2);
  Joystick oper = new Joystick(1);
  
  double thr = jDriver.getRawAxis(1);
  double turn = jDriver.getRawAxis(0);
  double cThr = cDriver.getRawAxis(1);
  double cRight = cDriver.getRawAxis(4);
  double cTurn = cDriver.getRawAxis(5);

  Encoder encL = new Encoder(0, 1, false);
  Encoder encR = new Encoder(3, 4, true);
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

   
  public void Arcade() {
    if (Math.abs(thr) < 0.1) {
      thr = 0.0;
    }
    if (Math.abs(turn) < 0.1) {
      turn = 0.0;
    }

    double l = -thr + turn;
    double r = -thr - turn;

    LwhealM.set(ControlMode.PercentOutput, l - 0.1);
    RwhealM.set(ControlMode.PercentOutput, r);
  }

 /*  public void ConArcade() {
    if (Math.abs(cThr) < 0.1) {
      cThr = 0.0;
    }
    if (Math.abs(cTurn) < 0.1) {
      cTurn = 0.0;
    }

    double le = -cThr + cTurn;
    double ri = -cThr - cTurn;

    LwhealM.set(ControlMode.PercentOutput, le - 0.1);
    RwhealM.set(ControlMode.PercentOutput, ri);
  }*/

 /*  public void Tank() {
    // Made For Controllers
    if (Math.abs(cThr) < 0.1) {
      cThr = 0.0;
    }
    if (Math.abs(cRight) < 0.1) {
      cRight = 0.0;
    }

    LwhealM.set(ControlMode.PercentOutput, cThr);
    RwhealM.set(ControlMode.PercentOutput, cTurn);
  }*/
  @Override
  public void robotInit() {
    rz_vic.follow(lz_tal);
    rink_tal.follow(link_vic);
    LwhealS.follow(LwhealM);
    RwhealS.follow(RwhealM);

    LwhealS.setInverted(false);
    LwhealM.setInverted(false);
    RwhealM.setInverted(true);
    RwhealS.setInverted(true);


    rz_vic.setInverted(false);
    lz_tal.setInverted(true);
    link_vic.setInverted(false);
    rink_tal.setInverted(true);


    encL.reset();
    encR.reset();
    encL.setReverseDirection(false);
    encR.setReverseDirection(true);

    rink_tal.set(ControlMode.PercentOutput, -1.0);
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    encL.reset();
    encR.reset();
  }

  @Override
  public void autonomousPeriodic() {
    SmartDashboard.putNumber("Enc left", encL.getDistance());
    SmartDashboard.putNumber("Enc Right", encR.getDistance());

    double a = encL.getDistance();
    double b = encR.getDistance();

    if (a <= 10 && b <= 10) {
      LwhealM.set(ControlMode.PercentOutput, 0.6);
      RwhealM.set(ControlMode.PercentOutput, 0.6);
    } else {
      LwhealM.set(ControlMode.PercentOutput, 0.0);
      RwhealM.set(ControlMode.PercentOutput, 0.0);

     
    }
  }

  @Override
  public void teleopInit() {}
  
  @Override
  public void teleopPeriodic() 
  {

    Arcade();

    boolean a = jDriver.getRawButton(1);
    boolean b = jDriver.getRawButton(2);        
    boolean c = jDriver.getRawButton(3);
    boolean d = jDriver.getRawButton(4);    

    // Zavit
    if(a)
    {
      lz_tal.set(ControlMode.PercentOutput, 1.0);
      rz_vic.set(ControlMode.PercentOutput, 1.0);
    }
    else
    {
      lz_tal.set(ControlMode.PercentOutput, 0.0);
      rz_vic.set(ControlMode.PercentOutput, 0.0);
    }
    if(b)
    {
      lz_tal.set(ControlMode.PercentOutput, -1.0);
      rz_vic.set(ControlMode.PercentOutput, -1.0);
    }
    else
    {
      lz_tal.set(ControlMode.PercentOutput, 0.0);
      rz_vic.set(ControlMode.PercentOutput, 0.0);
    }

    // INTAKE DA BUSSY
    if(c)
    {
      rink_tal.set(ControlMode.PercentOutput, 1.0);
      link_vic.set(ControlMode.PercentOutput, 1.0);}
    else
    {
      rink_tal.set(ControlMode.PercentOutput, 0.0);
      link_vic.set(ControlMode.PercentOutput, 0.0);
    }
    if(d)
    {
      rink_tal.set(ControlMode.PercentOutput, -1.0);
      link_vic.set(ControlMode.PercentOutput, -1.0);    
    }
    else
    {
      rink_tal.set(ControlMode.PercentOutput, 0.0);
      link_vic.set(ControlMode.PercentOutput, 0.0);
    }    
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
