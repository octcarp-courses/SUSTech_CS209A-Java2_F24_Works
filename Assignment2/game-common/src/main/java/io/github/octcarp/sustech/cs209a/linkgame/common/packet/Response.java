package io.github.octcarp.sustech.cs209a.linkgame.common.packet;

import java.io.Serializable;

public class Response implements Serializable {
    private ResponseType type;
    private Object data;

    public Response(ResponseType type) {
        this.type = type;
        this.data = null;
    }

    public Response(ResponseType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public ResponseType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
