package blendscene;

import java.util.HashMap;

import com.threed.jpct.Object3D;

public class Object3DManager {
	private HashMap<String, Object3D> loadedObjects;

	private static Object3DManager instance = null;

	public static Object3DManager getInstance() {
		if (instance == null)
			instance = new Object3DManager();
		return instance;
	}

	private Object3DManager() {
		loadedObjects = new HashMap<String, Object3D>();
	}

	public Object3D cloneObject3D(String objName) {
		if (!containsObject3D(objName))
			throw new RuntimeException("Can't clone mesh " + objName
					+ " because it does not exist in Object3DManager.");

		Object3D cloned = getObject3D(objName).cloneObject();
		cloned.clearRotation();
		cloned.clearTranslation();
		cloned.setScale(1);

		return cloned;
	}

	public boolean containsObject3D(String key) {
		return loadedObjects.containsKey(key);
	}

	public Object3D getObject3D(String key) {
		if (!loadedObjects.containsKey(key))
			throw new RuntimeException(
					"Error: Object3DManager does not contains " + key);
		return loadedObjects.get(key);
	}

	public void putObject3D(String key, Object3D value) {
		if (!loadedObjects.containsKey(key))
			loadedObjects.put(key, value);
	}

	public int size() {
		return loadedObjects.size();
	}
}
