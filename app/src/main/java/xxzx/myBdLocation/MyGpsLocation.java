package xxzx.myBdLocation;

import java.io.Serializable;

public class MyGpsLocation implements Serializable {

	private double mLatitude;// 经度
	private double mLongitude;// 纬度
	private double mAltitude;// 海拔
	private float mSpeed;// 速度
	private float mBearing;// 方向
	private float mAccuracy;//精度
	private String mTime;//时间
	private float mDirection;
	private int mSatelliteNumber;
	private String	mAddrStr;
	private int mLocationType;//0表示GPS定位，1表示网络定位，2表示离线定位
	//定位描述，在什么附近
	private String mLocationDescribe;

	public String getmDescribe() {
		return mDescribe;
	}

	public void setmDescribe(String mDescribe) {
		this.mDescribe = mDescribe;
	}

	//错误描述
	private String mDescribe;

	public float getmDirection() {
		return mDirection;
	}

	public void setmDirection(float mDirection) {
		this.mDirection = mDirection;
	}

	public String getmLocationDescribe() {
		return mLocationDescribe;
	}

	public void setmLocationDescribe(String mLocationDescribe) {
		this.mLocationDescribe = mLocationDescribe;
	}

	public int getmSatelliteNumber() {
		return mSatelliteNumber;
	}

	public void setmSatelliteNumber(int mSatelliteNumber) {
		this.mSatelliteNumber = mSatelliteNumber;
	}

	public String getmAddrStr() {
		return mAddrStr;
	}

	public void setmAddrStr(String mAddrStr) {
		this.mAddrStr = mAddrStr;
	}

	public int getmLocationType() {
		return mLocationType;
	}

	public void setmLocationType(int mLocationType) {
		this.mLocationType = mLocationType;
	}


	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public double getmAltitude() {
		return mAltitude;
	}

	public void setmAltitude(double mAltitude) {
		this.mAltitude = mAltitude;
	}

	public float getmSpeed() {
		return mSpeed;
	}

	public void setmSpeed(float mSpeed) {
		this.mSpeed = mSpeed;
	}

	public float getmBearing() {
		return mBearing;
	}

	public void setmBearing(float mBearing) {
		this.mBearing = mBearing;
	}

	public float getmAccuracy() {
		return mAccuracy;
	}

	public void setmAccuracy(float mAccuracy) {
		this.mAccuracy = mAccuracy;
	}

	public String getmTime() {
		return mTime;
	}

	public void setmTime(String time) 	{
		this.mTime = time;
	}

}
