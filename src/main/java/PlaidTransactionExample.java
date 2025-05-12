import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaidTransactionExample {

    private static final String CLIENT_ID = "67f16f1f246ca90023f97d37";
    private static final String SECRET = "da567f1365616326d68a78ed957f09";
    private static final String BASE_URL = "https://sandbox.plaid.com";

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        String linkToken = createLinkToken();
        System.out.println("Link token: " + linkToken);

        // Simulated public token for sandbox — use Plaid Link in real implementation
        String publicToken = "sandbox-public-token-abc123"; // Replace with real token from Link flow

        String accessToken = exchangePublicToken(publicToken);
        System.out.println("Access token: " + accessToken);

        fetchAccounts(accessToken);
        fetchTransactions(accessToken);
    }

    private static String createLinkToken() throws IOException {
        Map<String, Object> user = new HashMap<>();
        user.put("client_user_id", "user-123");

        Map<String, Object> data = new HashMap<>();
        data.put("client_id", CLIENT_ID);
        data.put("secret", SECRET);
        data.put("client_name", "My Java App");
        data.put("language", "en");
        data.put("country_codes", List.of("US"));
        data.put("user", user);
        data.put("products", List.of("transactions"));

        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/link/token/create")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode resJson = mapper.readTree(response.body().string());
            return resJson.get("link_token").asText();
        }
    }

    private static String exchangePublicToken(String publicToken) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("client_id", CLIENT_ID);
        data.put("secret", SECRET);
        data.put("public_token", publicToken);

        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/item/public_token/exchange")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode resJson = mapper.readTree(response.body().string());
            return resJson.get("access_token").asText();
        }
    }

    private static void fetchAccounts(String accessToken) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put("client_id", CLIENT_ID);
        data.put("secret", SECRET);
        data.put("access_token", accessToken);

        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/accounts/get")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode resJson = mapper.readTree(response.body().string());
            System.out.println("Accounts:\n" + resJson.toPrettyString());
        }
    }

    private static void fetchTransactions(String accessToken) throws IOException {
        String startDate = LocalDate.now().minusMonths(1).toString();
        String endDate = LocalDate.now().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("client_id", CLIENT_ID);
        data.put("secret", SECRET);
        data.put("access_token", accessToken);
        data.put("start_date", startDate);
        data.put("end_date", endDate);
        data.put("options", Map.of("count", 10));  // Limit for demo purposes

        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, MediaType.get("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL + "/transactions/get")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            JsonNode resJson = mapper.readTree(response.body().string());
            System.out.println("Transactions:\n" + resJson.toPrettyString());
        }
    }
}