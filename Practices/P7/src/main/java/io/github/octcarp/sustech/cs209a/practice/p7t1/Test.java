package io.github.octcarp.sustech.cs209a.practice.p7t1;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    @State(Scope.Benchmark)
    public static class MyState {
        Account synchronizedAccount;
        Account lockAccount;

        @Setup(Level.Iteration)
        public void setUp() {
            synchronizedAccount = new SyncAccount();
            lockAccount = new LockAccount();
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static void testSynchronizedAccount(Test.MyState state) {
        state.synchronizedAccount.setBalance(0);

        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(state.synchronizedAccount, 10));
        }

        service.shutdown();
        while (!service.isTerminated()) {
        }

//        assert state.synchronizedAccount.getBalance() == 1000;
        if (state.synchronizedAccount.getBalance() != 1000) {
            System.err.println("SyncAccount balance is not 1000");
        }
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public static void testLockAccount(Test.MyState state) {
        state.lockAccount.setBalance(0);

        ExecutorService service = Executors.newFixedThreadPool(100);

        for (int i = 1; i <= 100; i++) {
            service.execute(new DepositThread(state.lockAccount, 10));
        }

        service.shutdown();
        while (!service.isTerminated()) {
        }

//        assert state.lockAccount.getBalance() == 1000;
        if (state.lockAccount.getBalance() != 1000) {
            System.err.println("LockAccount balance is not 1000");
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .measurementIterations(3)
                .warmupIterations(1)
                .mode(Mode.AverageTime)
                .forks(1)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}