package com.detaysoft.xmppclient;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;

import android.os.AsyncTask;
import android.util.Log;


public class SendCustomIQPacketAsyncTask extends AsyncTask<IQ, Void, Void> {

    XMPPConnection connection;
    String TAG = getClass().getSimpleName();

	public SendCustomIQPacketAsyncTask(XMPPConnection connection) {
		this.connection = connection;
	}

	@Override
	protected Void doInBackground(IQ... params) {
		params[0].setType(IQ.Type.GET);
		params[0].setPacketID("pvrequest");
		if (connection.isConnected() == true) {
            connection.sendPacket(params[0]);
            Log.d(TAG,"IQ Paketi Gönderildi");
		}
		return null;
	}

}
