package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.Helper;
import com.phasmidsoftware.dsaipg.sort.HelperFactory;
import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.phasmidsoftware.dsaipg.util.Config_Benchmark.setupConfigFixes;

public class InsertionSortComparatorBenchmark {

    private final int runs;
    private final int n;
    private final Supplier<Integer[]> randomSupplier;
    private final Supplier<Integer[]> orderedSupplier;
    private final Supplier<Integer[]> partiallyOrderedSupplier;
    private final Supplier<Integer[]> reverseOrderedSupplier;

    public InsertionSortComparatorBenchmark(int runs, int n) {
        this.runs = runs;
        this.n = n;
        this.randomSupplier = () -> generateArray(n, Order.RANDOM);
        this.orderedSupplier = () -> generateArray(n, Order.ORDERED);
        this.partiallyOrderedSupplier = () -> generateArray(n, Order.PARTIALLY_ORDERED);
        this.reverseOrderedSupplier = () -> generateArray(n, Order.REVERSE_ORDERED);
    }

    public static void main(String[] args) {
        new InsertionSortComparatorBenchmark(100, 250).runBenchmarks();
        new InsertionSortComparatorBenchmark(50, 500).runBenchmarks();
        new InsertionSortComparatorBenchmark(20, 1000).runBenchmarks();
        new InsertionSortComparatorBenchmark(10, 2000).runBenchmarks();
        new InsertionSortComparatorBenchmark(5, 4000).runBenchmarks();
        new InsertionSortComparatorBenchmark(3, 8000).runBenchmarks();
        new InsertionSortComparatorBenchmark(2, 16000).runBenchmarks();
    }

    public void runBenchmarks() {
        System.out.println("Benchmarking Insertion Sort for N = " + n);
        benchmark("Random", randomSupplier);
        benchmark("Ordered", orderedSupplier);
        benchmark("Partially Ordered", partiallyOrderedSupplier);
        benchmark("Reverse Ordered", reverseOrderedSupplier);
    }

    private void benchmark(String description, Supplier<Integer[]> supplier) {
        Comparator<Integer> comparator = Integer::compareTo;
        Helper<Integer> helper = HelperFactory.createGeneric(
                "InsertionSort - " + description, comparator, supplier.get().length, runs, setupConfigFixes()
        );

        InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(helper);

        Consumer<Integer[]> sortingFunction = xs -> sorter.sort(xs);

        Benchmark_Timer<Integer[]> timer = new Benchmark_Timer<>("InsertionSort - " + description, sortingFunction);
        double time = timer.runFromSupplier(supplier, runs);
        System.out.printf("%s: %.3f ms per run%n", description, time);
    }

    private Integer[] generateArray(int size, Order order) {
        Integer[] array = new Integer[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        switch (order) {
            case ORDERED:
                Arrays.sort(array);
                break;
            case REVERSE_ORDERED:
                Arrays.sort(array, Comparator.reverseOrder());
                break;
            case PARTIALLY_ORDERED:
                Arrays.sort(array);
                for (int i = size / 2; i < size; i++) {
                    array[i] = random.nextInt(size);
                }
                break;
            default:
                break; // Random order (default)
        }
        return array;
    }

    private enum Order {
        RANDOM, ORDERED, PARTIALLY_ORDERED, REVERSE_ORDERED
    }

}