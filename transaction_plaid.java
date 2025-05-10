import com.plaid.client.PlaidClient;
import com.plaid.client.request.*;
import com.plaid.client.response.*;
import retrofit2.Response;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class PlaidClientWrapper {
    private final PlaidClient client;

    public PlaidClientWrapper() {
        String env = System.getenv("PLAID_ENV");
        PlaidClient.Builder builder = PlaidClient.newBuilder()
            .clientIdAndSecret(System.getenv("PLAID_CLIENT_ID"), System.getenv("PLAID_SECRET"))
            .sandboxBaseUrl(); // default

        if ("production".equalsIgnoreCase(env)) {
            builder = builder.productionBaseUrl();
        } else if ("development".equalsIgnoreCase(env)) {
            builder = builder.developmentBaseUrl();
        }

        this.client = builder.build();
    }

    public List<Map<String, Object>> getTransactions(String accessToken, LocalDate startDate, LocalDate endDate) {
        List<Map<String, Object>> transactionsList = new ArrayList<>();
        int offset = 0;
        int total = 0;

        do {
            try {
                TransactionsGetRequest request = new TransactionsGetRequest()
                    .accessToken(accessToken)
                    .startDate(startDate)
                    .endDate(endDate)
                    .options(new TransactionsGetRequest.Options()
                        .count(100)
                        .offset(offset)
                    );

                Response<TransactionsGetResponse> response = client.service()
                    .transactionsGet(request)
                    .execute();

                if (response.isSuccessful() && response.body() != null) {
                    List<Transaction> transactions = response.body().getTransactions();
                    total = response.body().getTotalTransactions();

                    for (Transaction t : transactions) {
                        Map<String, Object> transactionData = new HashMap<>();
                        transactionData.put("date", t.getDate() != null ? t.getDate().toString() : null);
                        transactionData.put("amount", t.getAmount());
                        transactionData.put("name", t.getName());
                        transactionData.put("category", t.getCategory() != null ? t.getCategory() : Collections.singletonList("Uncategorized"));
                        transactionData.put("transaction_id", t.getTransactionId());
                        transactionData.put("merchant_name", t.getMerchantName());
                        transactionData.put("payment_channel", t.getPaymentChannel());
                        transactionData.put("pending", t.getPending());

                        transactionsList.add(transactionData);
                    }

                    offset += transactions.size();
                } else {
                    System.err.println("Error retrieving transactions: " + response.errorBody().string());
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        } while (transactionsList.size() < total);

        return transactionsList;
    }

    public List<Map<String, Object>> getBalances(String accessToken) {
        List<Map<String, Object>> accountsList = new ArrayList<>();
        try {
            AccountsBalanceGetRequest request = new AccountsBalanceGetRequest().accessToken(accessToken);

            Response<AccountsGetResponse> response = client.service()
                .accountsBalanceGet(request)
                .execute();

            if (response.isSuccessful() && response.body() != null) {
                for (Account account : response.body().getAccounts()) {
                    Map<String, Object> accountData = new HashMap<>();
                    Map<String, Object> balances = new HashMap<>();

                    balances.put("available", account.getBalances().getAvailable());
                    balances.put("current", account.getBalances().getCurrent());
                    balances.put("limit", account.getBalances().getLimit());
                    balances.put("iso_currency_code", account.getBalances().getIsoCurrencyCode());
                    balances.put("unofficial_currency_code", account.getBalances().getUnofficialCurrencyCode());

                    accountData.put("account_id", account.getAccountId());
                    accountData.put("name", account.getName());
                    accountData.put("type", account.getType());
                    accountData.put("subtype", account.getSubtype());
                    accountData.put("balances", balances);

                    accountsList.add(accountData);
                }
            } else {
                System.err.println("Error retrieving balances: " + response.errorBody().string());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accountsList;
    }
}
