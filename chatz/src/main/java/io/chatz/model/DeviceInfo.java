package io.chatz.model;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

  private static final long serialVersionUID = -917711107449374805L;

  private String manufacturer;
  private String model;
  private String osName;
  private String osVersion;

  public String getModel() {
    return model;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setModel(String model) {
    this.model = model;
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