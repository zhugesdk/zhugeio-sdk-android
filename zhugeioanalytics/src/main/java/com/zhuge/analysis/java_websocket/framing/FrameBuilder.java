package com.zhuge.analysis.java_websocket.framing;

import com.zhuge.analysis.java_websocket.exceptions.InvalidDataException;

import java.nio.ByteBuffer;

public interface FrameBuilder extends Framedata {

    public abstract void setFin(boolean fin);

    public abstract void setOptcode(Opcode optcode);

    public abstract void setPayload(ByteBuffer payload) throws InvalidDataException;

    public abstract void setTransferemasked(boolean transferemasked);

}