package client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.application.Platform;

import shared.dto.ObjectWrapper;
import client.controller.ClientCtr;
import static javafx.application.Application.launch;
import javafx.scene.control.Label;
import shared.model.Player;

import java.io.File;

public class RegisterFrm {

    private ClientCtr mySocket = ClientCtr.getInstance();
    private Stage stage = mySocket.getStage();
    MediaPlayer backgroundMusicPlayer;

    public RegisterFrm() {
    }

    public void openScene() {
        Platform.runLater(() -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Client/Register.fxml"));
            Scene scene = new Scene(loader.load());
            mySocket.setRegisterScene(scene);
            
            stage.setScene(scene);
            stage.setTitle("Register");
            stage.show();

            // JavaFX UI Controls
            TextField usernameTxt = (TextField) scene.lookup("#username");
            PasswordField passwordTxt = (PasswordField) scene.lookup("#password");
            PasswordField cfpasswordTxt = (PasswordField) scene.lookup("#cfpassword");
            Button registerBtn = (Button) scene.lookup("#register");
            Button LoginBtn = (Button) scene.lookup("#loginBtn");

            Label msg = (Label) scene.lookup("#msg");
            ImageView imgErr = (ImageView) scene.lookup("#iconErr");

            msg.setVisible(false);
            imgErr.setVisible(false);

            // Set up click event handlers
            registerBtn.setOnAction(event -> {
                audioClickButton();
                // Handle login button click
                String username = usernameTxt.getText();
                String password = passwordTxt.getText();
                String cfpassword = cfpasswordTxt.getText();

                if(username.isEmpty()){
                    msg.setVisible(true);
                    imgErr.setVisible(true);
                    msg.setText("Error: Username is not empty!");
                    return;
                }
                if(password.isEmpty()){
                    msg.setVisible(true);
                    imgErr.setVisible(true);
                    msg.setText("Error: Password is not empty!");
                    return;
                }
                if(password.compareTo(cfpassword) != 0){
                    msg.setVisible(true);
                    msg.setWrapText(true);
                    imgErr.setVisible(true);
                    msg.setText("Error: Password and confirm password do not match.");
                    return;
                }
                // Process login logic
                Player player = new Player();
                player.setUsername(username);
                player.setPassword(password);
                backgroundMusicPlayer.stop();
                mySocket.sendData(new ObjectWrapper(ObjectWrapper.REGISTER_USER, player));
            });

            LoginBtn.setOnAction(event -> {
                audioClickButton();
                if (mySocket.getLoginFrm()== null) {
                    LoginFrm loginFrm = new LoginFrm();
                    mySocket.setLoginFrm(loginFrm);
                }
                backgroundMusicPlayer.stop();
                mySocket.getLoginFrm().openScene();
            });
            initializeBackgroundMusic();

        } catch (Exception e) {
            e.printStackTrace();
        }
    });
 }


    public void receivedDataProcessing(ObjectWrapper data) {
            TextField usernameTxt = (TextField) mySocket.getRegisterScene().lookup("#username");
            String result = (String) data.getData();
            if (result.equals("false")) {
                Label msg = (Label) mySocket.getRegisterScene().lookup("#msg");
                msg.setText("Error: Account already exists.");
            } else {
                mySocket.setUsername(usernameTxt.getText());
                mySocket.sendData(new ObjectWrapper(ObjectWrapper.LOGIN_SUCCESSFUL, mySocket.getUsername()));
                if (mySocket.getMainFrm() == null) {
                    MainFrm mainFrm = new MainFrm();
                    mySocket.setMainFrm(mainFrm);
                }
                mySocket.getMainFrm().openScene();
            }

    }

    public void initializeBackgroundMusic(){
        // tao am thanh nen
        String backgroundMusicFile = new File("src/main/resources/Sounds/login.mp3").toURI().toString();
        Media backgroundMusic = new Media(backgroundMusicFile);
        backgroundMusicPlayer = new MediaPlayer(backgroundMusic);

        backgroundMusicPlayer.setVolume(0.5);
        backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        // Thêm error handler
        backgroundMusicPlayer.setOnError(() -> {
            System.out.println("Media error occurred: " + backgroundMusicPlayer.getError());
        });

        // Thêm status listener để debug
        backgroundMusicPlayer.statusProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Status changed from " + oldValue + " to " + newValue);
        });

        backgroundMusicPlayer.play();
    }

    public void audioClickButton(){
        String clickButtonFile = new File("src/main/resources/Sounds/click-233950.mp3").toURI().toString();
        Media clickButton = new Media(clickButtonFile);
        MediaPlayer clickButtonPlayer = new MediaPlayer(clickButton);
        clickButtonPlayer.setVolume(0.8);
        clickButtonPlayer.play();
    }
}
