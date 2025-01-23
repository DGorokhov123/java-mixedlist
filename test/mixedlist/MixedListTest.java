package mixedlist;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MixedListTest {

    @Test
    void removeIterator() {
        Random random = new Random();
        String err1 = "";
        String err2 = "";
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new MixedList<>();
        for (int i = 0; i < 100; i++) {
            strings1.add("Iteration #" + i);
            strings2.add("Iteration #" + i);
        }
        ListIterator<String> iterator1 = strings1.listIterator();
        ListIterator<String> iterator2 = strings2.listIterator();
        try { iterator1.remove();  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.remove();  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator2.hasNext()) {
            iterator1.next();
            iterator2.next();
            int selector = random.nextInt(100);
            if (selector < 20) {
                iterator1.remove();
                iterator2.remove();
            }
        }
        try { iterator1.remove();  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.remove();  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator2.hasPrevious()) {
            iterator1.previous();
            iterator2.previous();
            int selector = random.nextInt(100);
            if (selector < 10) {
                iterator1.remove();
                iterator2.remove();
            }
        }
        try { iterator1.remove();  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.remove();  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);
        //for (int i = 0; i < strings1.size(); i++) System.out.println(strings1.get(i) + " | " + strings2.get(i));
        assertEquals(strings1.size(), strings2.size());
        for (int i = 0; i < strings1.size(); i++)  assertEquals(strings1.get(i), strings2.get(i));
    }

    @Test
    void addIterator() {
        Random random = new Random();
        String err1 = "";
        String err2 = "";
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new MixedList<>();
        for (int i = 0; i < 100; i++) {
            strings1.add("Iteration #" + i);
            strings2.add("Iteration #" + i);
        }
        ListIterator<String> iterator1 = strings1.listIterator();
        ListIterator<String> iterator2 = strings2.listIterator();
        try { iterator1.add("Added First");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.add("Added First");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator2.hasNext()) {
            assertEquals( iterator1.next(), iterator2.next() );
            int selector = random.nextInt(100);
            if (selector < 20) {
                iterator1.add("Added at" + (iterator1.nextIndex() - 1) );
                iterator2.add("Added at" + (iterator2.nextIndex() - 1) );
            }
        }
        try { iterator1.add("Added Last");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.add("Added Last");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator1.hasPrevious()) {
            assertEquals( iterator1.previous(), iterator2.previous() );
            int selector = random.nextInt(100);
            if (selector < 10) {
                iterator1.add("Added at" + iterator1.nextIndex());
                iterator2.add("Added at" + iterator2.nextIndex());
            }
        }
        try { iterator1.add("Added First2");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.add("Added First2");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);
        assertEquals(strings1.size(), strings2.size());
        for (int i = 0; i < strings1.size(); i++)  assertEquals(strings1.get(i), strings2.get(i));
        //for (int i = 0; i < strings1.size(); i++) System.out.println(i + ": " + strings1.get(i) + " | " + strings2.get(i));
    }



    @Test
    void setIterator() {
        Random random = new Random();
        String err1 = "";
        String err2 = "";
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new MixedList<>();
        for (int i = 0; i < 100; i++) {
            strings1.add("Iteration #" + i);
            strings2.add("Iteration #" + i);
        }
        ListIterator<String> iterator1 = strings1.listIterator();
        ListIterator<String> iterator2 = strings2.listIterator();
        try { iterator1.set("Set First");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.set("Set First");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator2.hasNext()) {
            iterator1.next();
            iterator2.next();
            int selector = random.nextInt(100);
            if (selector < 20) {
                iterator1.set("Set at" + (iterator1.nextIndex() - 1) );
                iterator2.set("Set at" + (iterator2.nextIndex() - 1) );
            }
        }
        try { iterator1.set("Set Last");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.set("Set Last");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);

        while (iterator1.hasPrevious()) {
            iterator1.previous();
            iterator2.previous();
            int selector = random.nextInt(100);
            if (selector < 10) {
                iterator1.set("Set at" + iterator1.nextIndex());
                iterator2.set("Set at" + iterator2.nextIndex());
            }
        }
        try { iterator1.set("Set First2");  err1 = ""; } catch (Exception e) { err1 = e.toString(); }
        try { iterator2.set("Set First2");  err2 = ""; } catch (Exception e) { err2 = e.toString(); }
        assertEquals(err1, err2);
        //for (int i = 0; i < strings1.size(); i++) System.out.println(i + ": " + strings1.get(i) + " | " + strings2.get(i));
        assertEquals(strings1.size(), strings2.size());
        for (int i = 0; i < strings1.size(); i++)  assertEquals(strings1.get(i), strings2.get(i));
    }





    @Test
    void nextPreviousIterator() {
        Random random = new Random();
        List<String> gets1 = new ArrayList<>();
        List<String> gets2 = new ArrayList<>();
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new MixedList<>();
        for (int i = 0; i < 5; i++) {
            strings1.add("Iteration #" + i);
            strings2.add("Iteration #" + i);
        }
        ListIterator<String> iterator1 = strings1.listIterator();
        ListIterator<String> iterator2 = strings2.listIterator();
        for (int i = 0; i < 1000; i++) {
            int selector = random.nextInt(100);
            if (selector < 50) {
                if (iterator1.hasNext()) gets1.add(iterator1.next());
                if (iterator2.hasNext()) gets2.add(iterator2.next());
            } else {
                if (iterator1.hasPrevious()) gets1.add(iterator1.previous());
                if (iterator2.hasPrevious()) gets2.add(iterator2.previous());
            }
        }
        assertEquals(strings1.size(), strings2.size());
        assertEquals(gets1.size(), gets2.size());
        for (int i = 0; i < strings1.size(); i++)  assertEquals(strings1.get(i), strings2.get(i));
        for (int i = 0; i < gets1.size(); i++)  assertEquals(gets1.get(i), gets2.get(i));
    }



    @Test
    void randomAddSetRemove() {
        Random random = new Random();
        List<String> strings1 = new ArrayList<>();
        List<String> strings2 = new MixedList<>();
        strings1.add("First Element");
        strings2.add("First Element");

        for (int i = 0; i < 10000; i++) {
            int selector = random.nextInt(100);
            if (selector < 40) {
                strings1.add("Iteration #" + i + ": addition in the end");
                strings2.add("Iteration #" + i + ": addition in the end");
            } else if (selector < 50) {
                if (strings1.isEmpty()) continue;
                int rnd = random.nextInt(strings1.size());
                strings1.set(rnd, "Iteration #" + i + ": set new value");
                strings2.set(rnd, "Iteration #" + i + ": set new value");
            } else if (selector < 60) {
                strings1.addFirst("Iteration #" + i + ": addition in the beginning");
                strings2.addFirst("Iteration #" + i + ": addition in the beginning");
            } else if (selector < 70) {
                if (strings1.isEmpty()) continue;
                int rnd = random.nextInt(strings1.size());
                strings1.add(rnd, "Iteration #" + i + ": random addition");
                strings2.add(rnd, "Iteration #" + i + ": random addition");
            } else {
                if (strings1.isEmpty()) continue;
                int rnd = random.nextInt(strings1.size());
                strings1.remove(rnd);
                strings2.remove(rnd);
            }
        }

        assertEquals(strings1.size(), strings2.size());
        for (int i = 0; i < strings1.size(); i++)  assertEquals(strings1.get(i), strings2.get(i));
    }


}