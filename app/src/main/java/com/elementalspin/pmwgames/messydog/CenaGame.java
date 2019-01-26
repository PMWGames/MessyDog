package com.elementalspin.pmwgames.messydog;

import android.util.Log;

import com.elementalspin.pmwgames.messydog.AndGraph.AGGameManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGInputManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScene;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScreenManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSprite;
import com.elementalspin.pmwgames.messydog.AndGraph.AGVector2D;
import com.elementalspin.pmwgames.messydog.java2d.Vector2D;

public class CenaGame extends AGScene {

    private static int PARADO = 0, ANDANDO = 1;
    private int estado_cao = PARADO;

    private float destino_x, destino_y, distancia_x, distancia_y;;
    private boolean chegou_destino_x, chegou_destino_y;
    private float velocidade_cao_fixa = 7.5f, velocidade_cao_x, velocidade_cao_y;
    private AGVector2D origem = null, destino = null, hipo = null;

    private AGSprite cao = null;

    public CenaGame(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        cao = createSprite(R.drawable.catio_sheet, 4, 1);
        cao.setScreenPercent(5, 10);
        cao.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);
        cao.addAnimation(4, true, 0, 3);

        origem = new AGVector2D();
        destino = new AGVector2D();
        hipo = new AGVector2D();

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {

        //Se a tela for clicada!
        if(AGInputManager.vrTouchEvents.screenClicked()){

            origem.setXY(cao.vrPosition.getX(), cao.vrPosition.getY());
            destino.setXY(AGInputManager.vrTouchEvents.getLastPosition().fX, AGInputManager.vrTouchEvents.getLastPosition().fY);

            hipo.setXY(destino.getX() - origem.getX(), destino.getY() - origem.getY());
            double an = Math.toDegrees(  Math.atan2(hipo.getY(), hipo.getX()) - Math.PI/2);

            cao.fAngle = (float) an;

            destino_x = AGInputManager.vrTouchEvents.getLastPosition().fX;
            destino_y = AGInputManager.vrTouchEvents.getLastPosition().fY;

            estado_cao = ANDANDO;

            chegou_destino_x = false;
            chegou_destino_y = false;

            if(cao.vrPosition.getX() > destino_x){
                distancia_x = cao.vrPosition.getX() - destino_x;
            } else if(cao.vrPosition.getX() < destino_x){
                distancia_x = destino_x - cao.vrPosition.getX();
            } else {
                distancia_x = 0;
            }

            if(cao.vrPosition.getY() > destino_y){
                distancia_y = cao.vrPosition.getY() - destino_y;
            } else if(cao.vrPosition.getY() < destino_y){
                distancia_y = destino_y - cao.vrPosition.getY();
            } else {
                distancia_y = 0;
            }

            if(distancia_y == distancia_x){
                velocidade_cao_x = velocidade_cao_fixa;
                velocidade_cao_y = velocidade_cao_fixa;
            } else if( distancia_x > distancia_y){

                float per = ((distancia_y * 100) / distancia_x);
                per = (velocidade_cao_fixa * per) / 100;

                velocidade_cao_y = per;
                velocidade_cao_x = velocidade_cao_fixa;

            } else if(distancia_x < distancia_y){

                float per = ((distancia_x * 100) / distancia_y);
                per = (velocidade_cao_fixa * per) / 100;

                velocidade_cao_x = per;
                velocidade_cao_y = velocidade_cao_fixa;
            }

        }

        //verifica se o estado do cão é o de andando ou parado
        if(estado_cao == ANDANDO){

            Log.i("ASDF", "velx: "+velocidade_cao_x + " |vely: "+velocidade_cao_y);

            if(!chegou_destino_x) {
                if (cao.vrPosition.getX() > destino_x) {

                    cao.vrPosition.setX(cao.vrPosition.getX() - velocidade_cao_x);

                    if (cao.vrPosition.getX() <= destino_x) {
                        chegou_destino_x = true;
                        if(chegou_destino_y){
                            estado_cao = PARADO;
                        }
                    }

                } else {

                    cao.vrPosition.setX(cao.vrPosition.getX() + velocidade_cao_x);

                    if (cao.vrPosition.getX() >= destino_x) {
                        chegou_destino_x = true;
                        if(chegou_destino_y){
                            estado_cao = PARADO;
                        }
                    }

                }
            }

            if(!chegou_destino_y) {
                if (cao.vrPosition.getY() > destino_y) {

                    cao.vrPosition.setY(cao.vrPosition.getY() - velocidade_cao_y);

                    if (cao.vrPosition.getY() <= destino_y) {
                        chegou_destino_y = true;
                        if(chegou_destino_x){
                            estado_cao = PARADO;
                        }
                    }

                } else {

                    cao.vrPosition.setY(cao.vrPosition.getY() + velocidade_cao_y);

                    if (cao.vrPosition.getY() >= destino_y) {
                        chegou_destino_y = true;
                        if(chegou_destino_x){
                            estado_cao = PARADO;
                        }
                    }

                }
            }

        }


    }
}
