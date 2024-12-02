package io.github.octcarp.sustech.cs209a.linkgame.common.model;

import java.io.Serializable;

public record GridPos(int row, int col) implements Serializable {
    public GridPos {
        if (row < 0 || col < 0) {
            System.err.printf("Invalid grid position: (%d, %d)\n", row, col);
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
        GridPos pos = (GridPos) obj;
        return row == pos.row && col == pos.col;
    }
}
