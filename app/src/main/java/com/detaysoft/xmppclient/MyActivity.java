package com.detaysoft.xmppclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.google.gson.JsonObject;

import org.detaysoft.atomic.util.Base64;
import org.detaysoft.atomic.util.Compress;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.ProviderManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class MyActivity extends Activity implements Observer{

    Button connectServerButton;
    ConnectionConfiguration connectionConfiguration;
    XMPPConnection xmppConnection;
    WebView myWebView;
    WebAppInterface webAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        myWebView = (WebView)findViewById(R.id.myWebView);
        webAppInterface = new WebAppInterface(this);
        myWebView.addJavascriptInterface(webAppInterface, "MyHandler");
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.loadUrl("file:///android_asset/index.html");

        ProviderManager.getInstance().addIQProvider(
                Provider.element_name, "detayopenfireplugin:iq:customiq", Provider.getInstance());

    }

    public void connectServer(View v){
        if(v.getId()==R.id.connectServer){
            connectionConfiguration = new ConnectionConfiguration("your ip",5222);
            xmppConnection = new XMPPConnection(connectionConfiguration);
            new ConnectLoginServerAsyncTask().execute(xmppConnection);
            webAppInterface.setConnection(xmppConnection);
        }
    }

    @Override
    protected void onResume() {
        Provider.getInstance().addObserver(this);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Provider.getInstance().deleteObserver(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(xmppConnection.isConnected()){
            xmppConnection.disconnect();
        }
    }

    public void sendJsonString(final String jsonString){
        runOnUiThread(new Runnable(){

            @Override
            public void run() {
                myWebView.loadUrl("javascript:changeText('"+jsonString+"')");
            }
        });
    }

    public String mDeCompressedData(String data) {
        byte[] decompressedData = new Compress()
                .deCompress(Base64.decode(data));
        String result = new String(decompressedData, 0,
                decompressedData.length, Charset.forName("ISO-8859-9"));
        return result;
    }

    @Override
    public void update(Observable observable, Object o) {
       if(o instanceof CustomIQ){
           String deCompressedData = mDeCompressedData(((CustomIQ) o).getSpvCommand());
           sendJsonString(deCompressedData);
           Log.d("MyActivity",deCompressedData);
       }
    }
}
class ConnectLoginServerAsyncTask extends AsyncTask<XMPPConnection,Void,Void> {

    @Override
    protected Void doInBackground(XMPPConnection... params) {
        try {
            params[0].connect();
            params[0].login("your jid", "your pass", "your resource");
            Log.d("MainActivity", "Başarıyla Connect & Login Olundu");
        } catch (XMPPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void sendMainUsersInfoPacket(XMPPConnection connection) {
        IQPacket mainUsersInfoIQPacket = new IQPacket("MainScreenController",
                "getMainUsersInfo");
        HashMap<String, String> mainUsersInfoJsonParams = new HashMap<String, String>();
        mainUsersInfoJsonParams
                .put("cname",
                        "org.detaysoft.atomic.detayopenfireplugin.processor.externalprocessor.subprocessor.Business");
        mainUsersInfoJsonParams.put("mname", "getMainUsersInfoForWeb");
        mainUsersInfoJsonParams.put("clid", "100");
        mainUsersInfoJsonParams.put("usid","P0798");
        JsonObject mainUsersInfojSonObject = mainUsersInfoIQPacket
                .getJsonObject(mainUsersInfoJsonParams);
        IQ mainUsersInfoIQ = mainUsersInfoIQPacket
                .getIQPacket(mainUsersInfojSonObject);
        new SendCustomIQPacketAsyncTask(connection)
                .execute(mainUsersInfoIQ);
    }



}