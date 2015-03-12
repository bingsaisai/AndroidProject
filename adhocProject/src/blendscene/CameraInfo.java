package blendscene;

import com.threed.jpct.SimpleVector;

public class CameraInfo {
	private SimpleVector lookAt;
	private SimpleVector position;
	
	public CameraInfo(SimpleVector lookAt, SimpleVector position) {
		this.lookAt = lookAt;
		this.position = position;
	}

	public SimpleVector getLookAt() {
		return lookAt;
	}

	public SimpleVector getPosition() {
		return position;
	}

	public void setLookAt(SimpleVector lookAt) {
		this.lookAt = lookAt;
	}

	public void setPosition(SimpleVector position) {
		this.position = position;
	}
}
