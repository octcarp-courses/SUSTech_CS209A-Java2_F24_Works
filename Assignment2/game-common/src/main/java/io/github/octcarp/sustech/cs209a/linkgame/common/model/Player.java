package io.github.octcarp.sustech.cs209a.linkgame.common.model;

import java.io.Serializable;

public record Player(String id, String password) implements Serializable {
    public Player {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password cannot be null or empty");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Player player = (Player) obj;
        return id.equals(player.id) && password.equals(player.password);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
