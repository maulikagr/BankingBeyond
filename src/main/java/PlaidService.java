import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import retrofit2.Response;

public class PlaidService {
    private PlaidApi plaidClient;
    private String accessToken;

    public void initializeSandboxSession() throws IOException {
        HashMap<String, String> apiKeys = new HashMap<>();
        apiKeys.put("clientId", "67f16f1f246ca90023f97d37");
        apiKeys.put("secret", "da567f1365616326d68a78ed957f09");

        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox); // Use sandbox environment
        plaidClient = apiClient.createService(PlaidApi.class);

        // Create a sandbox public token
        SandboxPublicTokenCreateRequest request = new SandboxPublicTokenCreateRequest()
            .institutionId("ins_3")
            .initialProducts(List.of(Products.TRANSACTIONS));

        try {
            Response<SandboxPublicTokenCreateResponse> response = plaidClient.sandboxPublicTokenCreate(request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to create sandbox public token: " + response.errorBody().string());
            }
            String publicToken = response.body().getPublicToken();

            // Exchange public token for access token
            ItemPublicTokenExchangeRequest exchangeRequest = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);
            Response<ItemPublicTokenExchangeResponse> exchangeResponse = plaidClient.itemPublicTokenExchange(exchangeRequest).execute();
            if (!exchangeResponse.isSuccessful() || exchangeResponse.body() == null) {
                throw new IOException("Failed to exchange public token: " + exchangeResponse.errorBody().string());
            }
            accessToken = exchangeResponse.body().getAccessToken();
        } catch (Exception e) {
            throw new IOException("Plaid initialization failed", e);
        }
    }

    public JSONObject getAccounts() throws IOException {
        AccountsGetRequest request = new AccountsGetRequest()
            .accessToken(accessToken);
        try {
            Response<AccountsGetResponse> response = plaidClient.accountsGet(request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to get accounts: " + response.errorBody().string());
            }
            return new JSONObject(response.body().toString());
        } catch (Exception e) {
            throw new IOException("Plaid getAccounts failed", e);
        }
    }

    public JSONObject getTransactions(String startDate, String endDate) throws IOException {
        TransactionsGetRequest request = new TransactionsGetRequest()
            .accessToken(accessToken)
            .startDate(LocalDate.parse(startDate))
            .endDate(LocalDate.parse(endDate));
        try {
            Response<TransactionsGetResponse> response = plaidClient.transactionsGet(request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                throw new IOException("Failed to get transactions: " + response.errorBody().string());
            }
            return new JSONObject(response.body().toString());
        } catch (Exception e) {
            throw new IOException("Plaid getTransactions failed", e);
        }
    }
}
 