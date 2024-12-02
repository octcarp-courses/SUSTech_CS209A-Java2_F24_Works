package io.github.octcarp.sustech.cs209a.linkgame.common.packet;

import java.io.Serializable;

public enum RequestType implements Serializable {
    REGISTER,
    LOGIN,
    LOGOUT,
    DISCONNECT,

    ENTER_LOBBY,
    EXIT_LOBBY,
    START_WAITING,
    STOP_WAITING,

    GET_MATCH_RECORD,

    JOIN_PLAYER,
    RECONNECT_MATCH,

    SELECT_BOARD,
    EXIT_MATCH,
    SHUFFLE_BOARD,

    TURN_MOVE,

    SHUTDOWN,
    ;
}
