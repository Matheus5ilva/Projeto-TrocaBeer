package br.com.trocabeer.core.utils;

import java.math.BigDecimal;
import java.util.Date;

public class NullUtils {

	public static BigDecimal bigDecimalNotNull(BigDecimal numero) {
		return numero != null ? numero : BigDecimal.ZERO;
	}
	
	public static String stringNotNull(String texto) {
		return texto != null ? texto : "";
	}
	
	public static Date dataNotNull(Date data) {
		return data != null ? data : new Date(0);
	}
	
}
