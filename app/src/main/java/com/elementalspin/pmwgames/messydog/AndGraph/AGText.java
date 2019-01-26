package com.elementalspin.pmwgames.messydog.AndGraph;

import android.util.Log;

import java.util.ArrayList;

public class AGText {

    private AGScene scene = null;
    private int cod_imagem = 0;
    private String text = null;
    private ArrayList<AGLetter> letters = null;
    private float r, g, b, a;
    private float pos_x, pos_y;
    private float size;

    public AGText(AGScene scene, int cod_imagem) {
        this.scene = scene;
        this.cod_imagem = cod_imagem;
        this.r = 1f;
        this.g = 1f;
        this.b = 1f;
        this.a = 1f;
        this.pos_y = 0;
        this.pos_x = 0;
        this.setSize(1);
        this.letters = new ArrayList<>();
    }

    public void setText(String text){
        this.text = text;
        this.write();
    }

    public String getText(){
        return this.text;
    }

    public void setTextColor(float r, float g, float b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public void setTextPosXY(float x, float y){
        this.pos_x = x;
        this.pos_y = y;
    }

    public float getTextPosX(){
        return this.pos_x;
    }

    public float getTextPosY(){
        return this.pos_y;
    }

    private void write(){


            for (int i = 0; i < this.letters.size(); i++) {
                this.letters.get(i).clearLetter();

            }

            this.letters.clear();


        AGLetter last_letter = null;
        int space = 0;
        for(int i = 0; i < this.text.length(); i++){


            if(last_letter != null) {
                space += last_letter.pixel_size;
            }

            AGLetter letter = new AGLetter(this.scene, this.cod_imagem);
            letter.setSize(this.size);
            letter.setText(this.text.toLowerCase().charAt(i)+"");
            letter.setTextColor(this.r, this.g, this.b, this.a);
            letter.setPosXY((int) this.pos_x + space, (int) this.pos_y);
            letter.write();
            this.letters.add(letter);

            last_letter = letter;

        }

    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    private class AGLetter {

        private String text = null;
        private float r, g, b, a;
        private AGSprite pixel = null;
        private ArrayList<AGSprite> lst_pixels = null;
        private AGScene scene = null;
        private int cod_image;
        private int pos_x;
        private int pos_y;
        private float size;
        public int pixel_size = 0;

        public AGLetter(AGScene scene, int cod_imagem){

            this.scene = scene;
            this.setCodImage(cod_imagem);
            this.lst_pixels = new ArrayList<>();
            this.r = 0;
            this.g = 0;
            this.b = 0;
            this.a = 0;
            this.setSize(0);
            this.setPosX(0);
            this.setPosY(0);
            this.setText("");

        }

        public void clearLetter(){
            if(!this.lst_pixels.isEmpty()){
                for(int i = 0 ; i < this.lst_pixels.size(); i++){

                    AGSprite spt = this.lst_pixels.get(i);
                    this.scene.removeSprite(spt);

                }
                this.lst_pixels.clear();
            }
        }

        public void write(){

            if(this.lst_pixels.size() > 0){
                for(int i = 0 ; i < this.lst_pixels.size(); i++){

                    AGSprite spt = this.lst_pixels.get(i);
                    this.scene.removeSprite(spt);

                }
                this.lst_pixels.clear();
            }

            int space = 0;
            //percorre letra por letra
            for(int i = 0; i < this.text.length(); i++){

                //pega o array da letra e guarda em letter
                int[][] letter = getArrayLetter(this.text.toLowerCase().charAt(i));

                //pega o maximo de linhas e colunas
                int max_x = letter[0].length;
                int max_y = letter.length;

                int sum_pixel = 0;
                this.pixel_size = (int) (this.size * max_x);
                /*
                if( ((5*max_x) % 2 == 0) && (max_x >= 1) ){
                    this.pixel_size = (int) (this.size * max_x);
                }else {
                    this.pixel_size = (int) ((this.size * max_x) - 1);
                }
                */
                //percorre as linhas e colunas do array da letra
                for(int j = max_y - 1; j >= 0; j--){

                    for(int k = max_x - 1; k >= 0; k--){

                        //verifica se o pixel é true e sendo assim desenha
                        if(letter[j][k] == 1){

                            this.lst_pixels.add(this.pixel);
                            AGSprite spt;
                            spt = this.scene.createSprite(this.cod_image, 1, 1);
                            spt.setPixelScaleSize(this.size);
                            spt.vrPosition.setX(this.pos_x + (k * this.size * spt.getSpriteWidth())/ 2);
                            spt.vrPosition.setY(this.pos_y - (j * this.size * spt.getSpriteHeight())/ 2);
                            if(this.text.toLowerCase().charAt(i) == ' '){
                                spt.setColor(this.r, this.g, this.b, 0f);
                            } else {
                                spt.setColor(this.r, this.g, this.b, this.a);
                            }
                            this.lst_pixels.add(spt);
                            sum_pixel = (int) spt.getSpriteWidth();

                        }

                    }

                }
                this.pixel_size*= (sum_pixel/2) + 0.5f;

            }



        }

        public void setTextColor(float r, float g, float b, float a){

            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;

        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getCodImage() {
            return cod_image;
        }

        public void setCodImage(int cod_image) {
            this.cod_image = cod_image;
        }

        public void setPosXY(int x, int y){
            this.pos_x = x;
            this.pos_y = y;
        }

        public int getPosX() {
            return pos_x;
        }

        public void setPosX(int pos_x) {
            this.pos_x = pos_x;
        }

        public int getPosY() {
            return pos_y;
        }

        public void setPosY(int pos_y) {
            this.pos_y = pos_y;
        }

        public float getSize() {
            return size;
        }

        public void setSize(float size) {
            this.size = size;
        }



        private int[][] getArrayLetter(char letter){

            int[][] arr_letter = null;

            switch (letter){

                case 'a':

                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1},
                            {0, 1, 1, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case 'b':
                    arr_letter = new int[][]{
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {1, 0, 1, 1, 0},
                            {1, 1, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 1, 0, 0, 1},
                            {1, 0, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case 'c':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case 'd':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {0, 1, 1, 0, 1},
                            {1, 0, 0, 1, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1},
                            {0, 1, 1, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case 'e':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 1, 1, 1, 1},
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case 'f':
                    arr_letter = new int[][]{
                            {0, 0, 1},
                            {0, 1, 0},
                            {1, 1, 1},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                    };
                    break;

                case 'g':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 0, 1},
                            {1, 0, 0, 1, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1},
                            {0, 1, 1, 0, 1},
                            {0, 0, 0, 0, 1},
                            {1, 1, 1, 1, 0},
                    };
                    break;
                case 'h':
                    arr_letter = new int[][]{
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {1, 0, 1, 1, 0},
                            {1, 1, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'i':
                    arr_letter = new int[][]{
                            {1},
                            {0},
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {0},
                            {0},
                    };
                    break;
                case 'j':
                    arr_letter = new int[][]{
                            {0, 1},
                            {0, 0},
                            {0, 1},
                            {0, 1},
                            {0, 1},
                            {0, 1},
                            {0, 1},
                            {0, 1},
                            {0, 1},
                            {1, 0},
                    };
                    break;
                case 'k':
                    arr_letter = new int[][]{
                            {1, 0, 0, 0},
                            {1, 0, 0, 0},
                            {1, 0, 0, 1},
                            {1, 0, 1, 0},
                            {1, 1, 0, 0},
                            {1, 0, 1, 0},
                            {1, 0, 1, 0},
                            {1, 0, 0, 1},
                            {0, 0, 0, 0},
                            {0, 0, 0, 0},
                    };
                    break;
                case 'l':
                    arr_letter = new int[][]{
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {1},
                            {0},
                            {0},
                    };
                    break;
                case 'm':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0},
                            {1, 0, 1, 1, 1, 1, 0},
                            {1, 1, 0, 1, 0, 0, 1},
                            {1, 0, 0, 1, 0, 0, 1},
                            {1, 0, 0, 1, 0, 0, 1},
                            {1, 0, 0, 1, 0, 0, 1},
                            {1, 0, 0, 1, 0, 0, 1},
                            {0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0 ,0},
                    };
                    break;
                case 'n':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'o':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'p':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 0, 1, 1, 0},
                            {1, 1, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 1, 0, 0, 1},
                            {1, 0, 1, 1, 0},
                            {1, 0, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                    };
                    break;
                case 'q':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 0, 1},
                            {1, 0, 0, 1, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1},
                            {0, 1, 1, 0, 1},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                    };
                    break;
                case 'r':
                    arr_letter = new int[][]{
                            {0, 0, 0},
                            {0, 0, 0},
                            {1, 0, 1},
                            {1, 1, 0},
                            {1, 0, 0},
                            {1, 0, 0},
                            {1, 0, 0},
                            {1, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                    };
                    break;
                case 's':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 0, 0},
                            {0, 0, 0, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 't':
                    arr_letter = new int[][]{
                            {0, 1, 0},
                            {0, 1, 0},
                            {1, 1, 1},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 0},
                            {0, 1, 1},
                            {0, 0, 0},
                            {0, 0, 0},
                    };
                    break;
                case 'u':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 1},
                            {0, 1, 1, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'v':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 0, 1, 0},
                            {0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'w':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1, 0, 0, 0, 1},
                            {1, 0, 0, 1, 0, 1, 0, 0, 1},
                            {0, 1, 0, 1, 0, 1, 0, 1, 0},
                            {0, 1, 0, 1, 0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0, 0, 1, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0, 0, 0, 0, 0},
                    };
                    break;
                case 'x':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;
                case 'y':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 0, 1, 0},
                            {0, 1, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 0, 0},
                    };
                    break;
                case 'z':
                    arr_letter = new int[][]{
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {1, 1, 1, 1, 1},
                            {0, 0, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 0, 0},
                            {1, 1, 1, 1, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '0':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '1':
                    arr_letter = new int[][]{
                            {0, 0, 1},
                            {0, 1, 1},
                            {1, 0, 1},
                            {0, 0, 1},
                            {0, 0, 1},
                            {0, 0, 1},
                            {0, 0, 1},
                            {0, 0, 1},
                            {0, 0, 0},
                            {0, 0, 0},
                    };
                    break;

                case '2':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 0, 0},
                            {1, 1, 1, 1, 1},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '3':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '4':
                    arr_letter = new int[][]{
                            {0, 0, 0, 1, 0},
                            {0, 0, 1, 1, 0},
                            {0, 1, 0, 1, 0},
                            {0, 1, 0, 1, 0},
                            {1, 0, 0, 1, 0},
                            {1, 1, 1, 1, 1},
                            {0, 0, 0, 1, 0},
                            {0, 0, 0, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '5':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 1},
                            {0, 1, 0, 0, 0},
                            {1, 0, 0, 0, 0},
                            {1, 1, 1, 1, 0},
                            {0, 0, 0, 0, 1},
                            {0, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '6':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 0},
                            {1, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '7':
                    arr_letter = new int[][]{
                            {1, 1, 1, 1, 1},
                            {0, 0, 0, 1, 0},
                            {0, 0, 0, 1, 0},
                            {0, 0, 1, 0, 0},
                            {0, 0, 1, 0, 0},
                            {0, 1, 0, 0, 0},
                            {0, 1, 0, 0, 0},
                            {0, 1, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '8':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case '9':
                    arr_letter = new int[][]{
                            {0, 1, 1, 1, 0},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 1},
                            {0, 0, 0, 0, 1},
                            {1, 0, 0, 0, 1},
                            {0, 1, 1, 1, 0},
                            {0, 0, 0, 0, 0},
                            {0, 0, 0, 0, 0},
                    };
                    break;

                case ':':
                    arr_letter = new int[][]{
                            {0},
                            {0},
                            {1},
                            {0},
                            {0},
                            {0},
                            {0},
                            {1},
                            {0},
                            {0},
                    };
                    break;

                case '.':
                    arr_letter = new int[][]{
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {1},
                            {0},
                            {0},
                    };
                    break;

                case ' ':
                    arr_letter = new int[][]{
                            {0, 0, 1},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                            {0, 0, 0},
                    };

                    break;
/*
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
            case '.':
                arr_letter = new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                };
                break;
*/





                default:
                    arr_letter = new int[][]{
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                            {0},
                    };
                    break;


            }

            return arr_letter;

        }

    }



}
