/**
 * 
 */
package com.justdebugit.metrics.influxdb;


/**
 * @author justdebugit
 *
 */
public interface Influxdb{
  /**
   * 写库，返回成功or失败
   * 
   * @return 
   * @throws InfluxdbException
   */
  public void    writeData(SinglePoint singlePoint) throws InfluxdbException;
  
  /**
   * 写完调用flush清理缓冲
   * 
   * @throws InfluxdbException
   */
  public void    flush() throws InfluxdbException;
  
}
