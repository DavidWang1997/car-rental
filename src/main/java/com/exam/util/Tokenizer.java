package com.exam.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类
 *
 * @author wangpeng
 */
public class Tokenizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Tokenizer.class);
    /**
     * 秘钥
     */
    private static final byte[] SECRET = "6k878d9871224kale99360df45rhj28f".getBytes();

    /**
     * 初始化head部分的数据为
     * {
     * "alg":"HS256",
     * "type":"JWT"
     * }
     */
    private static final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, null, null);

    /**
     * 生成token，该方法只在用户登录成功后调用
     *
     * @param /Map集合，可以存储用户id，token生成时间，token过期时间等自定义字段
     * @return token字符串, 若失败则返回null
     */
    public static String createToken(Map<String, Object> payload) {
        String tokenString = null;
        // 创建一个 JWS object
        JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payload)));
        try {
            // 将jwsObject 进行HMAC签名
            jwsObject.sign(new MACSigner(SECRET));
            tokenString = jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("签名失败:" + e.getMessage());
            e.printStackTrace();
        }
        return tokenString;
    }

    /**
     * 校验token是否合法，返回Map集合,集合中主要包含 state状态码 鉴权成功后从token中提取的数据
     * 该方法在过滤器中调用，每次请求API时都校验
     *
     * @param token
     * @return Map<String, Object>
     */
    public static Map<String, Object> validToken(String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            JWSVerifier verifier = new MACVerifier(SECRET);

            if (jwsObject.verify(verifier)) {
                JSONObject jsonOBj = payload.toJSONObject();
                // token校验成功（此时没有校验是否过期）
                resultMap.put("state", TokenState.VALID.toString());
                // 若payload包含expiryTime字段，则校验是否过期
                if (jsonOBj.containsKey("expiryTime")) {
                    long extTime = Long.valueOf(jsonOBj.get("expiryTime").toString());
                    long curTime = new Date().getTime();
                    // 过期了
                    if (curTime > extTime) {
                        resultMap.clear();
                        resultMap.put("state", TokenState.EXPIRED.toString());
                    }
                }
                resultMap.put("data", jsonOBj);

            } else {
                // 校验失败
                resultMap.put("state", TokenState.INVALID.toString());
            }

        } catch (Exception e) {
            LOGGER.error("校验出错", e);
            resultMap.clear();
            resultMap.put("state", TokenState.INVALID.toString());
        }
        return resultMap;
    }
}


