package com.detaysoft.xmppclient;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.google.gson.JsonObject;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;

import java.util.HashMap;

/**
 * Created by Ahmet Burak on 3.7.2014.
 */
public class WebAppInterface {
    Context mContext;
    XMPPConnection connection;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    public void setConnection(XMPPConnection connection){
        this.connection = connection;
    }

    @JavascriptInterface
    public void sendMainUsersInfoPacket() {
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