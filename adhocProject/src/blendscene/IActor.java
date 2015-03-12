package blendscene;

import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public interface IActor {
	/**
	 * Set position to your actor (make sure to configure all objects if you
	 * have more than one). This method is called by JPCTBlend when loading
	 * scene
	 * 
	 * @param pos
	 *            Actor position
	 */
	public void setPosition(SimpleVector pos);

	/**
	 * Set rotation to your actor (make sure to configure all objects if you
	 * have more than one). This method is called by JPCTBlend when loading
	 * scene
	 * 
	 * @param pos
	 *            Actor position
	 */
	public void setRotation(SimpleVector pos);

	/**
	 * Create here your logic to add your actor to the world.
	 * 
	 * @param world
	 *            World to add the actor
	 */
	public void addToWorld(World world);

	/**
	 * Create here your logic to remove actor from world.
	 */
	public void removeFromWorld();

	/**
	 * Create here your behavior, 'act' is called at each frame, so you don't
	 * need to create a loop here. More info in the JPCTBlend Manual.
	 */
	public void act();
	
	public Object3D getObject3D();
}
