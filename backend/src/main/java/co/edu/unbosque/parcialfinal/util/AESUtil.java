package co.edu.unbosque.parcialfinal.util;

import static org.apache.commons.codec.binary.Base64.decodeBase64;
import static org.apache.commons.codec.binary.Base64.encodeBase64;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

public class AESUtil {

	private static final String ALGORITMO = "AES";
	private static final String TIPOCIFRADO = "AES/GCM/NoPadding";

	private AESUtil() {
	}

	public static String encrypt(String llave, String iv, String texto) {
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(StandardCharsets.UTF_8), ALGORITMO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8));
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
			byte[] encrypted = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));
			return new String(encodeBase64(encrypted), StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			return "";
		}
	}

	public static String decrypt(String llave, String iv, String encrypted) {
		try {
			Cipher cipher = Cipher.getInstance(TIPOCIFRADO);
			SecretKeySpec secretKeySpec = new SecretKeySpec(llave.getBytes(StandardCharsets.UTF_8), ALGORITMO);
			GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv.getBytes(StandardCharsets.UTF_8));
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
			byte[] decrypted = cipher.doFinal(decodeBase64(encrypted));
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (GeneralSecurityException e) {
			return "";
		}
	}

	public static String hashingToSHA256(String content) {
		return DigestUtils.sha256Hex(content);
	}

	public static String hashingToSHA512(String content) {
		return DigestUtils.sha512Hex(content);
	}

	public static String hashingToMD5(String content) {
		return DigestUtils.md5Hex(content);
	}

}
