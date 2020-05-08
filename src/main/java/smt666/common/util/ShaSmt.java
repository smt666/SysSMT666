package smt666.common.util;

import smt666.common.util.ex.EncryptionException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class ShaSmt {

	/**将byte数组，转换成16进制字符串输出。
	 * 
	 * @param bytes
	 * @return
	 */
	public static String converByteToHexString(byte[] bytes){
	    String result="";
	    for(int i=0;i<bytes.length;i++){
	        int temp=bytes[i] & 0xff;
	        String tempHex=Integer.toHexString(temp);
	        if(tempHex.length()<2){
	            result += "0"+tempHex;
	        }else{
	            result += tempHex;
	        }
	    }
		return result;
	}
	
	/**
	 * SHA-1  数据加密。varchar(41)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：40位 。
	 */
	public static String jdkSHA1(String message) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA");
			byte[] sha1Encode = sha1.digest(message.getBytes());
			return converByteToHexString(sha1Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-1加密数据失败！");
		}
	}
	
	/**
	 * SHA-1  【加盐加密】。varchar(41)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @param salt 【盐】。String类型（第2把密钥）。 
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：40位 。
	 */
	public static String jdkSHA1(String message, String salt) {
		String data = message  + salt;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA");
			byte[] sha1Encode = sha1.digest(data.getBytes());
			return converByteToHexString(sha1Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-1加密数据失败！");
		}
	}
	
	/**
	 * SHA-256  数据加密。varchar(63)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：62位 。
	 */
	public static String jdkSHA256(String message) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] sha256Encode = sha256.digest(message.getBytes());
			return converByteToHexString(sha256Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-256加密数据失败！");
		}
	}
	
	/**
	 * SHA-256【加盐加密】。varchar(63)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @param salt 【盐】。String类型（第2把密钥）。
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：62位 。
	 */
	public static String jdkSHA256(String message, String salt) {
		String data = message + salt;
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] sha256Encode = sha256.digest(data.getBytes());
			return converByteToHexString(sha256Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-256加密数据失败！");
		}
	}
	
	/**
	 * SHA-384  数据加密。varchar(88)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：87位 。
	 */
	public static String jdksha384(String message) {
		try {
			MessageDigest sha384Digest=MessageDigest.getInstance("SHA-384");
			byte[] sha384Encode=sha384Digest.digest(message.getBytes());
			return converByteToHexString(sha384Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-384加密数据失败！");
		}
	}
	
	/**
	 * SHA-384【加盐加密】。 varchar(88)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @param salt 【盐】。String类型（第2把密钥）。
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：87位 。
	 */
	public static String jdksha384(String message, String salt) {
		String data = message + salt;
		try {
			MessageDigest sha384Digest=MessageDigest.getInstance("SHA-384");
			byte[] sha384Encode=sha384Digest.digest(data.getBytes());
			return converByteToHexString(sha384Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-384加密数据失败！");
		}
	}
	
	/**
	 * SHA-512  数据加密。varchar(129)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：128位 。
	 */
	public static String jdksha512(String message) {
		try {
			MessageDigest sha512Digest=MessageDigest.getInstance("SHA-512");
			byte[] sha512Encode=sha512Digest.digest(message.getBytes());
			return converByteToHexString(sha512Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-512加密数据失败！");
		}
	}
	
	/**
	 * SHA-512【加盐加密】。 varchar(129)
	 * @param message 【明文】需要加密的数据（String类型）
	 * @param salt 【盐】。String类型（第2把密钥）。
	 * @return 【加密后生成的指纹】返回加密之后的（String类型的）数据：128位 。
	 */
	public static String jdksha512(String message, String salt) {
		try {
			MessageDigest sha512Digest=MessageDigest.getInstance("SHA-512");
			byte[] sha512Encode=sha512Digest.digest(message.getBytes());
			return converByteToHexString(sha512Encode);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new EncryptionException("使用SHA-512加密数据失败！");
		}
	}
	
}
