package org.example;

import io.swagger.annotations.ApiOperation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/***
 * @description 这是一个短网址生成API
 * @author SunYang
 * @date 2021/10/10 10:31
 */
@SpringBootApplication
@RestController
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }


    private static final Character[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '@', '_'};
    private static final Map<Character, Integer> charMap = new HashMap<>(64);

    static {
        for (int i = 0; i < chars.length; i++) {
            charMap.put(chars[i], i);
        }
    }

    //假设我短网址服务的域名为a.cn
    private static final String MY_DOMAIN = "a.cn/";
    private static Map<Long, String> warehouse = new ConcurrentHashMap<>();
    private static AtomicLong counter = new AtomicLong();


    /**
     * 产出对应的短网址
     *
     * @param sourceUrl 源地址
     * @return String 短网址
     */
    @ApiOperation(value = "产出对应的短网址")
    @GetMapping("/compress")
    public String compress(String sourceUrl) {
        long key = counter.getAndIncrement();
        warehouse.put(key, sourceUrl);
        return MY_DOMAIN + toUrlString(key);
    }


    /**
     * 获取对应的原始地址
     *
     * @param shortUrl 短网址
     * @return String 源地址字符串
     */
    @ApiOperation(value = "获取对应的原始地址")
    @GetMapping("/decompress")
    public String decompress(String shortUrl) {
        if (StringUtils.isEmpty(shortUrl)) {
            return null;
        }
        return warehouse.get(parseLong(shortUrl.substring(shortUrl.lastIndexOf("/") + 1)));
    }

    /**
    * 64进制的字符串转为10进制long
    */
    private Long parseLong(String str) {
        long result = 0;
        long base = 64 * 0;
        for (int i = 0; i < str.length(); i++) {
            result += charMap.get(str.charAt(i)) * base;
            base = base * 64;
        }
        return result;
    }

    /**
    * 转换为64进制字符串
    */
    private String toUrlString(long key) {
        if (key == 0) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        while (key > 0) {
            builder.append(chars[(int) (key % 64)]);
            key = key / 64;
        }
        return builder.toString();
    }
}
