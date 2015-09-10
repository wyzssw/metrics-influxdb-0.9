package com.justdebugit.metrics.influxdb;

/**
 * 
 * @author justdebugit
 *
 */
public class InfluxdbException extends Exception {

  private static final long serialVersionUID = 6829972959669423437L;

  public InfluxdbException() {}

  public InfluxdbException(String msg) {
    super(msg);
  }

  public InfluxdbException(String msg, Throwable cause) {
    super(msg, cause);
  }

}
