package application;

public class CarData {

	private int rpm;
	private float mph;
	private float throttlePos;
	private float fuelLevel;
	private float coolandTemp;
	
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
		return coolandTemp;
	}
	public void setCoolandTemp(float f) {
		this.coolandTemp = f;
	}
}
