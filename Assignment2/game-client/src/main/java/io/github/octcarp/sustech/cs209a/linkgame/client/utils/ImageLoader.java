package io.github.octcarp.sustech.cs209a.linkgame.client.utils;

import io.github.octcarp.sustech.cs209a.linkgame.client.controller.MatchBoardController;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.Objects;

public class ImageLoader {
    public static ImageView addContent(int content) {
        return switch (content) {
            case 12 -> new ImageView(imageCarambola);
            case 1 -> new ImageView(imageApple);
            case 2 -> new ImageView(imageMango);
            case 3 -> new ImageView(imageBlueberry);
            case 4 -> new ImageView(imageCherry);
            case 5 -> new ImageView(imageGrape);
            case 6 -> new ImageView(imageKiwi);
            case 7 -> new ImageView(imageOrange);
            case 8 -> new ImageView(imagePeach);
            case 9 -> new ImageView(imagePear);
            case 10 -> new ImageView(imagePineapple);
            case 11 -> new ImageView(imageWatermelon);
            case 0 -> new ImageView(imageEmpty);

            default -> null;
        };
    }

    public static Image getDirectImgByPos(GridPos start, GridPos mid, GridPos end) {
        List<GridPos> posList = List.of(start, end);
        boolean left = false, right = false, up = false, down = false;
        for (GridPos pos : posList) {
            if (pos.row() == mid.row()) {
                if (pos.col() < mid.col()) {
                    left = true;
                } else {
                    right = true;
                }
            } else if (pos.col() == mid.col()) {
                if (pos.row() < mid.row()) {
                    up = true;
                } else {
                    down = true;
                }
            }
        }

        if (up) {
            if (left) {
                return lineUpLeft;
            } else if (right) {
                return lineUpRight;
            } else {
                return lineVertical;
            }
        } else if (down) {
            if (left) {
                return lineDownLeft;
            } else {
                return lineDownRight;
            }
        } else {
            return lineHorizontal;
        }
    }

    public static Image imageApple = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/apple.png")).toExternalForm());
    public static Image imageMango = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/mango.png")).toExternalForm());
    public static Image imageBlueberry = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/blueberry.png")).toExternalForm());
    public static Image imageCherry = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/cherry.png")).toExternalForm());
    public static Image imageGrape = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/grape.png")).toExternalForm());
    public static Image imageCarambola = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/carambola.png")).toExternalForm());
    public static Image imageKiwi = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/kiwi.png")).toExternalForm());
    public static Image imageOrange = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/orange.png")).toExternalForm());
    public static Image imagePeach = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/peach.png")).toExternalForm());
    public static Image imagePear = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/pear.png")).toExternalForm());
    public static Image imagePineapple = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/pineapple.png")).toExternalForm());
    public static Image imageWatermelon = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/watermelon.png")).toExternalForm());
    public static Image imageEmpty = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/fruits/empty.png")).toExternalForm());

    public static Image lineUpLeft = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/u-l.png")).toExternalForm());
    public static Image lineUpRight = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/u-r.png")).toExternalForm());
    public static Image lineDownLeft = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/d-l.png")).toExternalForm());
    public static Image lineDownRight = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/d-r.png")).toExternalForm());
    public static Image lineHorizontal = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/l-r.png")).toExternalForm());
    public static Image lineVertical = new Image(Objects.requireNonNull(MatchBoardController.class.getResource("/img/lines/u-d.png")).toExternalForm());
}