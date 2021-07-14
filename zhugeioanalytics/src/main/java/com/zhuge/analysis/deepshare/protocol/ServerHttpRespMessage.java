package com.zhuge.analysis.deepshare.protocol;

public abstract class ServerHttpRespMessage extends ServerMessage {

    protected ServerHttpSendMessage sent;

    private String err = null;

    private boolean isClientBadRequest = false;

    public ServerHttpRespMessage(ServerHttpSendMessage sent) {
        direction = ServerMessage.DIRECTION_IN;
        this.sent = sent;
    }

    public String getError() {
        return err;
    }

    public void setError(String err) {
        if (this.err == null) {
            this.err = err;
        }
    }

    public void setIsClientBadRequest(boolean isBad){
        isClientBadRequest = isBad;
    }

    public boolean isClientBadRequest(){
        return isClientBadRequest;
    }

    public boolean isOk() {
        return err == null;
    }

    public abstract void receive(byte[] bytes);

    public ServerHttpSendMessage getRequest() {
        return sent;
    }

    @Override
    public String toString() {
        return "ServerHttpRespMessage from " + sent.getClass().getSimpleName();
    }
}
