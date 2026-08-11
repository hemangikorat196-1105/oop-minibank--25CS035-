import java.util.Scanner;

record BankInfo(String name, String branch) {}

enum MenuOption 
{
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    EXIT
}

public class MiniBank 
{

    public static void main(String[] args) 
    {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "Rajkot Branch");

        System.out.println(bank);

        while (true) 
        {
            System.out.println("\n----- MINI BANK MENU -----");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            MenuOption option = switch (choice) 
            {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) 
            {
                System.out.println("Invalid Choice!");
                continue;
            }

            switch (option)
            {

                case OPEN_ACCOUNT ->System.out.println("Open Account");

                case DEPOSIT ->System.out.println("Deposit");

                case WITHDRAW ->System.out.println("Withdraw");

                case TRANSFER ->System.out.println("Transfer");

                case EXIT -> 
                {
                    System.out.println("Thank You for using MiniBank.");
                    sc.close();
                    return;
                }
            }
        }
    }
}