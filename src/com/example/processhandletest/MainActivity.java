package com.example.processhandletest;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	private ActivityManager mActivityManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivityManager = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	public void getRunningProcess(View view) {

		List<RunningServiceInfo> services = mActivityManager
				.getRunningServices(100);

		for (RunningServiceInfo info : services) {
			Log.d("process", "process name: " + info.process);
		}

	}

	public void getForegroundProcess(View view) {
		// The first in the list of RunningTasks is always the foreground task.
		RunningTaskInfo foregroundTaskInfo = mActivityManager
				.getRunningTasks(1).get(0);
		String foregroundTaskPackageName = foregroundTaskInfo.topActivity
				.getPackageName();
		PackageManager pm = getPackageManager();
		PackageInfo foregroundAppPackageInfo = null;
		try {
			foregroundAppPackageInfo = pm.getPackageInfo(
					foregroundTaskPackageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo
				.loadLabel(pm).toString();
		Log.d("process", "foreground process name: " + foregroundTaskAppName);
	}

	public void killProcess(View view) {
		List<ActivityManager.RunningAppProcessInfo> list = mActivityManager
				.getRunningAppProcesses();

		if (list != null) {
			for (int i = 0; i < list.size(); ++i) {
				String processid = list.get(i).processName;

				String packageName = getPackageName();
				if (processid.contains(packageName + ":homescreen")) {
					// here homescreen is the name you given in manifest for
					// android:process

					android.os.Process.killProcess(list.get(i).pid);

				}
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
