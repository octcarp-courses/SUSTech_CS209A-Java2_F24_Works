package io.github.octcarp.sustech.cs209a.linkgame.common.model;

import java.io.Serializable;

public record MatchRecord(String p1, String p2, String p1Score, String p2Score, String endTime,
                          String result) implements Serializable {
    public MatchRecord {
        if (p1 == null || p2 == null || p1Score == null || p2Score == null || endTime == null || result == null) {
            throw new IllegalArgumentException("MatchRecord fields cannot be null");
        }
    }
}
