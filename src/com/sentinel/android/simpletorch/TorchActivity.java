package com.sentinel.android.simpletorch;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class TorchActivity extends Activity {
	private TextView mInfoText;
	private Button mLightOnButton;
	private Button mLightOffButton;
	private static final String TAG = "SimpleTorch";
	private boolean camera_ready = false;
	private Camera mCamera;
	private Parameters mParams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_torch);
		Log.i(TAG, "In onCreate()");

		mInfoText = (TextView) findViewById(R.id.info_textview);
		
		if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
			mInfoText.setText("no camera found :(");
		else{
			mInfoText.setText(R.string.init);
			camera_ready = init_camera();
		}
		if (camera_ready) {
			mInfoText.setText(R.string.camera_good);
			mLightOnButton = (Button) findViewById(R.id.light_on);
			mLightOnButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Turn the light on
					turn_light_on();
				}
			});

			mLightOffButton = (Button) findViewById(R.id.light_off);
			mLightOffButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Turn the light off
					turn_light_off();
				}
			});

		} else{
			mInfoText.setText(R.string.camera_error);
		}
	}
    
    private boolean turn_light_on(){
    	Log.i(TAG,"Turning Light On");
    	mInfoText.setText(R.string.light_on);
    	
    	// set the camera parameters
    	mParams = mCamera.getParameters();
    	mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
    	mCamera.setParameters(mParams);
    	
    	// start up the preview thing to turn the light on. 
    	
    	return true;
    }
    private boolean turn_light_off(){
    	Log.i(TAG,"Turning Light Off");
    	mInfoText.setText(R.string.light_off);
    	// set the camera parameters
    	mParams = mCamera.getParameters();
    	mParams.setFlashMode(Parameters.FLASH_MODE_OFF);
    	mCamera.setParameters(mParams);
    	return true;
    }
    
    private boolean init_camera(){
    	boolean success = false;
    	Log.i(TAG,"Opening Camera");
    	if(mCamera == null)
    	{
    		try{
    			mCamera = Camera.open();
    			mParams = mCamera.getParameters();
    			success=true;
    		}
    		catch(RuntimeException e){
    			Log.e(TAG,"Error Opening Camera");
    			success=false;
    		}
    	}
    	if(mCamera == null)
    		success = false;
    	return success;
    }
    
    @Override
    protected void onPause() {
		super.onPause();
		if(mCamera != null){
			turn_light_off();
			mCamera.release();
			mCamera = null;
		}
    }
    
    @Override
    protected void onResume(){
    	super .onResume();
    	if(mCamera == null){
    		init_camera();
    	}
    }
    
}
