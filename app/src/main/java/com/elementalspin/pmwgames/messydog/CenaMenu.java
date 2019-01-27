package com.elementalspin.pmwgames.messydog;

import com.elementalspin.pmwgames.messydog.AndGraph.AGGameManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGInputManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScene;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScreenManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSoundManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSprite;

public class CenaMenu extends AGScene {

    private AGSprite bg = null;
    private AGSprite btn_start = null;
    private AGSprite dog_animation = null;

    public CenaMenu(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        //AGSoundManager.vrMusic.loadMusic("play.mp3", true);
        //AGSoundManager.vrMusic.play();

        bg = createSprite(R.drawable.tela_inicial, 1, 1);
        bg.setScreenPercent(100, 100);
        bg.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);

        dog_animation = createSprite(R.drawable.dog_animation, 5, 1);
        dog_animation.setScreenPercent(70, 40);
        dog_animation.vrPosition.setXY(AGScreenManager.iScreenWidth/3, AGScreenManager.iScreenHeight/4.5f);
        dog_animation.addAnimation(4, true, 0, 4);

        btn_start = createSprite(R.drawable.plau_button, 1, 1);
        btn_start.setScreenPercent(30, 10);
        btn_start.vrPosition.setXY(AGScreenManager.iScreenWidth/1.2f, AGScreenManager.iScreenHeight/8.5f);

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
            if(btn_start.collide(AGInputManager.vrTouchEvents.getLastPosition())){
                vrGameManager.setCurrentScene(Cenas.CENA_GAME);
            }
        }


    }
}
