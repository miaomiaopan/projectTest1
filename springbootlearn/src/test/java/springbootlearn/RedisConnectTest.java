package springbootlearn;

import redis.clients.jedis.Jedis;

public class RedisConnectTest {
	public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("211.159.220.105",6379);
        jedis.auth("123456");
        System.out.println(jedis);
        System.out.println(jedis.ping());
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("connecttest", "成功啦");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("connecttest"));
    }
}
