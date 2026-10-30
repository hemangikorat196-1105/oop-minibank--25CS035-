
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CollectionsDemo {

    public static void main(String[] args) {

        // AccountStore demonstration
        AccountStore store = new AccountStore();

        store.add(new SavingsAccount(1001, 5000, 1000));
        store.add(new CurrentAccount(1002, 2000, 1000));
        store.add(new FixedDepositAccount(1003, 9000));

        System.out.println("Account AC0002 lookup:");
        System.out.println(store.get("AC1002"));

        System.out.println("\nAccounts sorted by balance:");
        for (Account account : store.sortedByBalance()) {
            System.out.println(account);
        }

        // List and Set demonstration
        List<String> visitors = List.of(
                "Rahul", "Priya", "Rahul", "Amit", "Priya", "Neha");

        Set<String> uniqueVisitors = new HashSet<>(visitors);
        Set<String> firstSeen = new LinkedHashSet<>(visitors);

        System.out.println("\nUnique visitors: " + uniqueVisitors.size());
        System.out.println("First three distinct visitors:");

        int count = 0;
        for (String visitor : firstSeen) {
            if (count == 3) {
                break;
            }
            System.out.println(visitor);
            count++;
        }

        // Leaderboard demonstration
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Priya", 95);
        scores.put("Amit", 90);
        scores.put("Rahul", 95);
        scores.put("Neha", 85);

        ArrayList<Map.Entry<String, Integer>> leaderboard =
                new ArrayList<>(scores.entrySet());

        leaderboard.sort(
                Comparator.<Map.Entry<String, Integer>>comparingInt(
                        Map.Entry::getValue)
                        .reversed()
                        .thenComparing(Map.Entry::getKey));

        System.out.println("\nLeaderboard:");
        for (Map.Entry<String, Integer> entry : leaderboard) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }
}
