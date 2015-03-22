package com.example.microsoftproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
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
	private Timer timer;
	private TimerTask Ttask;
	private int timercount = 0;
	
	private static final long TIMERSCAN_PERIOD = 10;     // the time period of timerTask 
	private static final long SCAN_PERIOD = TIMERSCAN_PERIOD * 1000;
	
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
  /*      if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        */
        timer = new Timer();
        
        Ttask = new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(timercount == 0){
				 
			        if (!mLeDeviceListAdapter.isEmpty())
			        	mLeDeviceListAdapter.clear();
			        
					scanLeDevice(true);
				//	mBluetoothAdapter.startDiscovery();
					System.out.println("------------> START: " + timercount);
					
				}
				
				if(timercount == TIMERSCAN_PERIOD){
					timercount = 0;	
					scanLeDevice(false);
				//	mBluetoothAdapter.cancelDiscovery();
					System.out.println("------------> STOP: " + timercount);
				}else{
					timercount++;
				}
			}};
			
			
			
		/*	
			IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
			BroadcastReceiver mReceiver = new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					String action = intent.getAction();
					System.out.println("------------> SCAN found device: ");
					if(BluetoothDevice.ACTION_FOUND.equals(action)){
						
						BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
						
						short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
						System.out.println("------------> SCAN found device: " + device.getName() + " | " + device.getAddress() + " | " + rssi);
						
						if(device.getBondState() != BluetoothDevice.BOND_BONDED){
							
						//	short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
						//	System.out.println("------------> SCAN found device: " + device.getName() + " | " + device.getAddress() + " | " + rssi);
						//	
						}
						
					}
				}
				
			};
			
			registerReceiver(mReceiver, filter);
        */
	}
		
	 @Override
	    protected void onResume() {
	        super.onResume();
	        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
	        // fire an intent to display a dialog asking the user to grant permission to enable it.
	   /*     if (!mBluetoothAdapter.isEnabled()) {
	            if (!mBluetoothAdapter.isEnabled()) {
	                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	            }
	        } */
	        // Initializes list view adapter.
	        mLeDeviceListAdapter = new LeDeviceListAdapter();
	        setListAdapter(mLeDeviceListAdapter);
	       // scanLeDevice(true);
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
          //   mLeDeviceListAdapter.clear();
          //   scanLeDevice(true);
            timer.schedule(Ttask, 0, 1000);
             break;
         case R.id.menu_stop:
          //   scanLeDevice(false);
        	 timer.cancel();
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
		private ArrayList<String> mLeDevicesRssi;
		private LayoutInflater mInflator;
		
		public LeDeviceListAdapter(){
			super();
			mLeDevices = new ArrayList<BluetoothDevice>();
			mLeDevicesRssi = new ArrayList<String>();
			mInflator = MainActivity.this.getLayoutInflater();
		}
		
		public void addDevice(BluetoothDevice device, String rssi) {
			if(!mLeDevices.contains(device)){
				mLeDevices.add(device);
				mLeDevicesRssi.add(rssi);
			}
		}
		
		public BluetoothDevice getDevice(int position) {
			return mLeDevices.get(position);
		}
		
		public String getRSSI(int position){
			return mLeDevicesRssi.get(position);
		}
		
		 public void clear() {
	            mLeDevices.clear();
	            mLeDevicesRssi.clear();
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
			String rssi = mLeDevicesRssi.get(position);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            
            viewHolder.deviceAddress.setText(device.getAddress());
            viewHolder.deviceRssi.setText("rssi: " + rssi + " dbm");
            
            try {
				RecordIntoFile(device.getAddress(), deviceName, rssi);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
         //   viewHolder.deviceRssi.setText();
            return convertView;
			
		}	
	}
	
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = 
			new BluetoothAdapter.LeScanCallback() {
				
				@Override
				public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
					// TODO Auto-generated method stub
					
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							
							System.out.println("ComleteName: " + AdvertisingData.getCompleteName(scanRecord));
							System.out.println("Name: " + AdvertisingData.getName(scanRecord));
							System.out.println("TxPower: " + AdvertisingData.getTxPowerLevel(scanRecord));
							System.out.println("RSSI: " + rssi);
							
							mLeDeviceListAdapter.addDevice(device, String.valueOf(rssi));
		                    mLeDeviceListAdapter.notifyDataSetChanged();
		                    
		                    
						}});
				}
	};
	
	public static void RecordIntoFile(String deviceAddress, String deviceName, String rssi) throws IOException{
		
		File file = new File(Environment.getExternalStorageDirectory(), "BLERecord.txt");
		if(!file.exists())
			file.createNewFile();
		
		FileWriter fw = new FileWriter(file, true);
		String data = getDateCurrentTimeZone(System.currentTimeMillis()) + "\t" + deviceAddress  + "\t" + deviceName  + "\t" + rssi + "\n";
		fw.write(data);
		fw.flush();
		fw.close();
	}
	
	public static String getDateCurrentTimeZone(long timestamp) {
		try {
			Calendar calendar = Calendar.getInstance();
			long timeInMillis = timestamp;
			calendar.setTimeInMillis(timeInMillis);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			Date currenTimeZone = (Date) calendar.getTime();
			return sdf.format(currenTimeZone);
		} catch (Exception e) {
		}
		return "";
	}
	
	static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		TextView deviceRssi;
	}
}
