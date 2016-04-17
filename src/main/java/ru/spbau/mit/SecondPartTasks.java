package ru.spbau.mit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class SecondPartTasks {

    private static final long SAMPLES_NUM = 1000_000L;
    private static final double R = 0.5;

    private SecondPartTasks() {
    }

    // Найти строки из переданных файлов, в которых встречается указанная подстрока.
    public static List<String> findQuotes(List<String> paths, CharSequence sequence) {
        final Function<String, Stream<String>> mapper = p -> {
            try {
                return Files.lines(Paths.get(p));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        return paths.stream().flatMap(mapper).filter(p -> p.contains(sequence)).collect(Collectors.toList());
    }

    // В квадрат с длиной стороны 1 вписана мишень.
    // Стрелок атакует мишень и каждый раз попадает в произвольную точку квадрата.
    // Надо промоделировать этот процесс с помощью класса java.util.Random и посчитать,
    // какова вероятность попасть в мишень.
    public static double piDividedBy4() {
        final Function<Double, Double> sqr = x -> x * x;
        final PrimitiveIterator.OfDouble it = new Random().doubles().iterator();
        return Stream.generate(() -> sqr.apply(it.next() - R) + sqr.apply(it.next() - R)).limit(SAMPLES_NUM).
                filter(distSqr -> distSqr < R * R).count() / (double) SAMPLES_NUM;
    }

    // Дано отображение из имени автора в список с содержанием его произведений.
    // Надо вычислить, чья общая длина произведений наибольшая.
    public static String findPrinter(Map<String, List<String>> compositions) {
        return compositions.entrySet().stream().
                collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().stream().mapToInt(String::length).sum())).
                entrySet().stream().max(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).
                orElse("");
    }

    // Вы крупный поставщик продуктов. Каждая торговая сеть делает вам заказ в виде Map<Товар, Количество>.
    // Необходимо вычислить, какой товар и в каком количестве надо поставить.
    public static Map<String, Integer> calculateGlobalOrder(List<Map<String, Integer>> orders) {
        return orders.stream().map(Map::entrySet).flatMap(Set::stream).
                collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
    }
}
