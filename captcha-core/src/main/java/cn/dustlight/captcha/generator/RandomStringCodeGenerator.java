package cn.dustlight.captcha.generator;

import cn.dustlight.captcha.core.Code;
import cn.dustlight.captcha.core.DefaultCode;

import java.security.SecureRandom;
import java.util.Map;

/**
 * 随机字符串生成器
 */
public class RandomStringCodeGenerator implements CodeGenerator<String> {

    public static final char[] DEFAULT_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm".toCharArray();

    private SecureRandom secureRandom;
    private char[] chars;
    private int length;

    public RandomStringCodeGenerator(char[] chars, int length) {
        secureRandom = new SecureRandom();
        this.chars = chars;
        this.length = length;
    }

    public RandomStringCodeGenerator() {
        this(DEFAULT_CHARACTERS, 4);
    }

    public Code<String> generate(String name, Map<String, Object> parameters) throws GenerateCodeException {
        try {
            int len = length(parameters);
            char[] chars = chars(parameters);
            StringBuilder builder = new StringBuilder(length);
            for (int i = 0; i < len; i++)
                builder.append(chars[secureRandom.nextInt(chars.length)]);
            DefaultCode<String> code = new DefaultCode<>(builder.toString());
            code.setName(name);
            return code;
        } catch (Exception e) {
            throw new GenerateCodeException("Fail to generate code", e);
        }
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public char[] getChars() {
        return chars;
    }

    public int getLength() {
        return length;
    }

    private char[] chars(Map<String, Object> parameters) {
        if (parameters == null ||
                !parameters.containsKey("CODE_CHARS") ||
                parameters.get("CODE_CHARS") == null)
            return this.chars;
        return parameters.get("CODE_CHARS").toString().toCharArray();
    }

    private int length(Map<String, Object> parameters) {
        if (parameters == null ||
                !parameters.containsKey("CODE_LENGTH") ||
                parameters.get("CODE_LENGTH") == null)
            return this.length;
        return Integer.parseInt(parameters.get("CODE_LENGTH").toString());
    }
}
