import java.util.Scanner;

public class Driver1 
{
    public static void main(String[] args) 
    {

        String[] logs = 
        {
            "10:05 alice Hello there",
            "10:10 bob How are you",
            "10:15",                     
            "10:20 charlie hello everyone"
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        System.out.println(ChatFilter.filterMessages(logs, keyword));

        sc.close();
    }
}