package com.stockcharts.interns.elijahm.life;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class LifeAppTest {

    @Test
    public void test_state_transition() {
        System.out.println("test running");
        int[][] state = {
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,0,0,0,0},
            {0,0,0,0,1,0,0,0,0},
            {0,0,0,0,1,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
        };
        State s = new State(state);

        s.update();
        int[][] nextState = s.getState();
        int[][] expectedNextState = {
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,1,1,1,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
        };

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < nextState[0].length; j++) {
                assertTrue(nextState[i][j] == expectedNextState[i][j]);
            }
        }

        int[][] currState;
        for (int k = 0; k < 10; k++) {
            s.update();
            currState = s.getState();
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < nextState[0].length; j++) {
                    assertTrue(currState[i][j] == state[i][j]);
                }
            }

            s.update();
            currState = s.getState();
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < nextState[0].length; j++) {
                    assertTrue(currState[i][j] == nextState[i][j]);
                }
            }
            
        }
    }

    @Test
    public void test_state_transition_complicated() {
        System.out.println("test running");
        int[][] state = {
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,1,0,0,0},
            {0,0,0,0,1,1,1,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
        };

        int[][] expectedNextState = {
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,1,0,1,0,0},
            {0,0,0,0,1,0,1,0,0},
            {0,0,0,0,0,1,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0}
        };
        State s = new State(state);

        s.update();
        int[][] nextState = s.getState();
        

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < nextState[0].length; j++) {
                assertTrue(nextState[i][j] == expectedNextState[i][j]);
            }
        }
    }

    @Test
    public void benchmark_parallel_state_transition() {
        State s = new State(10000, 10000, .5);
        
        long timeInitialSeq = System.nanoTime();

        for (int i = 0; i < 10; i++) {
            s.update();
            System.out.println("update " + i + " finished sequentially");
        }

        long timeFinalSeq = System.nanoTime();
        long timeElapsedSequential = timeFinalSeq - timeInitialSeq;

        long timeInitialPara = System.nanoTime();


        s = new State(100, 1000000, .5);

        for (int i = 0; i < 10; i++) {
            s.parallelUpdate();
            System.out.println("update " + i + " finished in parallel");
        }

        long timeFinalPara = System.nanoTime();
        long timeElapsedParallel = timeFinalPara - timeInitialPara;
        System.out.println("time parallel       " + timeElapsedParallel);
        System.out.println("time seq            " + timeElapsedSequential);

        assertTrue(timeElapsedParallel < timeElapsedSequential);
    }

    // @Test
    public void find_optimal_num_threads() {
        List<long[]> data = new ArrayList<>();

        for (int numThreads = 1; numThreads < 32; numThreads++) {
            long iTime = System.currentTimeMillis();
            State s = new State(1000, 10000, numThreads);

            for (int i = 0; i < 30; i++) {
                s.parallelUpdate();
                // System.out.println("Thread " + i + " update " + i + " finished in parallel");
            }
            long fTime = System.currentTimeMillis();

            data.add(new long[]{(long) numThreads, fTime - iTime});
        }

        long[] best = data.get(0);

        for (long[] x : data) {
            if (best[1] > x[1]) {
                best = x;
            }
            System.out.println(Arrays.toString(x));
        }
        System.out.println("Optimal number of threads: " + best[0]);
    }

}
