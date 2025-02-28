package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PriorityQueueBenchmark {

    private final int insertions;
    private final int deletions;
    private final int runs;
    private final int n;

    public PriorityQueueBenchmark(int runs, int n, int insertions, int deletions) {
        this.runs = runs;
        this.n = n;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public static void main(String[] args) {
        new PriorityQueueBenchmark(10, 4095, 16000, 4000).runBenchmarks();
        new PriorityQueueBenchmark(8, 16000, 40000, 10000).runBenchmarks();
        new PriorityQueueBenchmark(5, 40000, 64000, 16000).runBenchmarks();
        new PriorityQueueBenchmark(3, 64000, 256000,22000).runBenchmarks();

        new PriorityQueueBenchmark(10, 4095, 16000, 4000).run4aryHeapBenchmarks();
        new PriorityQueueBenchmark(8, 16000, 40000, 10000).run4aryHeapBenchmarks();
        new PriorityQueueBenchmark(5, 40000, 64000, 16000).run4aryHeapBenchmarks();
        new PriorityQueueBenchmark(3, 64000, 256000,22000).run4aryHeapBenchmarks();

    }

    public void runBenchmarks() {
        System.out.println("\nBenchmarking PriorityQueue Implementations");
        System.out.println("Benchmarking for N = " + n);
        benchmark("Binary Heap", () -> createPriorityQueue(n,false));
        benchmark("Binary Heap with Floyd’s Trick", () -> createPriorityQueue(n,true));
    }

    public void run4aryHeapBenchmarks() {
        System.out.println("\nBenchmarking 4-ary Heap PriorityQueue Implementations");
        System.out.println("Benchmarking for N = " + n);
        benchmarkFourAryHeap("Four-Ary Heap", () -> createFourAryHeap(n, false));
        benchmarkFourAryHeap("Four-Ary Heap with Floyd’s Trick", () -> createFourAryHeap(n, true));
    }

    private void benchmark(String description, Supplier<PriorityQueue<Integer>> supplier) {
        Consumer<PriorityQueue<Integer>> pqOperation = pq -> insertAndDelete(pq);
        Benchmark_Timer<PriorityQueue<Integer>> timer = new Benchmark_Timer<>(description, pqOperation);
        double time = timer.runFromSupplier(supplier, runs);
        System.out.printf("%s: %.3f ms per run%n", description, time);
    }

    private void benchmarkFourAryHeap(String description, Supplier<FourAryHeap<Integer>> supplier) {
        Consumer<FourAryHeap<Integer>> pqOperation = fh -> insertAndDeleteFH(fh);
        Benchmark_Timer<FourAryHeap<Integer>> timer = new Benchmark_Timer<>(description, pqOperation);
        double time = timer.runFromSupplier(supplier, runs);
        System.out.printf("%s: %.3f ms per run%n", description, time);
    }

    private PriorityQueue<Integer> createPriorityQueue(int size, boolean floyd) {
        return new PriorityQueue<Integer>(size, true, Comparator.naturalOrder(), floyd);
    }

    private FourAryHeap<Integer> createFourAryHeap(int size, boolean floyd) {
        return new FourAryHeap<Integer>(size, true, Comparator.naturalOrder(), floyd);
    }

    private void insertAndDelete(PriorityQueue<Integer> pq) {
        Random random = new Random();
        Integer highestPrioritySpilled = null;

        // Insert elements
        for (int i = 0; i < insertions; i++) {
            int value = random.nextInt(100000);
            pq.give(value);
        }

        // Remove elements
        for (int i = 0; i < deletions; i++) {
            try {
                Integer removed = pq.take();
                if (highestPrioritySpilled == null || removed > highestPrioritySpilled) {
                    highestPrioritySpilled = removed;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Highest Priority Spilled: " + highestPrioritySpilled);
    }


    private void insertAndDeleteFH(FourAryHeap<Integer> fh) {
        Random random = new Random();
        Integer highestPrioritySpilled = null;

        // Insert elements
        for (int i = 0; i < insertions; i++) {
            int value = random.nextInt(100000);
            fh.give(value);
        }

        // Remove elements
        for (int i = 0; i < deletions; i++) {
            try {
                Integer removed = fh.take();
                if (highestPrioritySpilled == null || removed > highestPrioritySpilled) {
                    highestPrioritySpilled = removed;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Highest Priority Spilled: " + highestPrioritySpilled);
    }

}
