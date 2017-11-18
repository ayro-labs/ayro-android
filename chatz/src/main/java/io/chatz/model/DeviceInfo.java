package io.chatz.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceInfo implements Serializable {

  private static final long serialVersionUID = -917711107449374805L;

  @SerializedName("app_id")
  private String appId;
  @SerializedName("app_version")
  private String appVersion;
  @SerializedName("operating_system")
  private String operatingSystem;
  @SerializedName("manufacturer")
  private String manufacturer;
  @SerializedName("model")
  private String model;
  @SerializedName("carrier")
  private String carrier;

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getOperatingSystem() {
    return operatingSystem;
  }

  public void setOperatingSystem(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

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
}
