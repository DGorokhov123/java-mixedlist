package mixedlist;

import org.junit.jupiter.api.Test;

import java.util.*;

public class PerformanceTest {

    @Test
    void simpleTest() {
        List<String> strings = new MixedList<>();
        strings.add("00");   strings.add("01");   strings.add("02");   strings.add("03");   strings.add("04");
        strings.add("05");   strings.add("06");   strings.add("07");   strings.add("08");   strings.add("09");
        strings.add("10");   strings.add("11");   strings.add("12");   strings.add("13");   strings.add("14");
        strings.add("15");   strings.add("16");   strings.add("17");   strings.add("18");   strings.add("19");
        strings.add("20");   strings.add("21");   strings.add("22");   strings.add("23");   strings.add("24");
        strings.add("25");   strings.add("26");   strings.add("27");   strings.add("28");   strings.add("29");
        strings.add("30");   strings.add("31");
        for (int i = 0; i < strings.size(); i++)  System.out.print(strings.get(i) + " ");
        System.out.println();

        ListIterator<String> stringIterator = strings.listIterator();
        while (stringIterator.hasNext())  System.out.print(stringIterator.next() + " ");
        System.out.println();
        while (stringIterator.hasPrevious())  System.out.print(stringIterator.previous() + " ");
        System.out.println();
    }

    @Test
    void iteratorPerfTest() {
        long startTime;
        List<String> strings;
        Random random = new Random();
        int numItems = 10_000_000;
        System.out.printf("%-11s|%20s |%15s |%15s |%18s |%19s |%n",
                "Collection", "while hasNext()", "for each", "for i get(i)", "forEach() -> {}", "stream().forEach");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                strings = new ArrayList<>();
                System.out.printf("%-11s|", "ArrayList");
            } else if (i == 1) {
                strings = new LinkedList<>();
                System.out.printf("%-11s|", "LinkedList");
            } else {
                strings = new MixedList<>();
                System.out.printf("%-11s|", "MixedList");
            }

            for (int j = 0; j < numItems; j++)  strings.add("String #" + j);

            // while iterator hasNext()
            startTime = System.currentTimeMillis();
            ListIterator<String> iterator = strings.listIterator();
            while (iterator.hasNext()) {
                iterator.next();
            }
            System.out.printf("%20s |", (System.currentTimeMillis() - startTime) + " ms");

            // for each
            startTime = System.currentTimeMillis();
            for (String s : strings) {  }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");

