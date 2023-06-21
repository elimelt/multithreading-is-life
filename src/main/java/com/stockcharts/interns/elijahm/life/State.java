package com.stockcharts.interns.elijahm.life;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

public class State {
    int[][] data;
    private static final Random rand = new Random();
    public static final int ALIVE = 1;
    public static final int DEAD = 0;
    private int numThreads = 8;
    
    public State(int height, int width, double percentage) {
        
        this.data = new int[height][width];
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                this.data[y][x] = rand.nextInt(2);
            }
        }
        
    }

    public State(int height, int width, int numThreads) {
        this.numThreads = 16;
        this.data = new int[height][width];
        for (int y = 0; y < data.length; y++) {
            for (int x = 0; x < data[0].length; x++) {
                this.data[y][x] = rand.nextInt(2);
            }
        }
        
    }

    public State(int[][] state) {
        this.data = state;
    }

    public void update() {
        this.data = nextState(data);
    }

    public void parallelUpdate() {
        this.data = parallelNextState(this.data, numThreads);
    }

    public int[][] getState() {
        return this.data;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] line : this.data)
            sb.append(Arrays.toString(line) + "\n");

        return sb.toString();
    }

    protected int[][] nextState(int[][] oldState) {
        int[][] newState = new int[oldState.length][oldState[0].length];
        for (int y = 0; y < oldState.length; y++) {
            for (int x = 0; x < oldState[0].length; x++) {
                newState[y][x] = checkCell(x, y, oldState) ? ALIVE : DEAD;
            }
        }

        return newState;
    }

    protected int[][] parallelNextState(int[][] oldState, int numThreads){
        int[][] newState = new int[oldState.length][oldState[0].length];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            int rowStart = i * newState.length/numThreads;
            int rowEnd = (i + 1) * newState.length/numThreads;
            Thread t = new Thread(() -> {
                for (int j = rowStart; j < rowEnd; j++) {
                    for (int k = 0; k < newState[0].length; k++) {
                        newState[j][k] = checkCell(j, k, oldState) ? ALIVE : DEAD;
                    }
                }
            });
            t.start();
            threads.add(t);
            
        }
        try {
            for (Thread t : threads)
            t.join();
        } catch (Exception e) {
            // TODO: handle exception
        }

        return newState;
    }


    private boolean checkCell(int x, int y, int[][] data) {
        int neighborCount = countCellNeighbors(x, y, data);
        boolean cellLivesOn = data[y][x] == 1 && (neighborCount == 2 || neighborCount == 3);
        boolean newCellIsBorn = data[y][x] == 0 && neighborCount == 3;
        return cellLivesOn || newCellIsBorn;
    }

    private int countCellNeighbors(int x, int y, int[][] data) {
        int neighborCount = 0;

        for (int row = y - 1; row < y + 2; row++) {
            if (row < 0 || row >= data.length) 
                continue;
            
            for (int col = x - 1; col < x + 2; col++) {
                if (col < 0 || col >= data[0].length) 
                    continue;
                if (col == x && row == y) 
                    continue;
                
                neighborCount += data[row][col];
            }
        }
        return neighborCount; 
    }
}
