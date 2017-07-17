package application;

public class CarData {

	private int rpm;
	private float mph;
	private float throttlePos;
	private float fuelLevel;
	private float coolantTemp;
	private float oilTemp;
	private String gear;
	
	
	public float getOilTemp() {
		return oilTemp;
	}
	public void setOilTemp(float oilTemp) {
		this.oilTemp = oilTemp;
	}
	public String getGear() {
		return gear;
	}
	public void setGear(String gear) {
		this.gear = gear;
	}
	public int getRpm() {
		return rpm;
	}
	public void setRpm(int rpm) {
		this.rpm = rpm;
	}
	public float getMph() {
		return mph;
	}
	public void setMph(float f) {
		this.mph = f;
	}
	public float getThrottlePos() {
		return throttlePos;
	}
	public void setThrottlePos(float f) {
		this.throttlePos = f;
	}
	public float getFuelLevel() {
		return fuelLevel;
	}
	public void setFuelLevel(float f) {
		this.fuelLevel = f;
	}
	public float getCoolandTemp() {
		return coolantTemp;
	}
	public void setCoolandTemp(float f) {
		this.coolantTemp = f;
	}
}
