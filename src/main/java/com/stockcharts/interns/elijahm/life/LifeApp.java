package com.stockcharts.interns.elijahm.life;

import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.introcs.*;

public class LifeApp {
    
    
    private static final int SCALING_FACTOR = 1; 
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    
    
    public static void main(String[] args) {
        // State state = new State(HEIGHT, WIDTH, .5);
        // State state = new State(generateSpecialState());
        State state = new State(generateFull());
        

        Picture screen = new Picture(WIDTH * SCALING_FACTOR, HEIGHT * SCALING_FACTOR);
        
        while (true) {
            
            int[][] stateData = state.getState();
            display(stateData, screen);
            // state.update();
            state.parallelUpdate();
            // try {
            //     Thread.sleep(100);
            // } catch (Exception e) {
            //     // TODO: handle exception
            // }
            
        }
    }


    private static void display(int[][] state, Picture screen) {
        for (int y = 0; y < state.length; y++) {
            for (int x = 0; x < state[0].length; x++) {
                if (state[y][x] == 1) //screen.set(x, y, Color.GREEN);
                    drawSquare(x, y, screen, Color.PINK);
                else 
                    drawSquare(x, y, screen, Color.BLUE);

            }
        }
        screen.show();
    }

    private static void drawSquare(int x, int y, Picture screen, Color c) {
        for (int i = x * SCALING_FACTOR; i < (x + 1) * SCALING_FACTOR; i++) {
            for (int j = y * SCALING_FACTOR; j < (y + 1) * SCALING_FACTOR; j++) {
                screen.set(i, j, c);
            }
        }
    }

    private static int[][] generateSpecialState() {
        int[][] specialState = new int[HEIGHT][WIDTH];

        int x = WIDTH/2;
        int y = HEIGHT/2;

        specialState[y][x] = 1;
        specialState[y - 1][x - 1] = 1;
        specialState[y - 1][x] = 1;
        specialState[y - 2][x - 2] = 1;
        specialState[y - 3][x - 1] = 1;
        specialState[y - 3][x] = 1;
        specialState[y - 2][x + 1] = 1;
        return specialState;
    }

    private static int[][] generateFull() {
        int[][] initialFull = new int[HEIGHT][WIDTH];

        for (int i = 1; i < initialFull.length - 1; i++)
            Arrays.fill(initialFull[i], 1);
        
        for (int j = 1; j < initialFull[0].length; j++) {
            initialFull[j][0] = 0;
            initialFull[j][initialFull[0].length - 1] = 0;
        }

        return initialFull;

    }

}
