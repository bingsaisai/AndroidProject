package actors;

import java.io.IOException;

import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

import android.content.res.AssetManager;
import blendscene.IActor;

public class Actor implements IActor{

	private Object3D obj;
	private World world;
	
	public Actor(AssetManager assets) {

		try {
			obj = Loader.load3DS(assets.open("media/actors/ahri.3ds"),
					(float)(-0.02))[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
	public void setPosition(SimpleVector pos) {
		// TODO Auto-generated method stub
		obj.clearTranslation();
		obj.translate(pos);
	}

	@Override
	public void setRotation(SimpleVector rot) {
		// TODO Auto-generated method stub
		obj.clearRotation();
		obj.rotateX(rot.x);
		obj.rotateY(rot.y);
		obj.rotateZ(rot.z);
	}

	@Override
	public void addToWorld(World world) {
		// TODO Auto-generated method stub
		this.world = world;

		world.addObject(obj);
	}

	@Override
	public void removeFromWorld() {
		// TODO Auto-generated method stub
		world.removeObject(obj);
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		obj.rotateY(0.01f);
	}



	@Override
	public Object3D getObject3D() {
		// TODO Auto-generated method stub
		return obj;
	}

}
