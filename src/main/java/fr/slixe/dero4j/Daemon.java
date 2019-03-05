package fr.slixe.dero4j;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import static fr.slixe.dero4j.util.Helper.json;

public class Daemon
{
	private final String host;

	public Daemon(String host, int port)
	{
		this.host = String.format("http://%s:%d", host, port);
	}

	private JSONObject post(JSONObject json) throws RequestException
	{
		HttpResponse<JsonNode> httpResponse;
		try {
			httpResponse = Unirest.post(host + "/json_rpc").body(json).asJson();
		} catch (UnirestException e) {
			throw new RequestException("Daemon isn't available");
		}
		JSONObject response = httpResponse.getBody().getObject();
		if (!response.has("result")) {
			throw new RequestException(response.getString("error"));
		}
		return response.getJSONObject("result");
	}

	private JSONObject get(String path) throws RequestException
	{
		HttpResponse<JsonNode> httpResponse;
		try {
			httpResponse = Unirest.get(host + path).asJson();
		} catch (UnirestException e) {
			throw new RequestException("Daemon isn't available");
		}
		JSONObject response = httpResponse.getBody().getObject();
		if (!response.has("result")) {
			throw new RequestException(response.getString("error"));
		}
		return response.getJSONObject("result");
	}

	public int getHeight() throws RequestException
	{
		return get("/getheight").getInt("height");
	}

	public String getHost()
	{
		return this.host;
	}
}
