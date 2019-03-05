package fr.slixe.dero4j;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import fr.slixe.dero4j.structure.Tx;
import fr.slixe.dero4j.util.Helper;
import fr.slixe.dero4j.util.MapBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;

import static fr.slixe.dero4j.util.Helper.asUint64;
import static fr.slixe.dero4j.util.Helper.json;

public class DeroWallet implements IWallet
{
	private static final int SCALE = 12;

	private final String host;
	private final String username;
	private final String password;

	public DeroWallet(String host, int port, String username, String password)
	{
		this.host = String.format("http://%s:%d", host, port);
		this.username = username;
		this.password = password;
	}

	private JSONObject request(JSONObject json) throws RequestException
	{
		HttpResponse<JsonNode> req;
		try {
			req = Unirest.post(host).basicAuth(username, password).header("Content-Type", "application/json").body(json).asJson();
		} catch (UnirestException e) {
			throw new RequestException("Wallet is offline?");
		}
		System.out.println("Headers:" + req.getHeaders().toString());
		System.out.println("Request: " + json.toString());
		System.out.println("Response: " + req.getBody());

		JSONObject response = req.getBody().getObject();
		if (!response.has("result")) {
			throw new RequestException(response.getString("error"));
		}
		return response.getJSONObject("result");
	}

	@Override
	public String generateAddress(String id) throws RequestException
	{
		JSONObject json = request(json("make_integrated_address", new MapBuilder<String, Object>().put("payment_id", id).get()));
		return json.getString("integrated_address"); //make_integrated_address
	}

	@Override
	public String getPaymentIdFromAddress(String address) throws RequestException
	{
		JSONObject json = request(json("split_integrated_address", new MapBuilder<String, Object>().put("integrated_address", address).get()));
		return json.getString("payment_id"); //split_integrated_address
	}

	@Override
	public String sendTo(String address, BigDecimal amount) throws RequestException
	{
		JSONObject json = request(json("transfer", new MapBuilder<String, Object>().put("address", address).put("amount", Helper.asUint64(amount, SCALE)).get()));
		return json.getString("tx_hash");
	}

	@Override
	public List<Tx> getTransactions(String id, int minHeight) throws RequestException
	{
		List<Tx> list = new ArrayList<>();
		JSONObject json = request(json("get_bulk_payments", new MapBuilder<String, Object>().put("payment_ids", new String[]{id}).get()));
		if (!json.has("payments")) return list;

		JSONArray array = json.getJSONArray("payments");
		Iterator<Object> it = array.iterator();
		while (it.hasNext()) {
			JSONObject j = (JSONObject) it.next();
			int blockHeight = j.getInt("block_height");
			String txHash = j.getString("tx_hash");
			BigDecimal amount = Helper.toBigDecimal(j.getBigInteger("amount"), SCALE);
			byte lockedTime = (byte) j.getInt("unlock_time");
			list.add(new Tx.In(blockHeight, txHash, amount, lockedTime, null)); //fromAddress is null because it's isn't returned in json response
		}
		return list;
	}

	@Override
	public String getPrimaryAddress() throws RequestException
	{
		return request(json("getaddress")).getString("address"); //getaddress
	}

	@Override
	public int getHeight() throws RequestException
	{
		return request(json("getheight")).getInt("height"); //getheight
	}

	@Override
	public String sendToSC(String scid, String entrypoint, Map<String, Object> scParams, BigDecimal amount) throws RequestException
	{
		LinkedHashMap<String, Object> params = new MapBuilder<String, Object>().put("scid", scid).put("entrypoint", entrypoint).put("get_tx_key", true).put("value", asUint64(amount, 12)).get();
		if (scParams != null)
			params.putAll(scParams);
		return request(json("transfer_split", params)).toString(); //TODO
	}

	@Override
	public String paymentId()
	{
		StringBuilder builder = new StringBuilder(32);
		SecureRandom rnd = new SecureRandom();
		while (builder.length() < 32)
			builder.append(Integer.toHexString(rnd.nextInt()));
		return builder.substring(0, 32);
	}
}
