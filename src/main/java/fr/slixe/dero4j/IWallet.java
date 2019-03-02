package fr.slixe.dero4j;

import fr.slixe.dero4j.structure.Tx;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IWallet
{
	/**
	 *
	 * @param id which represent paymentId
	 * @return new integrated address
	 * @throws WalletException
	 */
	String generateAddress(String id) throws WalletException;
	/**
	 *
	 * @param address (integrated address)
	 * @return paymentId in this integrated address
	 * @throws WalletException
	 */
	String getPaymentIdFromAddress(String address) throws WalletException;
	/**
	 *
	 * @param address
	 * @param amount amount to sent
	 * @return
	 * @throws WalletException
	 */
	String sendTo(String address, BigDecimal amount) throws WalletException;
	/**
	 *
	 * @param paymentId
	 * @param minHeight
	 * @return list of incoming transactions
	 * @throws WalletException
	 */
	List<Tx> getTransactions(String paymentId, int minHeight) throws WalletException;
	/**
	 * @return the primary address of this wallet
	 * @throws WalletException
	 */
	String getPrimaryAddress() throws WalletException;
	/**
	 * @return current block height of wallet
	 * @throws WalletException
	 */
	int getHeight() throws WalletException;

	/**
	 *
	 * @param scid smart contract id
	 * @param entrypoint function in SC
	 * @param params can be null
	 * @param amount to send to scid
	 * @return tx hash
	 * @throws WalletException
	 */
	String sendToSC(String scid, String entrypoint, Map<String, Object> params, BigDecimal amount) throws WalletException;
}
