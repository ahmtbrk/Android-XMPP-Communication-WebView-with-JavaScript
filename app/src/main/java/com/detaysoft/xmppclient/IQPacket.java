package com.detaysoft.xmppclient;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;
import org.jivesoftware.smack.packet.IQ;

import com.google.gson.JsonObject;

public class IQPacket {

	public String sendClassName, sendMethodName,loginUserJID;

	public IQPacket(String sendClassName, String sendMethodName) {
		this.sendClassName = sendClassName;
		this.sendMethodName = sendMethodName;
	}

	public JsonObject getJsonObject(HashMap<String, String> jSonParams) {
		JsonObject jObject = new JsonObject();
		for (Entry<String, String> jSonItem : jSonParams.entrySet()) {
			jObject.addProperty(jSonItem.getKey(), jSonItem.getValue());
		}
		return jObject;
	}

	public IQ getIQPacket(JsonObject jSonObject) {
		byte[] encode = Base64.encodeBase64(jSonObject.toString().getBytes());
		final String jSonBase64 = new String(encode, Charset.forName("UTF-8"));
		IQ iq = new IQ() {

			@Override
			public String getChildElementXML() {
				return "<pvcommand xmlns='detayopenfireplugin:iq:customiq'>"
						+ "<sendClassName>" + sendClassName
						+ "</sendClassName>" + "<sendMethodName>"
						+ sendMethodName + "</sendMethodName>" + "<spvcommand>"
						+ jSonBase64 + "</spvcommand></pvcommand>";
			}

		};
		// iq.setFrom(loginUserJID);
		return iq;
	}

	public void clear() {
		sendClassName = "";
		sendMethodName = "";
	}

	public void setSendClassName(String sendClassName) {
		this.sendClassName = sendClassName;
	}

	public void setSendMethodName(String sendMethodName) {
		this.sendMethodName = sendMethodName;
	}

}
