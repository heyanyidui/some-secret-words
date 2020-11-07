package cn.heyanyidui;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class SecretWords {
    private static String KEY = "1230123456456789";

    private static String IV = "1230123456456789";

    public static void main(String[] args) {
        try {
            // 加密
            String strBefore = "小傻子";
            String aesStr = encrypt(strBefore);
            System.out.println(aesStr);

            // 解密
            String str = "RP+uIRITbQjnvAB5ykEFew==";
            String desStr = desEncryptReplace(str);
            System.out.print(desStr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密方法
     *
     * @param data 要加密的数据
     * @param key  加密key
     * @param iv   加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        try {

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"NoPadding PkcsPadding
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes(Charset.forName("UTF-8"));
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes(Charset.forName("UTF-8")));

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new Base64().encodeToString(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用默认的key和iv加密
     *
     * @param data
     * @return
     */
    public static String encrypt(String data) throws Exception {
        return encrypt(data, KEY, IV);
    }

    /**
     * 解密方法
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv   解密iv
     * @return 解密的结果
     * @throws Exception
     */
    public static String desEncrypt(String data, String key, String iv) throws Exception {
        try {
            byte[] encrypted1 = new Base64().decode(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(Charset.forName("UTF-8")), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes(Charset.forName("UTF-8")));

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, StandardCharsets.UTF_8);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param data
     * @return
     */
    public static String desEncrypt(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return desEncrypt(data, KEY, IV);
    }

    public static String desEncryptReplace(String data) throws Exception {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return desEncrypt(data, KEY, IV).replaceAll("\u0000", "");
    }
}
