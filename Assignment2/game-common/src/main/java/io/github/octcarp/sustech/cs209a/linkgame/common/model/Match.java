package io.github.octcarp.sustech.cs209a.linkgame.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Match implements Serializable {
    private String p1;
    private String p2;

    private int p1Score;
    private int p2Score;

    private String whoChoseSize;

    private String curTurn;

    private List<GridPos> lastPath;

    private Game game;


    public enum MatchStatus {
        INIT,
        RUN,
        P1_DIS,
        P2_DIS,
        FINISHED
    }

    MatchStatus status;

    //    private MatchStatus status;
    public Match() {

    }

    public Match(String p1, String p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.p1Score = 0;
        this.p2Score = 0;
        int choose = new Random().nextInt(2) + 1;
        this.whoChoseSize = choose == 1 ? p1 : p2;
        this.curTurn = whoChoseSize;
        this.lastPath = new ArrayList<>();
        this.game = new Game();
//        this.status = MatchStatus.INIT;
    }

    public Match(String p1, String p2, GridPos boardSize) {
        this.p1 = p1;
        this.p2 = p2;
        this.p1Score = 0;
        this.p2Score = 0;
        int choose = new Random().nextInt(2) + 1;
        this.whoChoseSize = choose == 1 ? p1 : p2;
        this.curTurn = whoChoseSize;
        this.lastPath = new ArrayList<>();
        this.game = new Game(boardSize.col(), boardSize.row());
    }

    public Match(String p1, String p2, int p1Score, int p2Score, String whoChoseSize, String curTurn, List<GridPos> lastPath, MatchStatus status, Game copy) {
        this.p1 = p1;
        this.p2 = p2;
        this.p1Score = p1Score;
        this.p2Score = p2Score;
        this.whoChoseSize = whoChoseSize;
        this.curTurn = curTurn;
        this.lastPath = lastPath;
        this.status = status;
        this.game = copy;
    }

    public Match copy() {
        return new Match(p1, p2, p1Score, p2Score, whoChoseSize, curTurn, lastPath, status, game.copy());
    }

    public void initGame(int row, int col) {
        this.game.setBoard(Game.setupBoard(row, col));
        status = MatchStatus.RUN;
    }

    public String getP1() {
        return p1;
    }

    public String getP2() {
        return p2;
    }

    public int getP1Score() {
        return p1Score;
    }

    public int getP2Score() {
        return p2Score;
    }

    public String getWhoChoseSize() {
        return whoChoseSize;
    }

    public String getCurTurn() {
        return curTurn;
    }

    public Game getGame() {
//        if (status == MatchStatus.PAUSE) {
//            System.out.println("Game is paused");
//            return null;
//        }
        return game;
    }

    public void incP1Score(int increment) {
        p1Score += increment;
    }

    public void incP2Score(int increment) {
        p2Score += increment;
    }

    public void switchTurn() {
        curTurn = curTurn.equals(p1) ? p2 : p1;
    }

    public void setLastPath(List<GridPos> lastPath) {
        this.lastPath = lastPath;
    }

    public List<GridPos> getLastPath() {
        return lastPath;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public MatchStatus getStatus() {
        return status;
    }

    public void setStatus(MatchStatus status) {
        this.status = status;
    }
}
