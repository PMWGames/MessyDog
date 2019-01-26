package com.elementalspin.pmwgames.messydog;

import com.elementalspin.pmwgames.messydog.AndGraph.AGGameManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGInputManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScene;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScreenManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSprite;

public class CenaMenu extends AGScene {

    private AGSprite btn_start = null;

    public CenaMenu(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        btn_start = createSprite(R.drawable.moana2, 1, 1);
        btn_start.setScreenPercent(30, 10);
        btn_start.vrPosition.setXY(AGScreenManager.iScreenWidth /2, AGScreenManager.iScreenHeight/2);

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
