import org.json.JSONObject;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        PlaidService plaid = new PlaidService();
        try {
            plaid.initializeSandboxSession();
            JSONObject accounts = plaid.getAccounts();
            System.out.println("Accounts:\n" + accounts.toString(2));

            JSONObject transactions = plaid.getTransactions("2024-12-01", "2025-01-01");
            System.out.println("Transactions:\n" + transactions.toString(2));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
