package com.example.adhocproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import blendscene.BlendScene;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Logger;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

import android.content.res.AssetManager;
import android.opengl.GLSurfaceView;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

public class MyRenderer implements GLSurfaceView.Renderer {

	private SurfaceView master = null;
	private AssetManager asset = null;;
	private World world = null;;
	private Camera cam = null;
	
	private FrameBuffer fb = null;
	private BlendScene scn;
	private int fps = 0;
	private long time = System.currentTimeMillis();
	
	private float x_1 = 0.0f;
	private float y_1 = 0.0f;
	private float x_2 = 0.0f;
	private float y_2 = 0.0f;
	private float moveValue = (float)0.1;
	
	public MyRenderer(SurfaceView master, World world, AssetManager asset){
		this.master = master;
		this.world = world;
		this.asset = asset;
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		
		scn.update();

		fb.clear();
		world.renderScene(fb);
		world.draw(fb);
		fb.display();

		//fps log
		if (System.currentTimeMillis() - time >= 1000) {
			Logger.log(fps + "fps");
			fps = 0;
			time = System.currentTimeMillis();
		}
		fps++;
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(gl, width, height);

		if (master == null) {

			world = new World();

			scn = new BlendScene("media/scenes/blendscene.xml", asset, world);

			MemoryHelper.compact();

			if (master == null) {
				Logger.log("Saving master Activity!");
		//		master = sur;
			}
		}
		
		cam = world.getCamera();
	//	cam.moveCamera(Camera.CAMERA_MOVEOUT, 200);
	//	cam.moveCamera(Camera.CAMERA_MOVEUP, 300);
	//	cam.setFOV(1.5f);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
		
	}

	public void onTouchEvent(int action, float eventX, float eventY){
		
		float x = eventX;
		float y = eventY;
		
		switch(action){
		case MotionEvent.ACTION_DOWN:{
			x_1 = x;
			y_1 = y;
			System.out.println("---------> ACTION_DOWN");
		break;
		}
		
		case MotionEvent.ACTION_UP:{
			System.out.println("---------> ACTION_UP");
			x_2 = x;
			y_2 = y;
			
		/*	System.out.println("----------x> " +(x_1 - x_2) + "---------y> " + (y_1 - y_2));
			
			if(Math.abs(x_1 - x_2) > 40){
			
			if(x_1 > x_2){
				System.out.println("---------> left");
				cam.moveCamera(Camera.CAMERA_MOVELEFT, 2);
			}
			if(x_1 < x_2){
				System.out.println("---------> right");
				cam.moveCamera(Camera.CAMERA_MOVERIGHT, 2);
			}
			}else{
			if(y_1 > y_2){
				System.out.println("---------> down");
				cam.moveCamera(Camera.CAMERA_MOVEIN, 2);
			}
			if(y_1 < y_2){
				System.out.println("---------> up");
				cam.moveCamera(Camera.CAMERA_MOVEOUT, 2);
			}
			}*/
		break;
		}
		case MotionEvent.ACTION_MOVE:{
			x_2 = x;
			y_2 = y;
			
			if(x_1 > x_2){
		//		System.out.println("---------> left");
				cam.moveCamera(Camera.CAMERA_MOVELEFT,moveValue );
			}
			if(x_1 < x_2){
			//	System.out.println("---------> right");
				cam.moveCamera(Camera.CAMERA_MOVERIGHT, moveValue );
			}
			
			if(y_1 > y_2){
		//		System.out.println("---------> down");
				cam.moveCamera(Camera.CAMERA_MOVEIN, moveValue );
			}
			if(y_1 < y_2){
			//	System.out.println("---------> up");
				cam.moveCamera(Camera.CAMERA_MOVEOUT,moveValue );
			}
		break;	
		}
		}
		
	}
	

	
}
