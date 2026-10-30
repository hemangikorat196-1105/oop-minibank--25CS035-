
package model;

import util.Repository;

public class RepositoryDemo {

    public static void main(String[] args) {

        Repository<Account> repository = new Repository<>();

        Account a1 = new SavingsAccount(1001, 5000, 1000);
        Account a2 = new CurrentAccount(1002, 2000, 1000);
        Account a3 = new FixedDepositAccount(1003, 9000);

        repository.saveAll(a1, a2, a3);

        System.out.println("Find account 1002:");
        System.out.println(repository.findById("1002"));

        System.out.println("\nAll accounts:");
        for (Account account : repository.findAll()) {
            System.out.println(account);
        }

        System.out.println("\nDelete account 1001:");
        System.out.println(repository.delete("1001"));

        System.out.println("Remaining accounts: " + repository.findAll().size());

        Repository<Customer> customerRepository = new Repository<>();
        Customer customer = new Customer(101, "Rahul", "rahul@gmail.com");
        customerRepository.save(customer);

        System.out.println("\nCustomer repository tested.");
    }
}
