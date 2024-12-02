package io.github.octcarp.sustech.cs209a.linkgame.common.packet;

import java.io.Serializable;

public enum ResponseType implements Serializable {
    LOGIN_RESULT,
    REGISTER_RESULT,
    LOGOUT_RESULT,

    ALL_WAITING_PLAYERS,
    WAITING_OPPONENT,

    GET_MATCH_RECORD_RESULT,

    START_MATCH,
    SYNC_MATCH,
    SYNC_BOARD,
    MATCH_FINISHED,

    OPP_DISCONNECTED,
    OPP_RECONNECTED,

    RECONNECT_SUCCESS,
    NO_MATCH_TO_RECONNECT,

    ERROR_MESSAGE,
    ;
}
