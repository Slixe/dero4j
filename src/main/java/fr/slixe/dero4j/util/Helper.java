package fr.slixe.dero4j.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class Helper
{
	public static BigInteger asUint64(BigDecimal value, int scale)
	{
		return value.scaleByPowerOfTen(scale).toBigIntegerExact();
	}

	public static BigDecimal toBigDecimal(BigInteger value, int scale)
	{
		BigDecimal bigDecimal = new BigDecimal(Math.pow(10, scale));
		return new BigDecimal(value).divide(bigDecimal).setScale(scale, RoundingMode.HALF_DOWN);
	}
}
