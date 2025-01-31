package com.elementalspin.pmwgames.messydog;

import android.content.Context;
import android.util.Log;

import com.elementalspin.pmwgames.messydog.AndGraph.AGGameManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGInputManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGMusic;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScene;
import com.elementalspin.pmwgames.messydog.AndGraph.AGScreenManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSoundManager;
import com.elementalspin.pmwgames.messydog.AndGraph.AGSprite;
import com.elementalspin.pmwgames.messydog.AndGraph.AGText;
import com.elementalspin.pmwgames.messydog.AndGraph.AGVector2D;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class CenaGame extends AGScene {

    private static int SALA = 0, QUARTO = 1;
    private int local_cao = SALA;

    private static int PARADO = 0, ANDANDO = 1;
    private int estado_cao = PARADO;

    private static int TAPETE_SALA = 1, VASO_SALA = 2, VASO_QUARTO = 3, CAMA_QUARTO = 4;
    private int ref_obj_interacao = 0;
    private boolean planta_sala_quebrou = false, isPlanta_quarto_quebrou = false;
    private boolean tapete_sala_baguncou = false, cama_quarto_baguncou = false;

    private float destino_x, destino_y, distancia_x, distancia_y;;
    private boolean chegou_destino_x, chegou_destino_y;
    private float velocidade_cao_fixa = 7.5f, velocidade_cao_x, velocidade_cao_y;
    private boolean cao_is_safe = false;
    private boolean is_game_over = false;
    private int tam_x_dog, tam_y_dog;

    private int points;

    private AGVector2D origem = null, destino = null, hipo = null;

    private AGSprite cao = null;
    private AGSprite dono = null;

    private AGSprite sala = null;
    private AGSprite hitbox_sofa_p = null, hitbox_sofa_g = null, hitbox_mesa_sala = null, hitbox_tapete_sala = null,
            hitbox_sala_safe1 = null, hitbox_coluna_sala = null, planta_sala = null, planta_sala_quebrada = null,
            tapete_sala = null, tapete_sala_baguncado = null, hitbox_go_quarto = null;

    private AGSprite quarto = null;
    private AGSprite hitbox_guarda_r_g = null, hitbox_guarda_r_m = null, hitbox_guarda_r_p = null, cama = null, cama_banguncada = null,
            planta_quarto = null, planta_quarto_quebrada = null, hitbox_quarto_safe1 = null, hitbox_go_sala = null;

    private AGSprite botao_aceitar = null;
    private AGSprite botao_cancelar = null;

    private AGText txt_points = null;

    private int ronda = 0;
    private int observa = 0;
    private boolean is_game_over_now = false;
    private int wait = 0;
    private int game_over_time = 0;
    private int chance_dono_aparece_sala, chance_dono_aparece_quarto;
    private boolean planta_quarto_quebrou;
    private boolean restart = false;


    public CenaGame(AGGameManager pManager) {
        super(pManager);
    }

    @Override
    public void init() {

        chance_dono_aparece_sala = 15;
        chance_dono_aparece_quarto = 15;

        tam_x_dog = 5;
        tam_y_dog = 10;

        AGSoundManager.vrMusic.loadMusic("trilha.mp3", true);
        AGSoundManager.vrMusic.setVolume(80, 80);
        AGSoundManager.vrMusic.play();

        //gerencia os locais que o cao está e suas interações
        cria_locais(SALA);

        origem = new AGVector2D();
        destino = new AGVector2D();
        hipo = new AGVector2D();

        txt_points = new AGText(this, R.drawable.pixel);
        txt_points.setSize(2);
        txt_points.setTextPosXY(AGScreenManager.iScreenWidth/1.2f, AGScreenManager.iScreenHeight/1.05f);
        txt_points.setText("0");

    }

    private void cria_cao(float pos_x, float pos_y) {

        cao = createSprite(R.drawable.catio_sheet, 5, 1);
        cao.setScreenPercent( tam_x_dog, tam_y_dog);
        cao.vrPosition.setXY(pos_x, pos_y);
        cao.addAnimation(5, true, 0, 4);

    }

    @Override
    public void restart() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void loop() {

        if(is_game_over_now || (observa > 30 * 3)){
            game_over_call();
        }

        if(cao == null){
            cria_cao(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/4.2f);
        }



        ronda ++;
        if(ronda > 30 * 5){

            Random r = new Random();
            int rand = r.nextInt(100);

            if(local_cao == SALA) {

                if (rand < chance_dono_aparece_sala){

                    if(dono == null) {

                        dono = createSprite(R.drawable.dono, 1, 1);
                        dono.setScreenPercent(10, 10);
                        dono.vrPosition.setXY(AGScreenManager.iScreenWidth / 6.2f, AGScreenManager.iScreenHeight/1.25f);
                        dono.fAngle = 180;

                    }



                    if( (planta_sala_quebrou || tapete_sala_baguncou) && (!cao_is_safe)){

                        is_game_over_now = true;

                    }
                }
            }else if(local_cao == QUARTO){



                if (rand < chance_dono_aparece_quarto){
                    if(dono == null) {
                        dono = createSprite(R.drawable.dono, 1, 1);
                        dono.setScreenPercent(10, 10);
                        dono.vrPosition.setXY(AGScreenManager.iScreenWidth / 1.2f, AGScreenManager.iScreenHeight/1.35f);
                        dono.fAngle = 90;
                    }
                    if( (cama_quarto_baguncou || planta_quarto_quebrou) && (!cao_is_safe)){

                        is_game_over_now = true;

                    }

                }
            }

            observa++;

            if(observa > 30 * 3){
                ronda = 0;
                if(!is_game_over_now) {
                    observa = 0;
                }
                removeSprite(dono);
                dono = null;


            }

        }

        eventos_toque();

        movimenta_cao();

        //clique ok cancel
        verifica_botoes_interacao();

        update_points();

        if(AGInputManager.vrTouchEvents.bBackButtonClicked){
            vrGameManager.vrActivity.finish();
        }

    }

    private void game_over_call() {


            String FILENAME = "points_file";
            String string = txt_points.getText();

            try {
                FileOutputStream fos = vrGameManager.vrActivity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
                fos.write(string.getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            reseta();

            vrGameManager.setCurrentScene(Cenas.CENA_GAMEOVER);

    }

    private void update_points() {

        if( Integer.parseInt(txt_points.getText()) != points){
            txt_points.setText(String.valueOf(points));
        }

    }

    private void interage() {

        if(ref_obj_interacao != 0){

            if(ref_obj_interacao == TAPETE_SALA && !tapete_sala_baguncou){

                tapete_sala.setColor(1,1,1,0);
                tapete_sala_baguncado.setColor(1,1,1,1);
                tapete_sala_baguncou = true;
                ref_obj_interacao = 0;
                points += 30;
                chance_dono_aparece_sala += 5;


            }
            if(ref_obj_interacao == VASO_SALA && !planta_sala_quebrou){

                planta_sala.setColor(1,1,1,0);
                planta_sala_quebrada.setColor(1,1,1,1);
                planta_sala_quebrou = true;
                ref_obj_interacao = 0;
                points += 200;
                chance_dono_aparece_sala += 10;
                int vidro_quebra = AGSoundManager.vrSoundEffects.loadSoundEffect("vidro.mp3");
                AGSoundManager.vrSoundEffects.play(vidro_quebra);

            }
            if(ref_obj_interacao == VASO_QUARTO && !planta_quarto_quebrou){

                planta_quarto.setColor(1,1,1,0);
                planta_quarto_quebrada.setColor(1,1,1,1);
                planta_quarto_quebrou = true;
                ref_obj_interacao = 0;
                points += 200;
                chance_dono_aparece_quarto += 10;
                int vidro_quebra = AGSoundManager.vrSoundEffects.loadSoundEffect("vidro.mp3");
                AGSoundManager.vrSoundEffects.play(vidro_quebra);

            }
            if (ref_obj_interacao == CAMA_QUARTO && !cama_quarto_baguncou){
                cama.setColor(1,1,1,0);
                cama_banguncada.setColor(1,1,1,1);
                cama_quarto_baguncou = true;
                ref_obj_interacao = 0;
                points += 40;
                chance_dono_aparece_quarto += 10;
            }

        }

    }

    private void verifica_botoes_interacao() {

        if(botao_aceitar != null){
            if(botao_aceitar.collide(AGInputManager.vrTouchEvents.getLastPosition())){

                if(ref_obj_interacao == TAPETE_SALA){
                    interage();
                }

                if(ref_obj_interacao == VASO_SALA){
                    interage();
                }

                if(ref_obj_interacao == VASO_QUARTO)
                {
                    interage();
                }

                if(ref_obj_interacao == CAMA_QUARTO){
                    interage();
                }

                mostrar_botoes_interacao(0, false);
                estado_cao = PARADO;
                return;

            }

            if(botao_cancelar.collide(AGInputManager.vrTouchEvents.getLastPosition())){
                mostrar_botoes_interacao(0, false);
                estado_cao = PARADO;
                return;
            }
        }

    }

    private void eventos_toque() {


        if(cao != null) {
            //Se a tela for clicada!
            if (AGInputManager.vrTouchEvents.screenClicked()) {


                if (local_cao == SALA) {

                    if ((AGInputManager.vrTouchEvents.getLastPosition().fX > AGScreenManager.iScreenWidth / 7.5f) && (AGInputManager.vrTouchEvents.getLastPosition().fY < AGScreenManager.iScreenHeight / 1.2f) &&
                            (AGInputManager.vrTouchEvents.getLastPosition().fX < AGScreenManager.iScreenWidth / 1.2f) && (AGInputManager.vrTouchEvents.getLastPosition().fY > AGScreenManager.iScreenHeight / 5.7f)) {

                        destino_x = AGInputManager.vrTouchEvents.getLastPosition().fX;
                        destino_y = AGInputManager.vrTouchEvents.getLastPosition().fY;

                        origem.setXY(cao.vrPosition.getX(), cao.vrPosition.getY());
                        destino.setXY(AGInputManager.vrTouchEvents.getLastPosition().fX, AGInputManager.vrTouchEvents.getLastPosition().fY);

                        hipo.setXY(destino.getX() - origem.getX(), destino.getY() - origem.getY());
                        double an = Math.toDegrees(Math.atan2(hipo.getY(), hipo.getX()) - Math.PI / 2);

                        cao.fAngle = (float) an;

                        mostrar_botoes_interacao(0, false);

                    }

                } else if(local_cao == QUARTO) {
                    //if ((AGInputManager.vrTouchEvents.getLastPosition().fX > AGScreenManager.iScreenWidth / 7.5f) && (AGInputManager.vrTouchEvents.getLastPosition().fY < AGScreenManager.iScreenHeight / 1.2f) &&
                      //      (AGInputManager.vrTouchEvents.getLastPosition().fX < AGScreenManager.iScreenWidth / 1.2f) && (AGInputManager.vrTouchEvents.getLastPosition().fY > AGScreenManager.iScreenHeight / 5.7f)) {

                        destino_x = AGInputManager.vrTouchEvents.getLastPosition().fX;
                        destino_y = AGInputManager.vrTouchEvents.getLastPosition().fY;

                        origem.setXY(cao.vrPosition.getX(), cao.vrPosition.getY());
                        destino.setXY(AGInputManager.vrTouchEvents.getLastPosition().fX, AGInputManager.vrTouchEvents.getLastPosition().fY);

                        hipo.setXY(destino.getX() - origem.getX(), destino.getY() - origem.getY());
                        double an = Math.toDegrees(Math.atan2(hipo.getY(), hipo.getX()) - Math.PI / 2);

                        cao.fAngle = (float) an;

                        mostrar_botoes_interacao(0, false);

                    //}
                }

                estado_cao = ANDANDO;

                chegou_destino_x = false;
                chegou_destino_y = false;

                if (cao.vrPosition.getX() > destino_x) {
                    distancia_x = cao.vrPosition.getX() - destino_x;
                } else if (cao.vrPosition.getX() < destino_x) {
                    distancia_x = destino_x - cao.vrPosition.getX();
                } else {
                    distancia_x = 0;
                }

                if (cao.vrPosition.getY() > destino_y) {
                    distancia_y = cao.vrPosition.getY() - destino_y;
                } else if (cao.vrPosition.getY() < destino_y) {
                    distancia_y = destino_y - cao.vrPosition.getY();
                } else {
                    distancia_y = 0;
                }

                if (distancia_y == distancia_x) {
                    velocidade_cao_x = velocidade_cao_fixa;
                    velocidade_cao_y = velocidade_cao_fixa;
                } else if (distancia_x > distancia_y) {

                    float per = ((distancia_y * 100) / distancia_x);
                    per = (velocidade_cao_fixa * per) / 100;

                    velocidade_cao_y = per;
                    velocidade_cao_x = velocidade_cao_fixa;

                } else if (distancia_x < distancia_y) {

                    float per = ((distancia_x * 100) / distancia_y);
                    per = (velocidade_cao_fixa * per) / 100;

                    velocidade_cao_x = per;
                    velocidade_cao_y = velocidade_cao_fixa;
                }

            }
        }

    }

    private void movimenta_cao() {


        if(cao != null) {
            //verifica se o estado do cão é o de andando ou parado
            if (estado_cao == ANDANDO) {

                if (cao != null) {

                    checa_collide_hitbox(cao);

                }

                if (!chegou_destino_x) {
                    if (cao.vrPosition.getX() > destino_x) {

                        cao.vrPosition.setX(cao.vrPosition.getX() - velocidade_cao_x);

                        if (cao.vrPosition.getX() <= destino_x) {
                            chegou_destino_x = true;
                            if (chegou_destino_y) {
                                estado_cao = PARADO;
                            }
                        }

                    } else {

                        cao.vrPosition.setX(cao.vrPosition.getX() + velocidade_cao_x);

                        if (cao.vrPosition.getX() >= destino_x) {
                            chegou_destino_x = true;
                            if (chegou_destino_y) {
                                estado_cao = PARADO;
                            }
                        }

                    }
                }

                if (!chegou_destino_y) {
                    if (cao.vrPosition.getY() > destino_y) {

                        cao.vrPosition.setY(cao.vrPosition.getY() - velocidade_cao_y);

                        if (cao.vrPosition.getY() <= destino_y) {
                            chegou_destino_y = true;
                            if (chegou_destino_x) {
                                estado_cao = PARADO;
                            }
                        }

                    } else {

                        cao.vrPosition.setY(cao.vrPosition.getY() + velocidade_cao_y);

                        if (cao.vrPosition.getY() >= destino_y) {
                            chegou_destino_y = true;
                            if (chegou_destino_x) {
                                estado_cao = PARADO;
                            }
                        }

                    }
                }

            }
        }




    }

    private void checa_collide_hitbox(AGSprite sprite) {

        if(cao != null) {
            if (local_cao == SALA) {

                if (hitbox_go_quarto.collide(sprite)) {

                    local_cao = QUARTO;
                    cria_locais(local_cao);
                    removeSprite(cao);
                    cao = null;
                    ref_obj_interacao = 0;

                    if (dono != null) {
                        removeSprite(dono);
                        dono = null;
                    }

                    estado_cao = PARADO;

                    cria_locais(QUARTO);
                    cria_cao(AGScreenManager.iScreenWidth / 1.25f, AGScreenManager.iScreenHeight / 1.35f);
                    cao.fAngle = 90;

                    return;

                }

                if (hitbox_tapete_sala.collide(sprite) && !tapete_sala_baguncou) {

                    mostrar_botoes_interacao(TAPETE_SALA, true);

                }

                if (hitbox_mesa_sala.collide(sprite) && !planta_sala_quebrou) {

                    mostrar_botoes_interacao(VASO_SALA, true);

                }

                if (hitbox_sofa_p.collide(sprite) || hitbox_sofa_g.collide(sprite) || hitbox_mesa_sala.collide(sprite) || hitbox_coluna_sala.collide(sprite)) {

                    if (sprite.vrPosition.getX() > destino_x) {
                        sprite.vrPosition.setX(sprite.vrPosition.getX() + velocidade_cao_x * 2);
                    } else {
                        sprite.vrPosition.setX(sprite.vrPosition.getX() - velocidade_cao_x * 2);
                    }

                    if (sprite.vrPosition.getY() > destino_y) {
                        sprite.vrPosition.setY(sprite.vrPosition.getY() + velocidade_cao_y * 2);
                    } else {
                        sprite.vrPosition.setY(sprite.vrPosition.getY() - velocidade_cao_y * 2);
                    }

                    estado_cao = PARADO;
                }

                if (hitbox_sala_safe1.collide(sprite)) {
                    cao_is_safe = true;
                } else {
                    cao_is_safe = false;
                }

            } else if (local_cao == QUARTO) {

                if (hitbox_go_sala.collide(sprite)) {

                    local_cao = SALA;
                    cria_locais(local_cao);
                    removeSprite(cao);
                    cao = null;
                    ref_obj_interacao = 0;

                    if (dono != null) {
                        removeSprite(dono);
                        dono = null;
                    }

                    estado_cao = PARADO;

                    cria_locais(SALA);
                    cria_cao(AGScreenManager.iScreenWidth / 2.9f, AGScreenManager.iScreenHeight / 4.9f);
                    // cria_cao(AGScreenManager.iScreenWidth/1.25f, AGScreenManager.iScreenHeight/1.35f);
                    cao.fAngle = 270;

                    return;

                }

                if (planta_quarto.collide(sprite) && !planta_quarto_quebrou) {

                    mostrar_botoes_interacao(VASO_QUARTO, true);

                }

                if (cama.collide(sprite) && !cama_quarto_baguncou) {

                    mostrar_botoes_interacao(CAMA_QUARTO, true);

                }

                if (hitbox_guarda_r_p.collide(sprite) || hitbox_guarda_r_g.collide(sprite) || hitbox_guarda_r_m.collide(sprite) || cama.collide(sprite) || planta_quarto.collide(sprite)) {

                    if (sprite.vrPosition.getX() > destino_x) {
                        sprite.vrPosition.setX(sprite.vrPosition.getX() + velocidade_cao_x * 2);
                    } else {
                        sprite.vrPosition.setX(sprite.vrPosition.getX() - velocidade_cao_x * 2);
                    }

                    if (sprite.vrPosition.getY() > destino_y) {
                        sprite.vrPosition.setY(sprite.vrPosition.getY() + velocidade_cao_y * 2);
                    } else {
                        sprite.vrPosition.setY(sprite.vrPosition.getY() - velocidade_cao_y * 2);
                    }

                    estado_cao = PARADO;
                }

                if (hitbox_quarto_safe1.collide(sprite)) {
                    cao_is_safe = true;
                } else {
                    cao_is_safe = false;
                }

            }
        }

    }

    private void reseta() {

        removeSprite(cao);
        cao = null;

        cria_locais(local_cao);
        ref_obj_interacao = 0;

        if(dono != null) {
            removeSprite(dono);
            dono = null;
        }

        chance_dono_aparece_sala = 15;
        chance_dono_aparece_quarto = 15;
        is_game_over_now = false;
        observa = 0;
        restart = true;

        points = 0;

    }

    private void mostrar_botoes_interacao(int interacao, boolean mostrar){

        if(mostrar){
            if(botao_aceitar == null){

                botao_aceitar = createSprite(R.drawable.mess_button, 1, 1);
                botao_aceitar.vrPosition.setXY(AGScreenManager.iScreenWidth/1.5f, AGScreenManager.iScreenHeight/4);
                botao_aceitar.setScreenProportional(AGScreenManager.iScreenWidth/5);

                botao_cancelar = createSprite(R.drawable.go_away_button, 1, 1);
                botao_cancelar.vrPosition.setXY(AGScreenManager.iScreenWidth/3f, AGScreenManager.iScreenHeight/4);
                botao_cancelar.setScreenProportional(AGScreenManager.iScreenWidth/5);

                ref_obj_interacao = interacao;

            }
        } else {
            if(botao_aceitar != null){
                removeSprite(botao_aceitar);
                botao_aceitar = null;
                removeSprite(botao_cancelar);
                botao_cancelar = null;
                ref_obj_interacao = 0;
            }
        }

    }

    private void cria_locais(int local) {

        if (local == SALA){

            remove_todos_oslocais();

            sala = createSprite(R.drawable.sala, 1,1);
            sala.setScreenPercent(100, 100);
            sala.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);

            hitbox_sala_safe1 = createSprite(R.drawable.pixel, 1, 1);
            hitbox_sala_safe1.setScreenPercent(25,7);
            hitbox_sala_safe1.vrPosition.setXY(AGScreenManager.iScreenWidth/1.285f, AGScreenManager.iScreenHeight/1.22f);
            hitbox_sala_safe1.setColor(1,1,1,0);

            hitbox_sofa_p = createSprite(R.drawable.pixel, 1, 1);
            hitbox_sofa_p.setScreenPercent(26, 9);
            hitbox_sofa_p.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/1.24f);
            hitbox_sofa_p.setColor(1,1,1,0);

            hitbox_sofa_g = createSprite(R.drawable.pixel, 1, 1);
            hitbox_sofa_g.setScreenPercent(20, 31);
            hitbox_sofa_g.vrPosition.setXY(AGScreenManager.iScreenWidth/1.25f, AGScreenManager.iScreenHeight/2.08f);
            hitbox_sofa_g.setColor(1,1,1,0);

            hitbox_mesa_sala = createSprite(R.drawable.pixel, 1, 1);
            hitbox_mesa_sala.setScreenPercent(21, 16);
            hitbox_mesa_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/2.1f, AGScreenManager.iScreenHeight/2.08f);
            hitbox_mesa_sala.setColor(1,1,1,0);

            hitbox_tapete_sala = createSprite(R.drawable.pixel, 1, 1);
            hitbox_tapete_sala.setScreenPercent(21, 8);
            hitbox_tapete_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/4.45f, AGScreenManager.iScreenHeight/1.24f);
            hitbox_tapete_sala.setColor(1,1,1,0);

            hitbox_coluna_sala = createSprite(R.drawable.pixel, 1, 1);
            hitbox_coluna_sala.setScreenPercent(17, 16);
            hitbox_coluna_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/6.5f, AGScreenManager.iScreenHeight/5.1f);
            hitbox_coluna_sala.setColor(1,1,1,0);

            planta_sala = createSprite(R.drawable.vaso_planta, 1, 1);
            planta_sala.setScreenPercent(12, 7);
            planta_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/2.09f, AGScreenManager.iScreenHeight/2.09f);
            planta_sala.setColor(1,1,1,1);

            planta_sala_quebrada = createSprite(R.drawable.prantaquebrada,1 ,1);
            planta_sala_quebrada.setScreenPercent(15,10);
            planta_sala_quebrada.vrPosition.setXY(AGScreenManager.iScreenWidth/2.09f, AGScreenManager.iScreenHeight/2.09f);
            planta_sala_quebrada.setColor(1,1,1,0);

            tapete_sala = createSprite(R.drawable.tapete, 1, 1);
            tapete_sala.setScreenPercent(21, 8);
            tapete_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/4.45f, AGScreenManager.iScreenHeight/1.24f);
            tapete_sala.setColor(1,1,1,1);

            tapete_sala_baguncado = createSprite(R.drawable.tapete_baguncado, 1, 1);
            tapete_sala_baguncado.setScreenPercent(21, 8);
            tapete_sala_baguncado.vrPosition.setXY(AGScreenManager.iScreenWidth/4.45f, AGScreenManager.iScreenHeight/1.24f);
            tapete_sala_baguncado.setColor(1,1,1,0);

            hitbox_go_quarto = createSprite(R.drawable.pixel, 1, 1);
            hitbox_go_quarto.setScreenPercent(4, 15);
            hitbox_go_quarto.vrPosition.setXY(AGScreenManager.iScreenWidth/4.2f, AGScreenManager.iScreenHeight/5.1f);
            hitbox_go_quarto.setColor(0.5f, 1, 1, 1);

            planta_sala_quebrou = false;
            tapete_sala_baguncou = false;
            is_game_over = false;
            game_over_time = 0;


        } else if( local == QUARTO){

            remove_todos_oslocais();

            quarto = createSprite(R.drawable.quarto, 1, 1);
            quarto.setScreenPercent(100, 100);
            quarto.vrPosition.setXY(AGScreenManager.iScreenWidth/2, AGScreenManager.iScreenHeight/2);

            hitbox_guarda_r_g = createSprite(R.drawable.pixel, 1, 1);
            hitbox_guarda_r_g.vrPosition.setXY(AGScreenManager.iScreenWidth/3f, AGScreenManager.iScreenHeight/1.35f);
            hitbox_guarda_r_g.setScreenPercent(48, 8);
            hitbox_guarda_r_g.setColor(1,1,1,0);

            hitbox_guarda_r_m = createSprite(R.drawable.pixel, 1, 1);
            hitbox_guarda_r_m.vrPosition.setXY(AGScreenManager.iScreenWidth/1.19f, AGScreenManager.iScreenHeight/1.8f);
            hitbox_guarda_r_m.setScreenPercent(15, 21);
            hitbox_guarda_r_m.setColor(1,1,1,0);

            hitbox_guarda_r_p = createSprite(R.drawable.pixel, 1, 1);
            hitbox_guarda_r_p.vrPosition.setXY(AGScreenManager.iScreenWidth/1.19f, AGScreenManager.iScreenHeight/3.8f);
            hitbox_guarda_r_p.setScreenPercent(15, 10);
            hitbox_guarda_r_p.setColor(1,1,1,0);

            hitbox_quarto_safe1 = createSprite(R.drawable.pixel, 1,1 );
            hitbox_quarto_safe1.vrPosition.setXY(AGScreenManager.iScreenWidth/1.19f, AGScreenManager.iScreenHeight/2.7f);
            hitbox_quarto_safe1.setScreenPercent(15, 14);
            hitbox_quarto_safe1.setColor(1,1,1,0);

            cama = createSprite(R.drawable.cama, 1 ,1);
            cama.vrPosition.setXY(AGScreenManager.iScreenWidth/3.8f, AGScreenManager.iScreenWidth/1.85f);
            cama.setScreenPercent(42, 20);
            //cama.setColor(1,1,1,0);

            cama_banguncada = createSprite(R.drawable.cama_baguncada, 1, 1);
            cama_banguncada.vrPosition.setXY(AGScreenManager.iScreenWidth/3.8f, AGScreenManager.iScreenHeight/(3.25f));
            cama_banguncada.setScreenPercent(42, 20);
            cama_banguncada.setColor(1,1,1,0);

            planta_quarto = createSprite(R.drawable.vaso_planta, 1, 1);
            planta_quarto.vrPosition.setXY(AGScreenManager.iScreenWidth/6.5f, AGScreenManager.iScreenHeight/2);
            planta_quarto.setScreenPercent(11, 6);

            planta_quarto_quebrada = createSprite(R.drawable.prantaquebrada, 1, 1);
            planta_quarto_quebrada.vrPosition.setXY(AGScreenManager.iScreenWidth/6.5f, AGScreenManager.iScreenHeight/2);
            planta_quarto_quebrada.setScreenPercent(11, 6);
            planta_quarto_quebrada.setColor(1,1,1,0);

            hitbox_go_sala = createSprite(R.drawable.pixel, 1, 1);
            hitbox_go_sala.vrPosition.setXY(AGScreenManager.iScreenWidth/1.05f, AGScreenManager.iScreenHeight/1.35f);
            hitbox_go_sala.setScreenPercent(4, 11);
            hitbox_go_sala.setColor(0.5f, 1, 1, 1);

        }

        if(restart){
            restart = false;
            if(local == QUARTO) {
                cria_cao(AGScreenManager.iScreenWidth / 2.9f, AGScreenManager.iScreenHeight / 4.9f);
            } else if(local == SALA){
                cria_cao(AGScreenManager.iScreenWidth/1.25f, AGScreenManager.iScreenHeight/1.35f);
            }
        }

    }

    private void remove_todos_oslocais() {

        //remove sala se tiver instanciado
        if(sala != null) {

            removeSprite(sala);
            removeSprite(hitbox_sala_safe1);
            removeSprite(hitbox_sofa_p);
            removeSprite(hitbox_sofa_g);
            removeSprite(hitbox_mesa_sala);
            removeSprite(hitbox_tapete_sala);
            removeSprite(hitbox_coluna_sala);
            removeSprite(planta_sala);
            removeSprite(planta_sala_quebrada);
            removeSprite(tapete_sala);
            removeSprite(tapete_sala_baguncado);
            removeSprite(hitbox_go_quarto);

            sala = null;
            hitbox_sala_safe1 = null;
            hitbox_sofa_p = null;
            hitbox_sofa_g = null;
            hitbox_mesa_sala = null;
            hitbox_tapete_sala = null;
            hitbox_coluna_sala = null;
            planta_sala = null;
            planta_sala_quebrada = null;
            tapete_sala = null;
            tapete_sala_baguncado = null;
            hitbox_go_quarto = null;

        }

        if(quarto != null){

            removeSprite(quarto);
            removeSprite(hitbox_guarda_r_p);
            removeSprite(hitbox_guarda_r_g);
            removeSprite(hitbox_guarda_r_m);
            removeSprite(planta_quarto);
            removeSprite(planta_quarto_quebrada);
            removeSprite(cama);
            removeSprite(cama_banguncada);
            removeSprite(hitbox_quarto_safe1);
            removeSprite(hitbox_go_sala);

            quarto = null;
            hitbox_guarda_r_g = null;
            hitbox_guarda_r_m = null;
            hitbox_guarda_r_p = null;
            planta_quarto = null;
            planta_quarto_quebrada = null;
            cama = null;
            cama_banguncada = null;
            hitbox_quarto_safe1 = null;
            hitbox_go_sala = null;

        }

    }
}
