import java.util.Scanner;

enum Coin{
        ONE,TWO,FIVE,TEN
    }

public class VendingMachine {
    
    public static void main(String[] args) {
        final int price = 15;
        int total = 0;

        Scanner sc = new Scanner(System.in);

        System.out.println("Snack Price ="+price);

        while(total<price)
        {
            System.out.println("Enter coin(one,two,five,ten) : ");
            String input = sc.next().toUpperCase();

            try
            {
                Coin coin = Coin.valueOf(input);

                int value = switch(coin)
                {
                    case ONE -> 1;
                    case TWO -> 2;
                    case FIVE -> 5;
                    case TEN -> 10;
                };

                total += value;
                System.out.println("Total so far: "+total);
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Invalid coin! Try again.");
            }
        }
        System.out.println("Paid.");
        System.out.println("Change: "+ (total - price));

        sc.close();
    }
}