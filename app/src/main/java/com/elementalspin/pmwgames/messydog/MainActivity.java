package com.elementalspin.pmwgames.messydog;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elementalspin.pmwgames.messydog.AndGraph.AGActivityGame;

public class MainActivity extends AGActivityGame {

    private CenaMenu cena_menu = null;
    private CenaGame cena_game = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.init(this, false);

        criaCenas();

        adicionaCenas();

    }


    private void criaCenas() {
        //método responsável por instanciar as cenas
        this.cena_menu = new CenaMenu(this.getGameManager());
        this.cena_game = new CenaGame(this.getGameManager());

    }

    private void adicionaCenas() {
        //método responsável por adicionar as cenas na lista de cenas
        this.getGameManager().addScene(this.cena_menu);
        this.getGameManager().addScene(this.cena_game);

    }
}
