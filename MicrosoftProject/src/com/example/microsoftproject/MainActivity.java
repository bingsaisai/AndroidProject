package com.example.microsoftproject;

import java.util.ArrayList;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private BluetoothAdapter mBluetoothAdapter;
	private final int REQUEST_ENABLE_BT = 1;
	private boolean mScanning;
	private Handler mHandler;
	private LeDeviceListAdapter mLeDeviceListAdapter;
	
	private static final long SCAN_PERIOD = 10000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		mHandler = new Handler();
		
		// Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
		
		// Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        
		final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        
        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
	}
		
	 @Override
	    protected void onResume() {
	        super.onResume();
	        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
	        // fire an intent to display a dialog asking the user to grant permission to enable it.
	        if (!mBluetoothAdapter.isEnabled()) {
	            if (!mBluetoothAdapter.isEnabled()) {
	                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	            }
	        }
	        // Initializes list view adapter.
	        mLeDeviceListAdapter = new LeDeviceListAdapter();
	        setListAdapter(mLeDeviceListAdapter);
	        scanLeDevice(true);
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		 if (!mScanning) {
	            menu.findItem(R.id.menu_stop).setVisible(false);
	            menu.findItem(R.id.menu_scan).setVisible(true);
	            menu.findItem(R.id.menu_refresh).setActionView(null);
	        } else {
	            menu.findItem(R.id.menu_stop).setVisible(true);
	            menu.findItem(R.id.menu_scan).setVisible(false);
	            menu.findItem(R.id.menu_refresh).setActionView(
	            		 R.layout.actionbar_indeterminate_progress);
	        }
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
         case R.id.menu_scan:
             mLeDeviceListAdapter.clear();
             scanLeDevice(true);
             break;
         case R.id.menu_stop:
             scanLeDevice(false);
             break;
     }
     return true;
	}
	
	private void scanLeDevice(final boolean enable){
		
		if(enable){
			mHandler.postDelayed(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					 invalidateOptionsMenu();
					
				}}, SCAN_PERIOD);
			
			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else{
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}
	
	private class LeDeviceListAdapter extends BaseAdapter {

		private ArrayList<BluetoothDevice> mLeDevices;
		private LayoutInflater mInflator;
		
		public LeDeviceListAdapter(){
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mInflator = MainActivity.this.getLayoutInflater();
		}
		
		public void addDevice(BluetoothDevice device) {
			if(!mLeDevices.contains(device)){
				mLeDevices.add(device);
			}
		}
		
		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}
		
		 public void clear() {
	            mLeDevices.clear();
	        }
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mLeDevices.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mLeDevices.get(position);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			ViewHolder viewHolder;
			
			if(convertView == null){
				convertView = mInflator.inflate(R.layout.listitem_device, null);
				viewHolder = new ViewHolder();
				viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
				viewHolder.deviceName = (TextView) convertView.findViewById(R.id.device_name);
				viewHolder.deviceRssi = (TextView) convertView.findViewById(R.id.device_rssi);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			BluetoothDevice device = mLeDevices.get(position);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            
            viewHolder.deviceAddress.setText(device.getAddress());
         //   viewHolder.deviceRssi.setText();
            return convertView;
			
		}	
	}
	
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = 
			new BluetoothAdapter.LeScanCallback() {
				
				@Override
				public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
					// TODO Auto-generated method stub
					
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mLeDeviceListAdapter.addDevice(device);
		                    mLeDeviceListAdapter.notifyDataSetChanged();
						}});
				}
	};
	
	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		TextView deviceRssi;
	}
}
