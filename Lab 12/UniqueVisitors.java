import java.util.*;

public class UniqueVisitors {
    public static void main(String[] args) {

        List<String> logins = Arrays.asList("hemangi","Dhanushi","khushi","hemangi","yatri","khushi","nensi","yatri");

        // HashSet for unique users
        HashSet<String> uniqueUsers = new HashSet<>();

        // LinkedHashSet keeps first-seen order
        LinkedHashSet<String> firstSeenUsers = new LinkedHashSet<>();

        for (String name : logins) {
            uniqueUsers.add(name);
            firstSeenUsers.add(name);
        }

        System.out.println("Unique users: " + uniqueUsers.size());

        System.out.println("First three distinct users:");

        int count = 0;

        for (String name : firstSeenUsers) {
            System.out.println(name);

            count++;

            if (count == 3) {
                break;
            }
        }
    }
}