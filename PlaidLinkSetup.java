import com.plaid.client.PlaidClient;
import com.plaid.client.request.ItemPublicTokenExchangeRequest;
import com.plaid.client.request.LinkTokenCreateRequest;
import com.plaid.client.request.LinkTokenCreateRequestUser;
import com.plaid.client.response.ItemPublicTokenExchangeResponse;
import com.plaid.client.response.LinkTokenCreateResponse;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class PlaidLinkSetup {
    private final PlaidClient client;

    public PlaidLinkSetup() {
        String env = System.getenv("PLAID_ENV");
        PlaidClient.Builder builder = PlaidClient.newBuilder()
            .clientIdAndSecret(System.getenv("PLAID_CLIENT_ID"), System.getenv("PLAID_SECRET"))
            .sandboxBaseUrl(); // Default to sandbox

        if ("production".equalsIgnoreCase(env)) {
            builder = builder.productionBaseUrl();
        } else if ("development".equalsIgnoreCase(env)) {
            builder = builder.developmentBaseUrl();
        }

        this.client = builder.build();
    }

    public String createLinkToken(String userId) {
        try {
            String clientUserId = (userId != null && !userId.isEmpty()) ? userId : UUID.randomUUID().toString();

            LinkTokenCreateRequest request = new LinkTokenCreateRequest()
                .clientName("Finance App")
                .language("en")
                .countryCodes(Arrays.asList("US"))
                .user(new LinkTokenCreateRequestUser().clientUserId(clientUserId))
                .products(Arrays.asList("transactions"));

            Response<LinkTokenCreateResponse> response = client.service()
                .linkTokenCreate(request)
                .execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getLinkToken();
            } else {
                System.err.println("Error creating link token: " + response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String exchangePublicToken(String publicToken) {
        try {
            ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(publicToken);

            Response<ItemPublicTokenExchangeResponse> response = client.service()
                .itemPublicTokenExchange(request)
                .execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getAccessToken();
            } else {
                System.err.println("Error exchanging public token: " + response.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

 
    
 
