package org.octcarp.p2t1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class P2T1 {
    @State(Scope.Thread)
    public static class MyState {
        int[] arrayImpl;
        List<Integer> arraylistImpl;
        List<Integer> linkedlistImpl;
        HashMap<Integer, Integer> intmapImpl;

        int LENGTH = 1000;
        int OFFSET = 12010001;

        int index;

        @Setup(Level.Iteration)
        public void setUp() {
            arrayImpl = new int[LENGTH];
            arraylistImpl = new ArrayList<>();
            linkedlistImpl = new LinkedList<>();
            intmapImpl = new HashMap<>();

            index = new Random().nextInt(LENGTH) + OFFSET;

            for (int i = OFFSET; i < OFFSET + LENGTH; i++) {
                int age = 18 + new Random().nextInt(22);
                intmapImpl.put(i, age);
                int index = i - OFFSET;
                arrayImpl[index] = age;
                arraylistImpl.add(index, age);
                linkedlistImpl.add(index, age);
            }

        }

    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testintmap(P2T1.MyState state) {
        return state.intmapImpl.get(state.index);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testarraylist(P2T1.MyState state) {
        return state.arraylistImpl.get(state.index - state.OFFSET);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testlinkedlist(P2T1.MyState state) {
        return state.linkedlistImpl.get(state.index - state.OFFSET);
    }


    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    public static int testarray(P2T1.MyState state) {
        return state.arrayImpl[state.index - state.OFFSET];
    }


    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(P2T1.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}