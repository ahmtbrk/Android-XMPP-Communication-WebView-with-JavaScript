package com.detaysoft.xmppclient;

/**
 * Created by AhmetBurak on 07.06.2014.
 */
public class CustomIQ {
    private String sendClassName;
    private String sendMethodName;
    private String spvCommand;
    public String getSendClassName() {
        return sendClassName;
    }
    public void setSendClassName(String sendClassName) {
        this.sendClassName = sendClassName;
    }
    public String getSendMethodName() {
        return sendMethodName;
    }
    public void setSendMethodName(String sendMethodName) {
        this.sendMethodName = sendMethodName;
    }
    public String getSpvCommand() {
        return spvCommand;
    }
    public void setSpvCommand(String spvCommand) {
        this.spvCommand = spvCommand;
    }

    @Override
    public String toString(){
        return sendClassName+"-"+sendMethodName+"-"+spvCommand;
    }
}
