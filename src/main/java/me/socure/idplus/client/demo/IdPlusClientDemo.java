package me.socure.idplus.client.demo;

import com.socure.idplus.client.*;
import com.socure.idplus.client.api.*;
import com.socure.idplus.client.models.*;

import java.util.ArrayList;
import java.util.List;

public class IdPlusClientDemo {

    public static void demo() {

        try {

            /*
                Step 1 - Use the correct ID+ key

                Ensure you use a valid ID+ key for the appropriate Socure environment. 
                It is strictly advised not to hard code the ID+ key directly in the code. 
                Instead, fetch it from a secured config file or via environment variables.
             */
            String apiKey = "id_plus_key";


            /*
                Step 2 - Create an 'IdPlusApi' instance

                Depending on the ID+ key environment chosen in step 1, use one of the following methods
                to create an API instance:

                1. IdPlusApiFactory.getProductionApi(prod_api_key) should be used for Socure Production/Certification environment
                2. IdPlusApiFactory.getSandboxApi(sandbox_api_key) should be used for Socure Sandbox environment
             */

            IdPlusApi apiInstance = IdPlusApiFactory.getProductionApi(apiKey);

            /*
                Step 3 - Create an 'IdPlusRequest' object

                This example creates a sample 'IdPlusRequest' object with modules 'fraud', 'kyc', 'emailrisk', 'phonerisk', and 'decision'.
                Refer to the later section for a complete reference on the 'IdPlusRequest' model class.
             */

            List<String> modules = new ArrayList<>();
            modules.add("fraud");
            modules.add("kycplus");
            modules.add("emailrisk");
            modules.add("phonerisk");
            modules.add("decision");


            AuthenticationContext authenticationContext = new AuthenticationContext();
            authenticationContext
                    .isCaptchaUsed(false)
                    .loginAttemptTime("2024-08-06T00:00:00Z")
                    .userCreatedTime("2024-08-06T00:00:00Z")
                    .userUpdatedTime("2024-08-06T00:00:00Z")
                    .lastSuccessfulLoginTime("2024-08-06T00:00:00Z")
                    .pageIdentifier("somebank.com");

            IdPlusRequest idPlusRequest = new IdPlusRequest();
            idPlusRequest
                    .firstName("sam")
                    .surName("hua")
                    .dob("1999-01-01")
                    .country("US")
                    .email("tester@tester.com")
                    .mobileNumber("+19999998888")
                    .userConsent(true)
                    .authenticationContext(authenticationContext)
                    .modules(modules);

            /*
                Step 4 - Invoke the 'verify' method (blocking call).

                Returns the 'CompletableFuture<IdPlusResponse>' object on a successful call with HTTP status code 200 or OK.
                Throws the 'ApiException' object for other cases.

                Refer to the later section for a complete reference on the 'IdPlusResponse' model class.
             */
            IdPlusResponse response = apiInstance.processRequest(idPlusRequest);
            if (response != null) {
                /*
                    Step 5 - Parse the 'IdPlusResponse' fields
                 */

                //Fetching the referenceId (aka transactionId) from the output
                System.out.println(response.getReferenceId());

                /*
                    Depending on the modules invoked, you could see the respective module based response.
                    In this example, we invoked the fraud module and the code below parses the reason codes for the same.

                    Note: You may receive a 'null' object when accessing response fields for modules that were not included in the request. Any further actions on a 'null' object may result in a 'RuntimeException' of type 'NullPointerException'.

                    Please read the complete reference on the different fields available for various modules in the 'IdPlusResponse' model class.
                 */
                System.out.println(response.getFraud().getReasonCodes());

                /*
                    Parsing the decision module overall result (applicable only when the decision module
                    is included in the 'IdPlusRequest' modules field)
                 */
                System.out.println(response.getDecision().getValue());
            }
        } catch (ApiException e) {
            /*
                Step 6 - Parse the error response
             */

            System.err.println("Exception when calling IdPlusApi#processRequest");
            System.err.println("Status code: " + e.getCode());
            System.err.println("Reason: " + e.getResponseBody());
            System.err.println("Response headers: " + e.getResponseHeaders());

            IdPlusErrorResponseParser.parse(e.getResponseBody())
                    .ifPresent(idPlusErrorResponse -> {
                        System.out.println(idPlusErrorResponse.getStatus());
                        System.out.println(idPlusErrorResponse.getReferenceId());
                        System.out.println(idPlusErrorResponse.getMsg());
                    });
        }
    }
    public static void main(String[] args) {
        demo();
    }
}