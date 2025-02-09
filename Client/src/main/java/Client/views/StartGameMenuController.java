package Client.views;

import Client.application.App;
import Client.controllers.ClientSocketController;
import Client.controllers.HexController;
import Client.enums.QueryRequests;
import Client.models.User;
import Client.models.network.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StartGameMenuController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Spinner<Integer> numberOfPlayersSpinner;
    @FXML
    private Spinner<Integer> mapHeightSpinner;
    @FXML
    private Spinner<Integer> mapWidthSpinner;
    @FXML
    private VBox invitationsVBox;
    @FXML
    private MenuButton usernamesMenuButton;
    @FXML
    private TextArea cheatCodeArea;
    @FXML
    private Text cheatCodeText;
    @FXML
    private Button continueButton;
    @FXML
    private Button saveGameButton;
    @FXML
    private TextField saveNameTextField;
    @FXML
    private VBox lobbyPeopleVBox;
    public static boolean updateInvites = false;
    public Timeline timeline;

    public void initialize() {
        initPanes();
        initInvitations();
        if (!App.isMute() && App.getMediaPlayer().isMute()) {
            App.playNext();
        }
        timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> {
            if (updateInvites) {
                updateInvites = false;
                initInvitations();
                updateLobbyPeopleVBox();
            }
        }));
        timeline.setCycleCount(-1);
//        continueButton.setVisible(WorldController.getWorld() != null);
//        saveGameButton.setVisible(WorldController.getWorld() != null);
//        saveNameTextField.setVisible(WorldController.getWorld() != null);
        continueButton.setVisible(false);
        saveGameButton.setVisible(false);
        saveNameTextField.setVisible(false);
        cheatCodeText.setVisible(false);
        cheatCodeArea.setVisible(false);
        anchorPane.setOnKeyReleased(keyEvent -> {
            if (keyEvent.isControlDown() && keyEvent.isShiftDown() && keyEvent.getCode().getName().equals("C")) {
                cheatCodeArea.setVisible(!cheatCodeArea.isVisible());
                cheatCodeText.setVisible(!cheatCodeText.isVisible());
            }
        });
        updateLobbyPeopleVBox();
        timeline.play();
    }

    public void updateLobbyPeopleVBox() {
        lobbyPeopleVBox.getChildren().clear();
        for (String s : MainMenuController.loggedInUser.getPeopleInLobby()) {
            lobbyPeopleVBox.getChildren().add(new Text(s));
        }
    }

    public void initPanes() {
        ArrayList<User> users = new Gson().fromJson(Objects.requireNonNull(ClientSocketController.sendRequestAndGetResponse(QueryRequests.GET_USERS, new HashMap<>())).getParams().get("users"),
                new TypeToken<List<User>>() {
                }.getType());
        for (User user : users) {
            if (!user.getUsername().equals(MainMenuController.loggedInUser.getUsername()))
                usernamesMenuButton.getItems().add(new CheckMenuItem(user.getUsername()));
        }
        numberOfPlayersSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 10, 1));
        mapHeightSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 10, 1));
        mapWidthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 10, 1));
    }

    public void initInvitations() {
        invitationsVBox.getChildren().clear();
        for (String invitation : MainMenuController.loggedInUser.getInvitations()) {
            invitationsVBox.getChildren().add(makeInvitationBox(invitation));
        }
    }

    public HBox makeInvitationBox(String invitation) {
        Button accept = new Button("✔");
        Button decline = new Button("❌");
        accept.setOnMouseClicked(mouseEvent -> {
            Response response = ClientSocketController.sendRequestAndGetResponse(QueryRequests.ACCEPT_INVITATION, new HashMap<>() {{
                put("loggedInUser", MainMenuController.loggedInUser.getUsername());
                put("host", invitation.substring(16));
                put("invitation", invitation);
            }});
            assert response != null;
            MainMenuController.loggedInUser = new Gson().fromJson(response.getParams().get("user"), User.class);
            initInvitations();
        });
        decline.setOnMouseClicked(mouseEvent -> {
            Response response = ClientSocketController.sendRequestAndGetResponse(QueryRequests.DECLINE_INVITATION, new HashMap<>() {{
                put("loggedInUser", MainMenuController.loggedInUser.getUsername());
                put("invitation", invitation);
            }});
            assert response != null;
            MainMenuController.loggedInUser = new Gson().fromJson(response.getParams().get("user"), User.class);
            initInvitations();
        });
        accept.setStyle("-fx-pref-width: 50");
        decline.setStyle("-fx-pref-width: 50");
        Text text = new Text(invitation);
        text.setFill(Color.ORANGE);
        HBox hBox = new HBox(text, accept, decline);
        hBox.setSpacing(5);
        return hBox;
    }

    public void cheatCodeAreaTyped(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Enter")) {
            String command = cheatCodeArea.getText().substring(cheatCodeArea.getText().substring(0, cheatCodeArea.getText().length() - 1)
                    .lastIndexOf("\n") + 1, cheatCodeArea.getText().length() - 1);
            if (command.equals("clear")) {
                cheatCodeArea.clear();
            } else {
                ClientSocketController.sendRequestAndGetResponse(QueryRequests.CHEAT_COMMAND, new HashMap<>() {{
                    put("command", command);
                }});
            }
        }
    }

    public void backButtonClicked(MouseEvent mouseEvent) {
        timeline.stop();
        ClientSocketController.sendRequestAndGetResponse(QueryRequests.LEAVE_LOBBY, new HashMap<>(){{
            put("username", MainMenuController.loggedInUser.getUsername());
        }});
        App.changeScene("mainMenuPage");
    }

    public void sendInvitations(MouseEvent mouseEvent) {
        ArrayList<String> receivers = new ArrayList<>();
        for (MenuItem item : usernamesMenuButton.getItems()) {
            if (((CheckMenuItem) item).isSelected())
                receivers.add(item.getText());
        }
        if (receivers.size() >= 1)
            ClientSocketController.sendRequestAndGetResponse(QueryRequests.SEND_INVITATION, new HashMap<>() {{
                put("sender", MainMenuController.loggedInUser.getUsername());
                put("receivers", new Gson().toJson(receivers));
            }});
    }

    public void startGameButtonClicked(MouseEvent mouseEvent) {
        if (MainMenuController.loggedInUser.getPeopleInLobby().size() < 2) {
            System.out.println("can't start");
            return;
        } else if (MainMenuController.loggedInUser.getPeopleInLobby().size() > numberOfPlayersSpinner.getValue()) {
            System.out.println("number of players is less the actual players");
            return;
        }
        System.out.println(MainMenuController.loggedInUser.getPeopleInLobby() + " * " + mapWidthSpinner.getValue() + " " + mapHeightSpinner.getValue());
        ClientSocketController.sendRequestAndGetResponse(QueryRequests.NEW_WORLD, new HashMap<>() {{
            put("people", new Gson().toJson(MainMenuController.loggedInUser.getPeopleInLobby()));
            put("width", String.valueOf(mapWidthSpinner.getValue()));
            put("height", String.valueOf(mapHeightSpinner.getValue()));
        }});
        timeline.stop();
    }

    public void continueButtonClicked(MouseEvent mouseEvent) {
        timeline.stop();
//        App.changeScene("gamePage");
    }

    public void saveGameButtonClicked(MouseEvent mouseEvent) throws IOException {
//        WorldController.saveGame(saveNameTextField.getText());
    }
}
