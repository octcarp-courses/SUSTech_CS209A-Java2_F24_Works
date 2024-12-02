package io.github.octcarp.sustech.cs209a.linkgame.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game implements Serializable {
    private static final int GRID_TYPE_NUM = 12;

    // row length
    private int row;

    // col length
    private int col;

    // board content
    private int[][] board;

    public Game() {
        board = new int[6][6];
    }

    public Game(int row, int col) {
        this.row = row;
        this.col = col;
        this.board = setupBoard(row, col);
    }

    public Game(int[][] board) {
        this.board = board;
        this.row = board.length;
        this.col = board[0].length;
    }

    public Game copy() {
        int[][] newBoard = new int[row][col];
        for (int i = 0; i < row; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, col);
        }
        return new Game(newBoard);
    }

    // randomly initialize the game board
    public static int[][] setupBoard(int row, int col) {
        final int gridNum = row * col;
        if (gridNum % 2 != 0) {
            throw new IllegalArgumentException("The number of grids must be even.");
        }

        List<Integer> pieces = new ArrayList<>();
        int numPairs = gridNum / 2;
        int piecesPerType = numPairs / GRID_TYPE_NUM;

        for (int i = 1; i <= GRID_TYPE_NUM; i++) {
            for (int j = 0; j < piecesPerType; j++) {
                pieces.add(i);
                pieces.add(i);
            }
        }

        while (pieces.size() < gridNum) {
            int extraPiece = (int) (Math.random() * GRID_TYPE_NUM) + 1;
            pieces.add(extraPiece);
            pieces.add(extraPiece);
        }

        Collections.shuffle(pieces);

        int[][] board = new int[row][col];

        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = pieces.get(index++);
            }
        }

        return board;
    }

    public void shuffleBoard() {
        ArrayList<Integer> pieces = new ArrayList<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                pieces.add(board[i][j]);
            }
        }
        Collections.shuffle(pieces);

        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                board[i][j] = pieces.get(index++);
            }
        }
    }

    public List<GridPos> judge(int row1, int col1, int row2, int col2) {
        if ((board[row1][col1] != board[row2][col2]) || (row1 == row2 && col1 == col2)) {
            return null;
        }

        // one line
        if (isDirectlyConnected(row1, col1, row2, col2, board)) {
            return getDirectPath(row1, col1, row2, col2, board);
        }

        // two lines
        if ((row1 != row2) && (col1 != col2)) {
            if (board[row1][col2] == 0 && isDirectlyConnected(row1, col1, row1, col2, board)
                    && isDirectlyConnected(row1, col2, row2, col2, board)) {
                List<GridPos> path1 = getDirectPath(row1, col1, row1, col2, board);
                List<GridPos> path2 = getDirectPath(row1, col2, row2, col2, board);
                if (path1 != null && path2 != null) {
                    path1.addLast(new GridPos(row1, col2));
                    path1.addAll(path2);
                    return path1;
                }
            }
            if (board[row2][col1] == 0 && isDirectlyConnected(row2, col2, row2, col1, board)
                    && isDirectlyConnected(row2, col1, row1, col1, board)) {
                List<GridPos> path1 = getDirectPath(row1, col1, row2, col1, board);
                List<GridPos> path2 = getDirectPath(row2, col1, row2, col2, board);
                if (path1 != null && path2 != null) {
                    path1.addLast(new GridPos(row2, col1));
                    path1.addAll(path2);
                    return path1;
                }
            }
        }

        // three lines
        if (row1 != row2)
            for (int i = 0; i < board[0].length; i++) {
                if (board[row1][i] == 0 && board[row2][i] == 0 &&
                        isDirectlyConnected(row1, col1, row1, i, board) && isDirectlyConnected(row1, i, row2, i, board)
                        && isDirectlyConnected(row2, col2, row2, i, board)) {
                    List<GridPos> path1 = getDirectPath(row1, col1, row1, i, board);
                    List<GridPos> path2 = getDirectPath(row1, i, row2, i, board);
                    List<GridPos> path3 = getDirectPath(row2, col2, row2, i, board);
                    if (path1 != null && path2 != null && path3 != null) {
                        path1.addLast(new GridPos(row1, i));
                        path1.addAll(path2);
                        path1.addLast(new GridPos(row2, i));
                        path1.addAll(path3);
                        return path1;
                    }
                }
            }
        if (col1 != col2)
            for (int j = 0; j < board.length; j++) {
                if (board[j][col1] == 0 && board[j][col2] == 0 &&
                        isDirectlyConnected(row1, col1, j, col1, board) && isDirectlyConnected(j, col1, j, col2, board)
                        && isDirectlyConnected(row2, col2, j, col2, board)) {
                    List<GridPos> path1 = getDirectPath(row1, col1, j, col1, board);
                    List<GridPos> path2 = getDirectPath(j, col1, j, col2, board);
                    List<GridPos> path3 = getDirectPath(row2, col2, j, col2, board);
                    if (path1 != null && path2 != null && path3 != null) {
                        path1.addLast(new GridPos(j, col1));
                        path1.addAll(path2);
                        path1.addLast(new GridPos(j, col2));
                        path1.addAll(path3);
                        return path1;
                    }

                }
            }

        return null;
    }

    // judge the validity of an operation
    private boolean isDirectlyConnected(int row1, int col1, int row2, int col2, int[][] board) {
        if (row1 == row2) {
            int minCol = Math.min(col1, col2);
            int maxCol = Math.max(col1, col2);
            for (int col = minCol + 1; col < maxCol; col++) {
                if (board[row1][col] != 0) {
                    return false;
                }
            }
            return true;
        } else if (col1 == col2) {
            int minRow = Math.min(row1, row2);
            int maxRow = Math.max(row1, row2);
            for (int row = minRow + 1; row < maxRow; row++) {
                if (board[row][col1] != 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // get path of a direct connection
    private List<GridPos> getDirectPath(int row1, int col1, int row2, int col2, int[][] board) {
        List<GridPos> path = new ArrayList<>();

        if (row1 == row2 || col1 == col2) {
            int rowStep = Integer.compare(row2, row1);
            int colStep = Integer.compare(col2, col1);

            int currentRow = row1 + rowStep;
            int currentCol = col1 + colStep;

            while (currentRow != row2 || currentCol != col2) {
                if (board[currentRow][currentCol] != 0) {
                    return null;
                }
                path.add(new GridPos(currentRow, currentCol));
                currentRow += rowStep;
                currentCol += colStep;
            }
            return path;
        }
        return null;
    }

    public void clearGrids(int row1, int col1, int row2, int col2) {
        if (board[row1][col1] > 0) {
            board[row1][col1] = 0;
            board[row2][col2] = 0;
        }
    }

    public boolean gameFinished() {
        for (int[] row : board) {
            for (int grid : row) {
                if (grid != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.row = board.length;
        this.col = board[0].length;
        this.board = board;
    }
}