            // for i get(i)
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numItems; j++) {
                strings.get(i);
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");

            // forEach() -> {}
            startTime = System.currentTimeMillis();
            strings.forEach((element) -> { });
            System.out.printf("%18s |", (System.currentTimeMillis() - startTime) + " ms");

            // stream().forEach
            startTime = System.currentTimeMillis();
            strings.stream().forEach((element) -> { });
            System.out.printf("%19s |", (System.currentTimeMillis() - startTime) + " ms");

            System.out.println();
            strings = null;
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
    }

    @Test
    void perfTest() {
        long startTime;
        List<String> strings;
        Random random = new Random();
        int numAddLast = 10_000_000;
        int numAddFirst = 5_000;
        int numRandomAdd = 1_000;
        int numRandomGet = 1_000;
        int numRandomSet = 1_000;
        int numRandomRemove = 1_000;
        System.out.printf("%-11s|%15s |%15s |%15s |%15s |%15s |%15s |%n",
                "Collection", "Add Last", "Add First", "Random Add", "Random Get", "Random Set", "Random Remove");
        System.out.printf("%-11s|%14s) |%14s) |%14s) |%14s) |%14s) |%14s) |%n",
                "", "(x" + numAddLast, "(x" + numAddFirst, "(x" + numRandomAdd, "(x" + numRandomGet,
                "(x" + numRandomSet, "(x" + numRandomRemove);
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                strings = new ArrayList<>();
                System.out.printf("%-11s|", "ArrayList");
            } else if (i == 1) {
                strings = new LinkedList<>();
                System.out.printf("%-11s|", "LinkedList");
            } else {
                strings = new MixedList<>();
                System.out.printf("%-11s|", "MixedList");
            }

            // sequential addition
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numAddLast; j++) {
                strings.add("String #" + j);
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            // first addition
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numAddFirst; j++) {
                strings.addFirst("First string #" + j);
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            // random addition
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numRandomAdd; j++) {
                strings.add(random.nextInt(strings.size()), "Additional string #" + j);
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            // random get
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numRandomGet; j++) {
                strings.get(random.nextInt(strings.size()));
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            // random set
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numRandomSet; j++) {
                strings.set(random.nextInt(strings.size()), "Some string to set");
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            // random remove
            startTime = System.currentTimeMillis();
            for (int j = 0; j < numRandomRemove; j++) {
                strings.remove(random.nextInt(strings.size()));
            }
            System.out.printf("%15s |", (System.currentTimeMillis() - startTime) + " ms");


            System.out.println();
            strings = null;
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
    }

    @Test
    void imitationTest() {
        long startTime;
        Random random = new Random();
        List<String> strings;
        int numAddLast = 100_000;
        int numIterations = 10_000;
        int[][] percent = { { 60, 25, 5, 5, 5 }, { 75, 15, 2, 4, 4 }, { 90, 5, 1, 2, 2 } };
        for (int i = 0; i < percent.length; i++) {
            if (Arrays.stream(percent[i]).reduce(0, Integer::sum) != 100) {
                System.out.println("Sum should be equal 100%");
                return;
            }
        }
        System.out.printf("%-11s|%5s%-12s |%5s%-12s |%5s%-12s |%n", "", percent[0][0], "% get(i)", percent[1][0], "% get(i)", percent[2][0], "% get(i)");
        System.out.printf("%-11s|%5s%-12s |%5s%-12s |%5s%-12s |%n", "", percent[0][1], "% add()", percent[1][1], "% add()", percent[2][1], "% add()");
        System.out.printf("%-11s|%5s%-12s |%5s%-12s |%5s%-12s |%n", "", percent[0][2], "% add(i)", percent[1][2], "% add(i)", percent[2][2], "% add(i)");
        System.out.printf("%-11s|%5s%-12s |%5s%-12s |%5s%-12s |%n", "", percent[0][3], "% set(i)", percent[1][3], "% set(i)", percent[2][3], "% set(i)");
        System.out.printf("%-11s|%5s%-12s |%5s%-12s |%5s%-12s |%n", "", percent[0][4], "% delete(i)", percent[1][4], "% delete(i)", percent[2][4], "% delete(i)");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
        System.out.println("Collection |       Set 1      |       Set 2      |       Set 3      | ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");

        for (int i = 0; i < 3; i++) {
            if (i == 0) System.out.printf("%-11s|", "ArrayList");
            //else if (i == 1) continue;
            else if (i == 1) System.out.printf("%-11s|", "LinkedList");
            else System.out.printf("%-11s|", "MixedList");

            for (int j = 0; j < percent.length; j++) {

                if (i == 0) strings = new ArrayList<>();
                else if (i == 1) strings = new LinkedList<>();
                else strings = new MixedList<>();

                for (int k = 0; k < numAddLast; k++) strings.add("String #" + k);

                startTime = System.currentTimeMillis();
                //int gets = 0, adds = 0, addis = 0, sets= 0, dels = 0;
                for (int k = 0; k < numIterations; k++) {
                    int cmd = random.nextInt(100);
                    if (cmd < percent[j][0]) {       // get(i)
                        strings.get(random.nextInt(strings.size()));
                        //gets++;
                    } else if (cmd < percent[j][0] + percent[j][1]) {     // add()
                        strings.add("Additional String");
                        //adds++;
                    } else if (cmd < percent[j][0] + percent[j][1] + percent[j][2]) {        // add(i)
                        strings.add(random.nextInt(strings.size()), "Additional string");
                        //addis++;
                    } else if (cmd < percent[j][0] + percent[j][1] + percent[j][2] + percent[j][3]) {        // set(i)
                        strings.set(random.nextInt(strings.size()), "Some string to set");
                        //sets++;
                    } else {           // delete(i)
                        strings.remove(random.nextInt(strings.size()));
                        //dels++;
                    }
                }
                System.out.printf("%17s |", (System.currentTimeMillis() - startTime) + " ms");
                //System.out.printf(" %d, %d, %d, %d, %d ", gets*100/numIterations, adds*100/numIterations,
                //        addis*100/numIterations, sets*100/numIterations, dels*100/numIterations);
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------------------");
    }

}
