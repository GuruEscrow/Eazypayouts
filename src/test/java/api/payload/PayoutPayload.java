package api.payload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.simple.JSONArray;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DataBase.MySqlConnectionForPayotInsert;
import api.resource.SignatureGenerator;
import eazypayout.genric_libraries.PropertiesUtility;
import eazypayout.genric_libraries.UtilitiesPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PayoutPayload {
	
	public static Response payoutRequestBody(String mode,double amt) throws Exception {
		Response response = null;
		try {
			PropertiesUtility property = new PropertiesUtility();
			property.propertiesInit(UtilitiesPath.PROPERTIES_PATH);
			int payoutCount = Integer.parseInt(property.readData("autopayoutRef"));

			Map<String, Object> escrowData = new LinkedHashMap<>();

			LocalDateTime date = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			String currentDate = date.format(formatter);
			Map<String, Object> payout = new LinkedHashMap<>();
			payout.put("autopayoutRef", currentDate + "Guru" + payoutCount);
			property.writeToProperties("payoutRefCount", ++payoutCount + "", UtilitiesPath.PROPERTIES_PATH);
			payout.put("amount", amt);
			payout.put("payout_mode", mode);
			payout.put("transaction_note", "LiveTesting");

			Map<String, Object> payee = new LinkedHashMap<>();
			payee.put("user_ref", "test-user");
			payee.put("company_name", "guru softwares");
			payee.put("user_name", "guruprasad");
			payee.put("user_mobile_number", "8970486528");

			// Add payee to payout
			payout.put("payee", payee);

			Map<String, Object> beneficiary = new LinkedHashMap<>();
			if(mode.equals("UPI")) {
				beneficiary.put("vpa", "Guruprasad");
			}else {
				beneficiary.put("account_name", "Guruprasad");
				beneficiary.put("account_no", "4449682902");
				beneficiary.put("ifsc", "KKBK0008061");
			}

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
			String signature = SignatureGenerator.generateRSASignature(convertToJson(escrowData), privateKey);
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

			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return response;
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
