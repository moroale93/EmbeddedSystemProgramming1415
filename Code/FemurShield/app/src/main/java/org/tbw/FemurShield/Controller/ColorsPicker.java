package org.tbw.FemurShield.Controller;


import java.util.Random;

/**
 * Questa classe carica i colori dal file values/arrays.xml e li mette a disposizione tramite metodi statici.
 *
 * @author Marco Biasin
 */
public class ColorsPicker {
    private static ColorsPicker instance = null;
    private static String[][] palettes;

    private ColorsPicker(String[][] palettes) {
        this.palettes = palettes;

    }

    public static ColorsPicker getInstance(String[][] palettes) {
        if (instance == null) {
            instance = new ColorsPicker(palettes);
        }
        return instance;
    }

    /**
     * Ritorna 3 colori da una paletta in ordine casuale
     *
     * @return i tre colori scelti
     */
    public static String[] pickRandomColors() {
        if (palettes != null) {
            int[] index = new int[3];
            Random random = new Random();
            index[0] = random.nextInt(3);
            int j = random.nextInt(2);
            switch (index[0]) {
                case 0:
                    if (j == 0) {
                        index[1] = 1;
                        index[2] = 2;
                    } else {
                        index[1] = 2;
                        index[2] = 1;
                    }
                    break;
                case 1:
                    if (j == 0) {
                        index[1] = 0;
                        index[2] = 2;
                    } else {
                        index[1] = 2;
                        index[2] = 0;
                    }
                    break;
                case 2:
                    if (j == 0) {
                        index[1] = 0;
                        index[2] = 1;
                    } else {
                        index[1] = 1;
                        index[2] = 0;
                    }
                    break;

            }
            int i = random.nextInt(7);
            return new String[]{palettes[i][index[0]], palettes[i][index[1]], palettes[i][index[2]]};
        }
        return new String[]{"#F77925", "#64B897", "#ffff3831"};
    }
}
