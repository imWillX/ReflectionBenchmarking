package me.williamxu;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class MyBenchmark {

    static Class<?> staticClazz = Number.class;
    static Method staticMethod;

    static {
        try {
            staticMethod = staticClazz.getMethod("increment");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @State(Scope.Benchmark)
    public static class Number {
        public int i = 0;

        public void increment() {
            i++;
        }

        public Number() {}
    }


    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchDirect(Number number) {
        number.increment();
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchReflectionWithInit(Number number) {
        try {
            Class<?> clazz = Number.class;
            Method method = clazz.getMethod("increment");
            method.invoke(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchReflectionWithoutInit(Number number) {
        try {
            Method method = staticClazz.getMethod("increment");
            method.invoke(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchStaticReflection(Number number) {
        try {
            staticMethod.invoke(number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
