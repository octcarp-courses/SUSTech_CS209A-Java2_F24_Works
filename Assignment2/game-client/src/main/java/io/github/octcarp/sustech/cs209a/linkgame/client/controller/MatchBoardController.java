package io.github.octcarp.sustech.cs209a.linkgame.client.controller;

import io.github.octcarp.sustech.cs209a.linkgame.client.net.ClientService;
import io.github.octcarp.sustech.cs209a.linkgame.client.net.MatchData;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.ImageLoader;
import io.github.octcarp.sustech.cs209a.linkgame.client.utils.SceneSwitcher;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Game;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.GridPos;
import io.github.octcarp.sustech.cs209a.linkgame.common.model.Match;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MatchBoardController {
    // Status banner
    @FXML
    private Label lblYourName;
    @FXML
    private Label lblYourScore;
    @FXML
    private Label lblOppName;
    @FXML
    private Label lblOppScore;
    @FXML
    private Label lblCurPlayer;

    // Select board size
    @FXML
    private VBox vbSelectSize;
    @FXML
    private Label lblSelectBoardSize;
    @FXML
    private ChoiceBox<String> cbBoardSize;
    @FXML
    private Button btnConfirmSize;

    // Game board
    @FXML
    private HBox vbBoard;
    @FXML
    private Label lblSelectedPoints;
    @FXML
    private GridPane gpGameBoard;
    @FXML
    private Label lblJudgeResult;

    @FXML
    private Button btnShuffle;

    private Match match;

    private final int[] position = new int[3];

    private String myId;

    private boolean finished = false;

    @FXML
    private void initialize() {
        myId = ClientService.getInstance().getMyId();
        vbBoard.setVisible(false);
    }

    public void initMatch(Match match) {
        this.match = match;
        if (myId.equals(match.getP1())) {
            lblYourName.setText(match.getP1());
            lblOppName.setText(match.getP2());
        } else {
            lblYourName.setText(match.getP2());
            lblOppName.setText(match.getP1());
        }
        boolean select = match.getWhoChoseSize().equals(myId);
        if (select) {
            lblSelectBoardSize.setText("Please select the board size");
        } else {
            lblSelectBoardSize.setText("Wait For your opponent to select the board size");
            cbBoardSize.setVisible(false);
            btnConfirmSize.setVisible(false);
        }
    }

    @FXML
    private void handleSelectSize(ActionEvent actionEvent) {
        String size = cbBoardSize.getValue();
        if (size == null) {
            return;
        }
        int row = Integer.parseInt(size.split("×")[0]);
        int col = Integer.parseInt(size.split("×")[1]);

        MatchData.getInstance().initBoard(new GridPos(row, col));

        vbSelectSize.setVisible(false);
        vbSelectSize.setManaged(false);
    }

    private void paintGameBoard(boolean enableE) {
        boolean enable = enableE && match.getCurTurn().equals(myId);
        gpGameBoard.getChildren().clear();
        Game game = match.getGame();
        int[][] board = game.getBoard();
        for (int row = 0; row < game.getRow(); row++) {
            for (int col = 0; col < game.getCol(); col++) {
                ImageView imageView = ImageLoader.addContent(board[row][col]);
                if (board[row][col] == 0) {
                    imageView.setFitWidth(40);
                    imageView.setFitHeight(40);
                    imageView.setPreserveRatio(true);
                    gpGameBoard.add(imageView, col, row);
                    continue;
                }

                imageView.setFitWidth(30);
                imageView.setFitHeight(30);
                imageView.setPreserveRatio(true);
                Button button = new Button();
                button.setPrefSize(40, 40);
                button.setGraphic(imageView);
                button.setDisable(!enable);
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(_ -> handleButtonPress(finalRow, finalCol));
                gpGameBoard.add(button, col, row);
            }
        }
    }

    @FXML
    private void handleButtonPress(int row, int col) {
        if (position[0] == 0) {
            position[1] = row;
            position[2] = col;
            position[0] = 1;
            lblSelectedPoints.setText("(" + row + "," + col + ")");
        } else {
            position[0] = 0;
            String selectedPoints = lblSelectedPoints.getText();
            lblSelectedPoints.setText(selectedPoints + " -> (" + row + "," + col + ")");

            List<GridPos> move = new ArrayList<>();
            move.add(new GridPos(position[1], position[2]));
            move.add(new GridPos(row, col));

            MatchData.getInstance().sendMove(move);
        }
    }

    @FXML
    private void handleShuffle() {
        MatchData.getInstance().shuffleBoard();
    }

    @FXML
    private void handleExit(ActionEvent actionEvent) {
        if (!finished) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit Match");
            alert.setHeaderText("Are you sure you want to exit the match?");
            alert.setContentText("All progress will be lost.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SceneSwitcher.getInstance().switchScene("main-menu");
                }
            });
        } else {
            SceneSwitcher.getInstance().switchScene("main-menu");
        }
    }

    public void updateMatchByData(Match match) {
        this.match = match;
        Platform.runLater(() -> {
            vbSelectSize.setVisible(false);
            vbSelectSize.setManaged(false);
            switch (match.getStatus()) {
                case P1_DIS -> {
                    lblJudgeResult.setText(match.getP1() + " is disconnect");
                    vbBoard.setVisible(false);
                    btnShuffle.setDisable(true);
                    gpGameBoard.setVisible(false);
                }
                case P2_DIS -> {
                    lblJudgeResult.setText(match.getP2() + " is disconnect");
                    vbBoard.setVisible(false);
                    btnShuffle.setDisable(true);
                    gpGameBoard.setVisible(false);
                }
                case RUN -> {
                    vbBoard.setVisible(true);
                    gpGameBoard.setVisible(true);
                    updateMatch();
                }

            }
        });
    }

    public void updateBoard(Game game) {
        this.match.getGame().setBoard(game.getBoard());
        Platform.runLater(() -> paintGameBoard(true));
    }

    private void updateMatch() {
        Platform.runLater(
                () -> {
                    if (myId.equals(match.getP1())) {
                        lblYourName.setText(match.getP1());
                        lblOppName.setText(match.getP2());
                        lblYourScore.setText(String.valueOf(match.getP1Score()));
                        lblOppScore.setText(String.valueOf(match.getP2Score()));
                    } else {
                        lblYourName.setText(match.getP2());
                        lblOppName.setText(match.getP1());
                        lblYourScore.setText(String.valueOf(match.getP2Score()));
                        lblOppScore.setText(String.valueOf(match.getP1Score()));
                    }

                    boolean myTurn = match.getCurTurn().equals(myId);
                    paintGameBoard(true);
                    lblCurPlayer.setText(myTurn ? "Your Turn" : "Opponent's Turn");
                    List<GridPos> lastPath = match.getLastPath();
                    if (lastPath != null) {
                        if (lastPath.size() >= 2) {
                            paintPath(lastPath);
                            lblJudgeResult.setText(myTurn ? "Opponent's Right" : "Bingo!");
                        } else {
                            lblJudgeResult.setText(myTurn ? "Opponent's Wrong" : "No below 3 link found");
                        }
                    }
                }
        );
    }

    private void paintPath(List<GridPos> path) {
        Game game = match.getGame();
        GridPos start = path.getFirst();
        GridPos end = path.getLast();
        if (path.size() > 2) {
            for (int i = 1; i < path.size() - 1; i++) {
                GridPos current = path.get(i);
                Image dirImage = ImageLoader.getDirectImgByPos(path.get(i - 1), path.get(i), path.get(i + 1));
                ImageView imageView = new ImageView(dirImage);
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                gpGameBoard.add(imageView, current.col(), current.row());
            }
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
                game.clearGrids(start.row(), start.col(), end.row(), end.col());
                paintGameBoard(true);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            game.clearGrids(start.row(), start.col(), end.row(), end.col());
            paintGameBoard(true);
        }
    }

    public void matchFinished(Match match) {
        this.match = match;
        finished = true;
        Platform.runLater(() -> {
            paintGameBoard(false);
            btnShuffle.setDisable(true);

            int yourScore = Integer.parseInt(lblYourScore.getText());
            int oppScore = Integer.parseInt(lblOppScore.getText());
            if (yourScore > oppScore) {
                lblJudgeResult.setText("You Win");
            } else if (yourScore < oppScore) {
                lblJudgeResult.setText("You Lose");
            } else {
                lblJudgeResult.setText("Draw");
            }

            lblCurPlayer.setText("Game Finished");
        });
    }

}