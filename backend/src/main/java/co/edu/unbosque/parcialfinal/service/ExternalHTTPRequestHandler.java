package co.edu.unbosque.parcialfinal.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ExternalHTTPRequestHandler {

	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(10)).build();

	private static final Gson GSON = new GsonBuilder().create();

	private ExternalHTTPRequestHandler() {
	}

	public static HttpResponse<String> send(String method, String url, String json, Map<String, String> headers) {
		HttpRequest.BodyPublisher body = json == null ? HttpRequest.BodyPublishers.noBody()
				: HttpRequest.BodyPublishers.ofString(json);
		HttpRequest.Builder builder = HttpRequest.newBuilder().uri(URI.create(url)).timeout(Duration.ofSeconds(15))
				.header("Content-Type", "application/json").header("Accept", "application/json")
				.method(method, body);
		if (headers != null) {
			headers.forEach(builder::setHeader);
		}
		try {
			return HTTP_CLIENT.send(builder.build(), HttpResponse.BodyHandlers.ofString());
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return null;
		}
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doGet(String url, Map<String, String> headers) {
		HttpResponse<String> response = send("GET", url, null, headers);
		return response == null ? null : response.body();
	}

	public static <T> T doGetAndConvertToDto(String url, Class<T> type) {
		return doGetAndConvertToDto(url, type, null);
	}

	public static <T> T doGetAndConvertToDto(String url, Class<T> type, Map<String, String> headers) {
		String json = doGet(url, headers);
		if (json == null || json.isBlank()) {
			return null;
		}
		return GSON.fromJson(json, type);
	}

	public static <T> List<T> doGetAndConvertToDtoList(String url, Class<T[]> type) {
		return doGetAndConvertToDtoList(url, type, null);
	}

	public static <T> List<T> doGetAndConvertToDtoList(String url, Class<T[]> type, Map<String, String> headers) {
		String json = doGet(url, headers);
		if (json == null || json.isBlank()) {
			return new ArrayList<>();
		}
		JsonElement element = JsonParser.parseString(json);
		if (element.isJsonArray()) {
			return new ArrayList<>(Arrays.asList(GSON.fromJson(element, type)));
		}
		return new ArrayList<>(Arrays.asList(GSON.fromJson("[" + json + "]", type)));
	}

	public static String doPost(String url, String json) {
		return doPost(url, json, null);
	}

	public static String doPost(String url, String json, Map<String, String> headers) {
		HttpResponse<String> response = send("POST", url, json, headers);
		return response == null ? null : response.body();
	}

	public static String doPut(String url, String json) {
		HttpResponse<String> response = send("PUT", url, json, null);
		return response == null ? null : response.body();
	}

	public static String doDelete(String url) {
		HttpResponse<String> response = send("DELETE", url, null, null);
		return response == null ? null : response.body();
	}

	public static String toJson(Object object) {
		return GSON.toJson(object);
	}

	public static String prettyPrintUsingGson(String uglyJson) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(JsonParser.parseString(uglyJson));
	}

}
