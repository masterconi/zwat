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
import com.ctre.phoenix.motorcontrol.can.VictorSPX;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  VictorSPX lb_motor = new VictorSPX(1);
  VictorSPX lf_motor = new VictorSPX(2);
  VictorSPX rf_motor = new VictorSPX(3);
  TalonSRX rb_motor = new TalonSRX(4);
  
  TalonSRX lz_tal = new TalonSRX(5);
  VictorSPX rz_vic = new VictorSPX(6);
  VictorSPX link_vic = new VictorSPX(7);
  TalonSRX rink_tal = new TalonSRX(8);
  
  Joystick driver = new Joystick(0);
  Joystick oper = new Joystick(1);

  Encoder encL = new Encoder(0, 1, false);
  Encoder encR = new Encoder(2, 3, true);
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

   
  public void Arcade(double thr, double turn) {
    if (Math.abs(thr - 0.05) < 0.1) {
      thr = 0.0;
    }
    if (Math.abs(turn + 0.1) < 0.1) {
      turn = 0.0;
    }

    double l = -thr + turn;
    double r = -thr - turn;

    lf_motor.set(ControlMode.PercentOutput, l);
    rf_motor.set(ControlMode.PercentOutput, r);
  }

  public void Tank(double y1, double y2) {
    // Made For Controllers
    if (Math.abs(y1) < 0.1) {
      y1 = 0.0;
    }
    if (Math.abs(y2) < 0.1) {
      y2 = 0.0;
    }

    lf_motor.set(ControlMode.PercentOutput, y1);
    rf_motor.set(ControlMode.PercentOutput, y2);
    }

  @Override
  public void robotInit() {
    rz_vic.follow(lz_tal);
    rink_tal.follow(link_vic);
    lb_motor.follow(lf_motor);
    rb_motor.follow(rf_motor);

    lb_motor.setInverted(false);
    lf_motor.setInverted(false);
    rf_motor.setInverted(true);
    rb_motor.setInverted(true);

    lz_tal.setInverted(true);
    link_vic.setInverted(false);
    rink_tal.setInverted(true);
    rz_vic.setInverted(false);

    encL.reset();
    encR.reset();
    encL.setReverseDirection(false);
    encR.setReverseDirection(true);
    encL.setDistancePerPulse(1/11.6);
    encR.setDistancePerPulse(1/11.6);
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Enc left", encL.getDistance());
    SmartDashboard.putNumber("Enc Right", encR.getDistance());
  }

  @Override
  public void autonomousInit() {
    encL.reset();
    encR.reset();
  }

  @Override
  public void autonomousPeriodic() {
    double a = encL.getDistance();
    double b = encR.getDistance();

  if (a <= 10 && b <= 10) {
      lf_motor.set(ControlMode.PercentOutput, 0.6);
      rf_motor.set(ControlMode.PercentOutput, 0.6);
    }
  else {
      lf_motor.set(ControlMode.PercentOutput, 0.0);
      rf_motor.set(ControlMode.PercentOutput, 0.0);
    }
  }

  @Override
  public void teleopInit() {}
  
  @Override
  public void teleopPeriodic() 
  {
    double ly_thr = driver.getRawAxis(1);
    double rx_turn = driver.getRawAxis(4);
    //double ry_thr= driver.getRawAxis(5);
    
    Arcade(ly_thr, rx_turn);
    //Tank(ly_thr,ry_thr);

    boolean a = oper.getRawButton(11);
    boolean b = oper.getRawButton(12);        
    boolean c = oper.getRawButton(3);
    boolean d = oper.getRawButton(4);    

    // Angel Control System : Up
    if(a){
      lz_tal.set(ControlMode.PercentOutput, 0.5);
      rz_vic.set(ControlMode.PercentOutput, 0.5);
    }
    else{
      lz_tal.set(ControlMode.PercentOutput, 0.0);
      rz_vic.set(ControlMode.PercentOutput, 0.0);
    }
    // Angel Control System : Down
    if(b){
      lz_tal.set(ControlMode.PercentOutput, -0.5);
      rz_vic.set(ControlMode.PercentOutput, -0.5);
    }
    else{
      lz_tal.set(ControlMode.PercentOutput, 0.0);
      rz_vic.set(ControlMode.PercentOutput, 0.0);
    }
    // Outake
    if(c){
      rink_tal.set(ControlMode.PercentOutput, 0.5);
      link_vic.set(ControlMode.PercentOutput, 0.5);}
    else{
      rink_tal.set(ControlMode.PercentOutput, 0.0);
      link_vic.set(ControlMode.PercentOutput, 0.0);
    }
    // Intake
    if(d){
      rink_tal.set(ControlMode.PercentOutput, -0.5);
      link_vic.set(ControlMode.PercentOutput, -0.5);    
    }
    else{
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
