package fr.slixe.dero4j;

import fr.slixe.dero4j.structure.Tx;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IWallet
{
	/**
	 * generate a new integrated address with the payment id
	 * @param id which represent paymentId
	 * @return new integrated address
	 * @throws RequestException
	 */
	String generateAddress(String id) throws RequestException;
	/**
	 * @param address (integrated address)
	 * @return paymentId in this integrated address
	 * @throws RequestException
	 */
	String getPaymentIdFromAddress(String address) throws RequestException;
	/**
	 * @param address is the receiver address
	 * @param amount amount to sent
	 * @return
	 * @throws RequestException
	 */
	String sendTo(String address, BigDecimal amount) throws RequestException;
	/**
	 * @param paymentId
	 * @param minHeight minimum height to start to count transactions
	 * @return list of incoming transactions
	 * @throws RequestException
	 */
	List<Tx> getTransactions(String paymentId, int minHeight) throws RequestException;
	/**
	 * @return the primary address of this wallet
	 * @throws RequestException
	 */
	String getPrimaryAddress() throws RequestException;
	/**
	 * @return current block height of wallet
	 * @throws RequestException
	 */
	int getHeight() throws RequestException;
	/**
	 * @param scid smart contract id
	 * @param entrypoint function in SC
	 * @param scParams are the parameters used by smart contract. Map can be null
	 * @param amount to send to scid
	 * @return tx hash
	 * @throws RequestException
	 */
	String sendToSC(String scid, String entrypoint, Map<String, Object> scParams, BigDecimal amount) throws RequestException;
	/**
	 * Generate a new 64 hex bytes paymentId
	 * @return a new generated paymentId
	 */
	String paymentId();
}
