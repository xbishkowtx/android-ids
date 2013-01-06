package pl.edu.agh.ids.monitors;

import java.io.File;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallMonitor extends Monitor {

	public static final String FILENAME = "calls.csv";
	public static final String HEADER = "timestamp,number,incoming\n";
	public static final int OUTGOING = 0;
	public static final int INCOMING = 1;

	private TelephonyManager telephonyManager;
	private OutgoingCallReceiver outgoingCallReceiver;

	public CallMonitor(Service parentService, File dir) {
		super(parentService, dir);
	}

	@Override
	protected String getFileName() {
		return FILENAME;
	}

	@Override
	protected String getHeader() {
		return HEADER;
	}

	@Override
	public void start() {
		telephonyManager = (TelephonyManager) parentService.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new IncomingCallListener(), PhoneStateListener.LISTEN_CALL_STATE);
		outgoingCallReceiver = new OutgoingCallReceiver();
		parentService.getApplicationContext().registerReceiver(outgoingCallReceiver, new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL));
	}

	protected class IncomingCallListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					break;
				case TelephonyManager.CALL_STATE_RINGING:
					buffer.append(System.currentTimeMillis() + "," + incomingNumber + "," + INCOMING + "\n");
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					break;
			}
		}
	}

	protected class OutgoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			buffer.append(System.currentTimeMillis() + "," + outgoingNumber + "," + OUTGOING + "\n");
		}

	}
}
