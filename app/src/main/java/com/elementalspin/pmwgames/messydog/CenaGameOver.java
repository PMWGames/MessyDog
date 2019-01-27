package com.elementalspin.pmwgames.messydog;

import com.elementalspin.pmwgames.messydog.AndGraph.AGGameManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGInputManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScene;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScreenManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSoundManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSprite;
import com.elementalspin.pmwgames.messydog.AndGraph.AGText;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CenaGameOver extends AGScene {

    private AGSprite background = null;
    private AGSprite button = null;
    private AGText txt_points = null;

    public CenaGameOver(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        AGSoundManager.vrMusic.loadMusic("game_over.mp3", false);
        AGSoundManager.vrMusic.play();

        background = createSprite(R.drawable.gameover, 1,1 );
        background.setScreenPercent(100, 100);
        background.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);

        button = createSprite(R.drawable.restart_button, 1, 1);
        button.setScreenPercent( 40, 10);
        button.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/8.5f);

        String pontos = "0";
        try {
            FileInputStream fis = vrGameManager.vrActivity.openFileInput("points_file");
            int c;
            String temp = "";
            while( (c = fis.read()) != -1 ){
                temp += Character.toString((char) c);
            }
            pontos = temp;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        txt_points = new AGText(this, R.drawable.pixel);
        txt_points.setSize(6);
        txt_points.setTextPosXY(20, AGScreenManager.iScreenHeight/1.1f);
        txt_points.setTextColor(0,0,0,1);
        txt_points.setText("Score: " + pontos);
    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {

        if(AGInputManager.vrTouchEvents.screenClicked()){
            if(button.collide(AGInputManager.vrTouchEvents.getLastPosition())){
                vrGameManager.setCurrentScene(Cenas.CENA_GAME);
            }
        }

    }
}
