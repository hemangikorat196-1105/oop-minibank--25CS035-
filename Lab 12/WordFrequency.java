import java.util.*;

public class WordFrequency {
    public static void main(String[] args) {

        String paragraph =
            "java is easy and java is powerful. " +
            "java is popular and java is useful.";

        // Remove punctuation and convert to lowercase
        paragraph = paragraph.toLowerCase()
                             .replaceAll("[^a-z ]", "");

        String[] words = paragraph.split("\\s+");

        HashMap<String, Integer> frequency = new HashMap<>();

        // Count words
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }

        // Sort words by frequency
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(frequency.entrySet());

        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b) {

                return b.getValue().compareTo(a.getValue());
            }
        });

        System.out.println("Words sorted by frequency:");

        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}