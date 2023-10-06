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
import edu.wpi.first.wpilibj.Timer;
import com.kauailabs.navx.frc.AHRS;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  TalonSRX lb_motor = new TalonSRX(1);
  VictorSPX lf_motor = new VictorSPX(2);
  VictorSPX rf_motor = new VictorSPX(3);
  VictorSPX rb_motor = new VictorSPX(4);
  VictorSPX l_intake = new VictorSPX(7);
  TalonSRX r_intake = new TalonSRX(8);

  Joystick driver = new Joystick(0);
  Joystick oper = new Joystick(1);

  AHRS navX = new AHRS();
  Timer timer = new Timer();

  Encoder encLeft = new Encoder(0, 1, false);
  Encoder encRight = new Encoder(2, 3, true);

  private final boolean fireButton = oper.getRawButton(1);
  private final boolean inTakeButton = oper.getRawButton(2);
  private static double driveDistance;
  private static double l_Distance;
  private static double r_Distance;
  private static double Angle = 10;
  private final double ly_thr = driver.getRawAxis(1);
  private final double rx_turn = driver.getRawAxis(4);
  private final double ry_thr = driver.getRawAxis(5);

  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */

  public void Shoot(double power) {
    l_intake.set(ControlMode.PercentOutput, power);
    r_intake.set(ControlMode.PercentOutput, power);
  }

  public void Drive(double power) {
    lf_motor.set(ControlMode.PercentOutput, power);
    lb_motor.set(ControlMode.PercentOutput, power);
    rb_motor.set(ControlMode.PercentOutput, power);
    rf_motor.set(ControlMode.PercentOutput, power);
  }

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
    lb_motor.set(ControlMode.PercentOutput, l);
    rb_motor.set(ControlMode.PercentOutput, r);
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

  public void WallRideAuto(double a, double b, double dist) {
    if (timer.get() < 1) {
      Shoot(-0.8);
    } else {
      if (a >= dist && b >= dist) {
        Drive(-0.8);
        Shoot(0.0);
      } else {
        Drive(0.0);
        Shoot(0.0);
      }
    }
  }

  public void ChargeStationAuto(double Angle) {
    if (timer.get() < 1) {
      Shoot(-1.0);
    } else if (timer.get() > 1) {
      if (timer.get() < 2) {
        Drive(-1);
        Shoot(0.0);
      } else if (navX.getRoll() > Angle) {
        Drive(0.5);
        Shoot(0.0);
      } else if (navX.getRoll() < -Angle) {
        Drive(-0.5);
        Shoot(0.0);
      } else {
        Drive(0.0);
        Shoot(0.0);
      }
    }
  }

  @Override
  public void robotInit() {
    lb_motor.setInverted(false);
    lf_motor.setInverted(false);
    rf_motor.setInverted(true);
    rb_motor.setInverted(true);

    r_intake.setInverted(false);
    l_intake.setInverted(true);

    encLeft.reset();
    encRight.reset();
    encLeft.setReverseDirection(false);
    encRight.setReverseDirection(true);
    encLeft.setDistancePerPulse(6 / 256.0);
    encRight.setDistancePerPulse(6 / 256.0);
    timer.restart();
  }

  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Enc left", encLeft.getDistance());
    SmartDashboard.putNumber("Enc Right", encRight.getDistance());
    SmartDashboard.putNumber("Timer", timer.get());
    SmartDashboard.putNumber("Robot Roll", navX.getRoll());
  }

  @Override
  public void autonomousInit() {
    encLeft.reset();
    encRight.reset();
    timer.restart();
    navX.reset();
  }

  @Override
  public void autonomousPeriodic() {
    l_Distance = encLeft.getDistance();
    r_Distance = encRight.getDistance();
    driveDistance = -10;
    Angle = 10;

    WallRideAuto(l_Distance, r_Distance, driveDistance);
    // ChargeStationAuto(Angle);
  }

  @Override
  public void teleopInit() {
    timer.restart();
  }

  @Override
  public void teleopPeriodic() {
    Arcade(ly_thr, rx_turn);
    // Tank(ly_thr, ry_thr);

    if (fireButton) {
      Shoot(1.0);
    } else if (inTakeButton) {
      Shoot(-0.6);
    } else {
      Shoot(0.0);
    }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
    // read stick value and assign to value

    double stick = oper.getRawAxis(1);

    if (oper.getRawButton(7)) {
      l_intake.set(ControlMode.PercentOutput, stick);
    } else {
      l_intake.set(ControlMode.PercentOutput, 0.0);
    }

    if (oper.getRawButton(8)) {
      r_intake.set(ControlMode.PercentOutput, stick);
    } else {
      r_intake.set(ControlMode.PercentOutput, 0.0);
    }

    // if (oper.getRawButton(9)) {
    // l_kipul.set(ControlMode.PercentOutput, stick);
    // } else {
    // l_kipul.set(ControlMode.PercentOutput, 0.0);
    // }

    // if (oper.getRawButton(10)) {
    // r_kipul.set(ControlMode.PercentOutput, stick);
    // } else {
    // r_kipul.set(ControlMode.PercentOutput, 0.0);
    // }

    if (oper.getRawButton(1)) {
      r_intake.set(ControlMode.PercentOutput, stick);
      l_intake.set(ControlMode.PercentOutput, stick);
    } else {
      r_intake.set(ControlMode.PercentOutput, 0.0);
      l_intake.set(ControlMode.PercentOutput, 0.0);
    }
  }

  @Override
  public void simulationInit() {
  }

  @Override
  public void simulationPeriodic() {
  }
}
