package com.example.lulang_leakha;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class HelloController implements Initializable {
    @FXML
    private ImageView imagplay;
    @FXML
    private Slider volumeslider;
    @FXML
    private Label videoduaration;
    @FXML
    private Label playedduration;
    @FXML
    private AnchorPane frmaeroot;
    @FXML
    private Label labelv;
    @FXML
    private ImageView btnclose;
    @FXML
    private Label volumelabel;
    @FXML
    private MediaView mediaview;
    Media media;
    MediaPlayer player;
    @FXML
    private ImageView imagestop;
    @FXML
    private ImageView imagepause;
    @FXML
    private ImageView imgminimise;
    @FXML
    private ProgressBar playslider;
    /************************creating variable to control time duaration for media player******************/
    private Timer timer;
    private TimerTask task;
    private boolean playingMode;
    double durating_time;
    double endTime;
    double played_Time;

    @FXML
    private Label labeltime;
    private  int myvaluse;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        /*******************getting video from the resources folder**************************/
        String scr= getClass().getResource("/mane.mp4").toExternalForm();
        media = new Media(scr);
        player= new MediaPlayer(media);
        mediaview.setMediaPlayer(player);
        /****************************calling function for progressbar******************************/
        myvaluse= (int)playslider.getProgress();
        labeltime.setText(Integer.toString(myvaluse));

       /************************calling function to control volume**************************/
        controlVolume();
        int myvolume = 30;
        volumelabel.setText(Integer.toString(myvolume));
        labeltime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(player.getCurrentTime())+"";

            }
        }));

    }

    /*******************************function for initialisation and control for volume using slider***************************/
    public void controlVolume(){
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                volumeslider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        if (volumeslider.isValueChanging()) {
                            player.setVolume(volumeslider.getValue()/100);
                        }
                        /********************creating and initialising label to display currrent volume***************************/
                        int myvolume;
                        myvolume = (int) volumeslider.getValue();
                        volumelabel.setText(Integer.toString(myvolume));
                    }
                });
            }
        });
    }
    @FXML
    void PlayMedia(MouseEvent event) {

        MediaPlayer.Status status= player.getStatus();
        /**********************checking whether video is still playing or ortherwise pause video*********************/

        if(status == MediaPlayer.Status.HALTED||status==MediaPlayer.Status.UNKNOWN){
        }
        if(status == MediaPlayer.Status.PAUSED||
                status == MediaPlayer.Status.READY||
                status == MediaPlayer.Status.STOPPED){
            player.play();
            
            StatTime();
        }
        else {
            player.pause();
            /*************************calling stoptime function as video is paused*********************/
            stoptime();
            labeltime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    /*****************************displaying playbacktime for video *****************************/
                    return getTime(player.getCurrentTime())+"";
                }
            }));
        }
    }

    @FXML
    void PauseMedia(MouseEvent event) {

        player.pause();
        /*****************************displaying playbacktime for video *****************************/
        stoptime();
        labeltime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(player.getCurrentTime())+"";
            }
        }));
    }
    @FXML
    void minimizebutton(MouseEvent event) {


    }
    @FXML
    void StopMedia(MouseEvent event) {
        player.stop();
        stoptime();
    }
    /***************************function to initialise and get current time for playing video**************************/
    public void StatTime(){

        timer= new Timer();
        task = new TimerTask() {
            @Override
            public void run() {

                playingMode= true;
                durating_time = player.getCurrentTime().toSeconds();
                endTime = media.getDuration().toSeconds();

                /*************************initialising progresbar level *******************************/
                playslider.setProgress(durating_time/endTime);
                played_Time=durating_time/endTime;

                if(played_Time==1){
                    stoptime();
                }
            }
        };
        timer.scheduleAtFixedRate(task,1000,1000);
    }
    public void stoptime(){
        playingMode= false;
        timer.cancel();
    }
    /*************************function for calculating and converting current duartion for video to seconds and minutes*****************/
    public   String getTime(Duration time){
        int played_hours = (int)time.toHours();
        int played_minutes =(int)time.toMinutes();
        int played_seconds = (int)time.toSeconds();

        if(played_seconds>59)played_seconds= played_seconds%60;
        if(played_minutes >59)played_minutes = played_minutes %60;
        if(played_hours>59)played_hours= played_hours%60;

        if (played_hours>0)return String.format("%d:%02d:%02d",
                played_hours,played_minutes ,played_seconds);
        else return String.format("%02d:%02d",played_minutes ,played_seconds);
    }
    @FXML
    void closeWindow(MouseEvent event) {
        Platform.exit();

    }

}