package fr.slixe.dero4j.structure;

import fr.slixe.dero4j.Daemon;

public class SmartContract
{
	private final Daemon daemon;
	private final String scid;

	public SmartContract(Daemon daemon, String scid)
	{
		this.daemon = daemon;
		this.scid = scid;
	}

	public String getData(String key)
	{
		return null; //TODO
	}

	public String getSCID()
	{
		return this.scid;
	}
}
