package api.payout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DataBase.MySqlConnection;
import DataBase.MySqlConnectionForPayotInsert;
import genricLibraries.PropertiesUtility;
import genricLibraries.UtilitiesPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ImpsPayoutPayload {

	public static List<String> payoutRefToCheckWebhook = null;
	public static List<String> webhookFoundPayoutRef = null;

	public static void main(String[] args) throws Exception {
		payoutRefToCheckWebhook = new ArrayList<String>();
		webhookFoundPayoutRef = new ArrayList<String>();

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		Runnable task = new Runnable() {
			@Override
			public void run() {

				try {
					Response response = impsPayout();
					System.out.println("Payout done checking for webhooks..........");
					//System.out.println("Array size: "+payoutRefToCheckWebhook.size());
					for (int i = 0; i < payoutRefToCheckWebhook.size(); i++) {
						// System.out.println(payoutRefToCheckWebhook.get(i));
						boolean flag = MySqlConnection.webhookChecker(payoutRefToCheckWebhook.get(i));
						if (flag) {
							System.out.println("Webhook found for the payout_ref: " + payoutRefToCheckWebhook.get(i)
									+ ", and removed from the list.");
							webhookFoundPayoutRef.add(payoutRefToCheckWebhook.get(i));
						}
					}
					for (int i = 0; i < webhookFoundPayoutRef.size(); i++) {
						payoutRefToCheckWebhook.remove(webhookFoundPayoutRef.get(i));
					}
					webhookFoundPayoutRef.clear();
					System.out.println("Webhooks pending for mentioned payouts: " + payoutRefToCheckWebhook);
					System.out.println(
							"------------------------------------------------------------------------------------");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		//This is running to check the webhooks continuosly for 1 hr with 10 minutes interval
		final Runnable postShutdownTask = new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Checking the payouts i.e webhooks are not yet received....");
					//System.out.println(payoutRefToCheckWebhook.size());
					while (payoutRefToCheckWebhook.size() > 0) {
						for (int i = 0; i < payoutRefToCheckWebhook.size(); i++) {
							// System.out.println(payoutRefToCheckWebhook.get(i));
							boolean flag = MySqlConnection.webhookChecker(payoutRefToCheckWebhook.get(i));
							if (flag) {
								System.out.println("Webhook found for the payout_ref: " + payoutRefToCheckWebhook.get(i)
										+ " and removed from the list");
								webhookFoundPayoutRef.add(payoutRefToCheckWebhook.get(i));
							}
						}
						for (int i = 0; i < webhookFoundPayoutRef.size(); i++) {
							payoutRefToCheckWebhook.remove(webhookFoundPayoutRef.get(i));
						}
						webhookFoundPayoutRef.clear();
						System.out.println("Webhooks pending for mentioned payouts: " + payoutRefToCheckWebhook);
						System.out.println(
								"------------------------------------------------------------------------------------");
						if(!(payoutRefToCheckWebhook.size()==0)) {
							System.out.println("Recheck will happen after 1 minutes");
							TimeUnit.MINUTES.sleep(1);
						}
						
					}
					System.out.println("***********Execution completed*****************");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		};

		// Scheduling a task to run at fixed delay
		scheduler.scheduleWithFixedDelay(task, 0, 60, TimeUnit.SECONDS);

		// Optionally, shutdown the scheduler after a certain period
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				scheduler.shutdown();
				try {
					postShutdownTask.run();
					
				} catch (Exception e) {
					Thread.currentThread().interrupt();
					System.err.println("Interrupted while waiting for scheduler shutdown.");
				}
			}

		}, 60, TimeUnit.MINUTES);

	}

	// This method contains the impsPayout request body and if we got 200 response
	// code updating the payout to database
	public static Response impsPayout() throws Exception {
		Response response = null;
		try {
			PropertiesUtility property = new PropertiesUtility();
			property.propertiesInit(UtilitiesPath.PROPERTIES_PATH);
			int payoutCount = Integer.parseInt(property.readData("payoutRefCount"));

			Map<String, Object> escrowData = new LinkedHashMap<>();

			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String currentDate = date.format(formatter);
			Map<String, Object> payout = new LinkedHashMap<>();
			payout.put("payout_ref", currentDate + "GuruALiveImps" + payoutCount);
			property.writeToProperties("payoutRefCount", ++payoutCount + "", UtilitiesPath.PROPERTIES_PATH);
			payout.put("amount", 1);
			payout.put("payout_mode", "IMPS");
			payout.put("transaction_note", "LiveTesting");

			Map<String, Object> payee = new LinkedHashMap<>();
			payee.put("user_ref", "test-user");
			payee.put("company_name", "guru softwares");
			payee.put("user_name", "guruprasad");
			payee.put("user_mobile_number", "8970486528");

			// Add payee to payout
			payout.put("payee", payee);

			Map<String, Object> beneficiary = new LinkedHashMap<>();
			beneficiary.put("account_name", "Guruprasad");
			beneficiary.put("account_no", "4449682902");
			beneficiary.put("ifsc", "KKBK0008061");

			// Add beneficiary to payout
			payout.put("beneficiary", beneficiary);

			JSONArray payoutArray = new JSONArray();
			payoutArray.add(payout);

			// Add payout to escrowData as array
			escrowData.put("payouts", payoutArray);

			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/M/yyyy, h:mm:ss a");
			escrowData.put("timestamp", date.format(formatter1));

			// Accessing private key from local file
			String privateKeyPath = "./PrivateKey/livePrivate.key";
			String privateKey = null;
			try {
				privateKey = Files.readString(Paths.get(privateKeyPath));
			} catch (IOException e) {
				System.err.println("Mention a correct private key file path: " + e.getMessage());
				e.printStackTrace();
			}
			String signature = generateRSASignature(convertToJson(escrowData), privateKey);
			escrowData.put("signature", signature);

//			It is the apikey for header and to access the api
			// Accessing Api key from local file
			String apiKeyPath = "./PrivateKey/liveApi.key";
			String apiKey = null;
			try {
				apiKey = Files.readString(Paths.get(apiKeyPath));
			} catch (IOException e) {
				System.err.println("Mention a correct API key file path: " + e.getMessage());
				e.printStackTrace();
			}

//			It is for invoking the api 
			//sandbox url: https://sandboxpayout.eazypayouts.com/payout/v1/sandbox
			//live url: https://pay.eazypayouts.com/payout/v1/prod
			response = RestAssured.given().contentType(ContentType.JSON).accept(ContentType.JSON)
					.header("apikey", apiKey).body(convertToJson(escrowData))
					.baseUri("https://pay.eazypayouts.com/payout/v1/prod").when().post().then().extract()
					.response();

			if (response.statusCode() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode requestBody = objectMapper.readTree(convertToJson(escrowData));
				JsonNode jsonArray = requestBody.path("payouts");
				String timestamp = requestBody.path("timestamp").asText();
				DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy, h:mm:ss a");
				DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime dateTime = LocalDateTime.parse(timestamp, inputFormatter);

				for (JsonNode jsonBlock : jsonArray) {
					String payoutRef = jsonBlock.path("payout_ref").asText();
					String amt = jsonBlock.path("amount").asText();
					String mode = jsonBlock.path("payout_mode").asText();
					JsonNode beneBlock = jsonBlock.path("beneficiary");
					String acc_no = beneBlock.path("account_no").asText();
					String ifsc = beneBlock.path("ifsc").asText();
					MySqlConnectionForPayotInsert.insertPayoutToDB(payoutRef, dateTime.format(outputFormatter), amt,
							mode, acc_no, ifsc, response.getBody().asString());
					payoutRefToCheckWebhook.add(payoutRef);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return response;
	}

	// This method is used to signature generation
	public static String generateRSASignature(String payload, String privateKeyPEM) throws Exception {
//      Remove the BEGIN and END lines and any whitespace characters
		privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "")
				.replace("-----END PRIVATE KEY-----", "").replaceAll("\\s", "");

		// Base64 decode the private key
		byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPEM);

		// Create a PKCS8EncodedKeySpec from the decoded private key bytes
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

		// Initialize a KeyFactory for RSA
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		// Generate a PrivateKey from the key specification
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		// Create a Signature instance using SHA256withRSA
		Signature rsaSignature = Signature.getInstance("SHA256withRSA");

		// Initialize the Signature with the private key
		rsaSignature.initSign(privateKey);

		// Update the Signature with the payload bytes
		rsaSignature.update(payload.getBytes(StandardCharsets.UTF_8));

		// Generate the signature
		byte[] signatureBytes = rsaSignature.sign();

		// Encode the signature as Base64
		String encodedSignature = Base64.getEncoder().encodeToString(signatureBytes);

		return encodedSignature;
	}

	// converting the LinkedHashMap to JSON object
	private static String convertToJson(Map<String, Object> payload) {
		// For simplicity, converting to a string manually
		StringBuilder jsonBuilder = new StringBuilder("{");

		for (Map.Entry<String, Object> entry : payload.entrySet()) {
			jsonBuilder.append("\"").append(entry.getKey()).append("\":");
			if (entry.getValue() instanceof String) {
				jsonBuilder.append("\"").append(entry.getValue()).append("\",");
			} else {
				jsonBuilder.append(entry.getValue()).append(",");
			}
		}
		// Remove the trailing comma if there are entries
		if (payload.size() > 0) {
			jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
		}

		jsonBuilder.append("}");

		return jsonBuilder.toString();
	}

}
