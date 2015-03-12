package com.example.adhocproject;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import com.threed.jpct.Logger;
import com.threed.jpct.World;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class SurfaceView extends Activity{

	private static SurfaceView master = null;
	
	private GLSurfaceView mGLView;
	private MyRenderer renderer = null;
	private World world = null;
	
	private int fps = 0;
	
	private float x_1 = 0.0f;
	private float y_1 = 0.0f;
	private float x_2 = 0.0f;
	private float y_2 = 0.0f;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		if (master != null) {
			copy(master);
		}
		
		super.onCreate(savedInstanceState);
		mGLView = new GLSurfaceView(getApplication());
		
		mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {

			@Override
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				// TODO Auto-generated method stub
				
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE,16,EGL10.EGL_NONE};
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});
		
		renderer = new MyRenderer(master, world, getAssets());
		mGLView.setRenderer(renderer);
		setContentView(mGLView);
		
		mGLView.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				renderer.onTouchEvent(event.getAction(), event.getX(), event.getY());
				
		/*		float x = event.getX();
				float y = event.getY();
				
				final int action = MotionEventCompat.getActionMasked(event);
				
				switch(event.getAction()){
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
					
					System.out.println("----------x> " +(x_1 - x_2) + "---------y> " + (y_1 - y_2));
					
					
					if(x_1 > x_2)
						System.out.println("---------> left");
					if(x_1 < x_2)
						System.out.println("---------> right");
					if(y_1 > y_2)
						System.out.println("---------> down");
					if(y_1 < y_2)
						System.out.println("---------> up");
				break;
				}
				case MotionEvent.ACTION_MOVE:{
					
					
				break;	
				}
				}
				*/
				return true;
			}});
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		mGLView.onPause();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mGLView.onResume();
	}
	
	@Override
	protected void onStop(){
		super.onStop();
	}
	
	private void copy(Object src) {
		try {
			Logger.log("Copying data from master Activity!");
			Field[] fs = src.getClass().getDeclaredFields();
			for (Field f : fs) {
				f.setAccessible(true);
				f.set(this, f.get(src));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}	
