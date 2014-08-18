package com.detaysoft.xmppclient;

import android.util.Log;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import java.util.Observable;

/**
 * Created by Ahmet Burak on 2.7.2014.
 */
public class Provider extends Observable implements IQProvider {

    private static Provider instance = new Provider();

    private Provider() {

    }

    public static Provider getInstance(){
        return instance;
    }

    public static String element_name = "pvcommand";

    @Override
    public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {

        int eventType = xmlPullParser.getEventType();
        CustomIQ customIQ = null;
        String currentTag = null;
        do {
            if (eventType == XmlPullParser.START_TAG) {
                currentTag = xmlPullParser.getName();
                if (xmlPullParser.getName().equalsIgnoreCase("pvcommand")) {
                    customIQ = new CustomIQ();
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                currentTag = null;
                if (xmlPullParser.getName().equalsIgnoreCase("pvcommand")) {
                    // burda dispatcher.
                    loadData(customIQ);
                    xmlPullParser = null;
                    break;
                }
            } else if (eventType == XmlPullParser.TEXT) {

                if (currentTag != null) {
                    if (currentTag.equalsIgnoreCase("sendClassName")) {
                        customIQ.setSendClassName(xmlPullParser.getText());
                    } else if (currentTag.equalsIgnoreCase("sendMethodName")) {
                        customIQ.setSendMethodName(xmlPullParser.getText());
                    } else if (currentTag.equalsIgnoreCase("spvcommand")) {
                        customIQ.setSpvCommand(xmlPullParser.getText());
                    }
                }
            }
            eventType = xmlPullParser.next();
        } while (eventType != XmlPullParser.END_DOCUMENT);

        IQ testIQ = new IQ(){
            @Override
            public String getChildElementXML() {
                return "";
            }
        };
        return testIQ;
    }

    public void loadData(CustomIQ customIQ) {
        if(customIQ.getSendMethodName().equals("getMainUsersInfo")){
            notifyObservers(customIQ);
            setChanged();
            Log.d("Provider","notifyObServers");
        }
    }
}
