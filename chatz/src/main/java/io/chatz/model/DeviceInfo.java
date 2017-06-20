package io.chatz.model;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

  private static final long serialVersionUID = -917711107449374805L;

  private String model;
  private String carrier;
  private String osName;
  private String osVersion;

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getCarrier() {
    return carrier;
  }

  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  public String getOsName() {
    return osName;
  }

  public void setOsName(String osName) {
    this.osName = osName;
  }

  public String getOsVersion() {
    return osVersion;
  }

  public void setOsVersion(String osVersion) {
    this.osVersion = osVersion;
  }
}