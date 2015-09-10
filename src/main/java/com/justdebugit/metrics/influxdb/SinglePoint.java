package com.justdebugit.metrics.influxdb;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * influxdb line protocol
 * 
 * @author justdebugit
 * @see https://influxdb.com/docs/v0.9/write_protocols/write_syntax.html
 */
class SinglePoint {
  private String measurement;
  private Map<String, String> tagMap; // tag map
  private Map<String, Object> fieldMap;// field map
  private long timestamp;// 单位毫秒

  public static Builder newBuilder(String measurement) {
    assert measurement != null && measurement.length() > 0;
    return new Builder(measurement);
  }

  private SinglePoint(Builder builder) {
    this.measurement = builder.measurement;
    this.tagMap = Collections.unmodifiableMap(builder.tagMap == null ? new HashMap<String, String>() : builder.tagMap);
    this.fieldMap = Collections.unmodifiableMap(builder.fieldMap == null ? new HashMap<String, Object>() : builder.fieldMap);
    this.timestamp = builder.timestamp == 0 ? System.currentTimeMillis() : builder.timestamp;
  }

  public String getMeasurement() {
    return measurement;
  }

  public Map<String, String> getTagMap() {
    return tagMap;
  }

  public Map<String, Object> getFieldMap() {
    return fieldMap;
  }

  public long getTimestamp() {
    return timestamp;
  }

  

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(measurement);
    if (!tagMap.isEmpty()) {
      sb.append(",");
      int i = 0;
      for (Map.Entry<String, String> entry : tagMap.entrySet()) {
        if (i++>0) {
          sb.append(",");
        }
        sb.append(entry.getKey()).append("=").append(entry.getValue());
      }
    }
    if (!fieldMap.isEmpty()) {
      sb.append(" ");
      int i = 0;
      for (Map.Entry<String, Object> entry : fieldMap.entrySet()) {
        if (i++>0) {
           sb.append(",");
        }
        sb.append(entry.getKey()).append("=").append(entry.getValue());
      }
    }
    sb.append(" ").append(timestamp);
    return sb.toString();
  }



  public static class Builder {
    private String measurement;
    private Map<String, String> tagMap;
    private Map<String, Object> fieldMap;
    private long timestamp;

    private Builder(String measurement) {
      this.measurement = measurement;
      this.tagMap = new HashMap<String, String>();
      this.fieldMap = new HashMap<String, Object>();
    }


    public Builder addTag(String key, String value) {
      tagMap.put(key, value);
      return this;
    }
    
    public Builder addAllTag(Map<String, String> map){
      tagMap.putAll(map);
      return this;
    }

    public Builder addField(String key, Object value) {
      fieldMap.put(key, value);
      return this;
    }


    public Builder addAllField(Map<String, Object> map) {
      fieldMap.putAll(map);
      return this;
    }

    public Builder setTimestamp(long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public SinglePoint build() {
      return new SinglePoint(this);
    }


  }

}
