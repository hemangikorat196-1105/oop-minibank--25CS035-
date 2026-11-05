package util;
import model.Account;
public final class StatementFormatter {
    private StatementFormatter(){}
    public static String buildStatement(Account account){StringBuilder b=new StringBuilder();b.append("----- MINI BANK STATEMENT -----\n");b.append(account.statement()).append("\n");b.append("-------------------------------");return b.toString();}
}
