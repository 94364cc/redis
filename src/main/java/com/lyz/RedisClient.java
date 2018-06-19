package com.lyz;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/12.
 */
public class RedisClient {

    private Jedis jedis;
    private JedisPool jedisPool;
    private ShardedJedis shardedJedis;
    private ShardedJedisPool shardedJedisPool;

    public RedisClient(){
        initialPool();
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();
    }
    private void initialPool(){
        //池基本配置
        JedisPoolConfig config=new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(10001);
        config.setTestOnBorrow(false);
        jedisPool=new JedisPool(config,"127.0.0.1",6379);
    }
    public void initialShardedPool(){
        //池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000l);
        config.setTestOnBorrow(false);
        //slave链接
        List<JedisShardInfo> shards=new ArrayList<JedisShardInfo>();
        shards.add(new JedisShardInfo("127.0.0.1",6379,"master"));
        //构造池
        shardedJedisPool=new ShardedJedisPool(config,shards);
    }

    public void show(){
        //KeyOperate();
        //StringOperate();
        //ListOperate();
        //SetOperate();
        //SortedSetOperate();
        HashOperate();
//        jedisPool.returnResource(jedis);
//        shardedJedisPool.returnResource(shardedJedis);
    }
    private void KeyOperate() {
        System.out.println("======================key==========================");
        // 清空数据
        System.out.println("empty the redis DB:"+jedis.flushDB());
        // 判断key否存在
        System.out.println("key999 exist:"+jedis.exists("key999"));
        //新增键值对
        System.out.println("add key/value:"+jedis.set("user","zhangsan"));
        System.out.println(jedis.set("user1","lisi"));
        //exist
        System.out.println("exist:"+jedis.exists("user"));

        //delete the key,if the key is not exist,ignore it
        System.out.println("delete the key:"+jedis.del("user2"));
        //output all the key
        Set<String> keys =jedis.keys("*");
        Iterator<String> iterator=keys.iterator();
        while(iterator.hasNext()){
            String key=iterator.next();
            System.out.println(key);
        }
        // 设置 key001的过期时间
        System.out.println("set the key expired:"+jedis.expire("user",5));
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //query the rest time,if forever or expired then output -1
        System.out.println("query the rest time:"+jedis.ttl("user")+" second");
        System.out.println("remove the resr time:"+jedis.persist("user"));
        System.out.println("query the rest time:"+jedis.ttl("user")+" second");

        System.out.println("query the type of value:"+jedis.type("user"));
    }

    private void StringOperate() {
        System.out.println("======================String_1==========================");
        System.out.println("empty the db:"+jedis.flushDB());
        System.out.println("add new element");
        jedis.set("user01","zhangsan");
        jedis.set("user02","lisi");
        jedis.set("user03","wangwu");
        //output the new element
        System.out.println("delete the element:"+jedis.del("user03"));
        System.out.println("query the del element:"+jedis.exists("user03"));
        //modify the element
        System.out.println("modify the element and it will rewrite the value:"+jedis.set("user02","zcc"));
        System.out.println("query the modify element:"+jedis.get("user02"));

        System.out.println("modify and old value will append the new string:"+jedis.append("user02", " is very good"));
        System.out.println("query the modify element:"+jedis.get("user02"));

        //mutiple operations
        System.out.println("add several elements:"+jedis.mset("key1","value1","key2","value2","key3","value3"));
        System.out.println("get serval elements:"+jedis.mget("key1","key2","key3"));
        System.out.println("del severals elements:"+jedis.del(new String[]{"key1","key2"}));
        System.out.println("get serval elements:"+jedis.mget("key1","key2","key3"));

        //jedis具备的功能shardedJedis中也可直接使用，下面测试一些前面没用过的方法
        System.out.println("empty the db:"+jedis.flushDB());
        System.out.println("add the new element avoid to rewrite the old value");
        System.out.println("add a not exist element:"+shardedJedis.setnx("key1", "value1"));
        System.out.println("add another not exist element:"+shardedJedis.setnx("key2", "value2"));
        System.out.println("add a exist element:"+shardedJedis.setnx("key2", "value2"));
        System.out.println("get the key1 value:"+shardedJedis.get("key1"));
        System.out.println("get the key2 value:"+shardedJedis.get("key2"));

        System.out.println("add a new element and set the timeout=2 second:"+shardedJedis.setex("key3", 2, "value3-2second"));
        System.out.println("get the element:"+shardedJedis.get("key3"));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("get the element:"+shardedJedis.get("key3"));
        System.out.println("get the new value and update it:"+shardedJedis.getSet("key1","value1-after"));
        System.out.println("get the new value:"+shardedJedis.get("key1"));
        System.out.println("get the sublist:"+shardedJedis.getrange("key1",2,3));
    }

    private void ListOperate() {
        System.out.println("======list=======");
        System.out.println("empty the db:"+jedis.flushDB());
        System.out.println("add a list element:"+jedis.lpush("stringlists","vector"));
        shardedJedis.lpush("stringlists", "ArrayList");
        shardedJedis.lpush("stringlists", "vector");
        shardedJedis.lpush("stringlists", "vector");
        shardedJedis.lpush("stringlists", "LinkedList");
        shardedJedis.lpush("stringlists", "MapList");
        shardedJedis.lpush("stringlists", "SerialList");
        shardedJedis.lpush("stringlists", "HashList");
        shardedJedis.lpush("numberlists", "3");
        shardedJedis.lpush("numberlists", "1");
        shardedJedis.lpush("numberlists", "5");
        shardedJedis.lpush("numberlists", "2");

        System.out.println("all the element:"+jedis.lrange("stringlists",0,-1));
        // 删除列表指定的值 ，第二个参数为删除的个数（有重复时），后add进去的值先被删，类似于出栈
        System.out.println("delete the list element:"+jedis.lrem("stringlists",2,"vector"));
        System.out.println("query the element:"+jedis.lrange("stringlists", 0, -1));
        //删除区间以外的数据,包头又包尾
        System.out.println("delete the element except of index of 0-3:"+shardedJedis.ltrim("stringlists", 0, 3));
        System.out.println("query the element:"+jedis.lrange("stringlists", 0, -1));
        //元素出栈,删除并输出list的第一个元素
        System.out.println("pop the element:"+jedis.lpop("stringlists"));
        System.out.println("query the element:"+jedis.lrange("stringlists", 0, -1));
        System.out.println("update the element:"+jedis.lset("stringlists",0,"hello"));
        System.out.println("query the element:"+jedis.lrange("stringlists", 0, -1));
        System.out.println("output the list size:"+jedis.llen("stringlists"));

        //sort the list
        /*
         * list中存字符串时必须指定参数为alpha，如果不使用SortingParams，而是直接使用sort("list")，
         * 会出现"ERR One or more scores can't be converted into double"
         */
        SortingParams sortingParams=new SortingParams();
        sortingParams.alpha();
        sortingParams.limit(0, 3);
        System.out.println("sort list:"+jedis.sort("stringlists",sortingParams));
        System.out.println("sort list:"+jedis.sort("numberlists"));
        System.out.println("get the index value:"+jedis.lindex("stringlists",2));
    }

    private void SetOperate() {
        System.out.println("====set=====");
        System.out.println("empty the db:"+jedis.flushDB());
        System.out.println("add the set elemet:"+jedis.sadd("set","ele1"));
        System.out.println("add the set elemet:"+jedis.sadd("set","ele2"));
        System.out.println("add the set elemet:"+jedis.sadd("set","ele3"));
        System.out.println("query the all element:"+jedis.smembers("set"));
        System.out.println("delete the element:"+jedis.srem("set","ele3"));
        System.out.println("query the all element:"+jedis.smembers("set"));
        //随机出栈
        System.out.println("sets pop:"+jedis.spop("set"));
        System.out.println("query the all element:"+jedis.smembers("set"));
        //无修改方法
        System.out.println("judge the element exist:"+jedis.sismember("set","ele1"));
        System.out.println("add a new set list:"+jedis.sadd("set01","ele1"));
        System.out.println("add a new set list:"+jedis.sadd("set01","ele2"));
        System.out.println("add a new set list:"+jedis.sadd("set01","ele3"));
        System.out.println("add a new set list:"+jedis.sadd("set01","ele4"));
        System.out.println(jedis.smembers("set01"));
        System.out.println(jedis.smembers("set"));
        System.out.println("query the inner join:"+jedis.sinter("set","set01"));
        System.out.println("query the union join:"+jedis.sunion("set","set01"));
        System.out.println("query the diff join:"+jedis.sdiff("set01","set"));


    }

    private void SortedSetOperate() {
        System.out.println("=====zset======");
        System.out.println(jedis.flushDB());
        System.out.println("======add======");
        System.out.println("zset add element001:"+jedis.zadd("zset",7.0,"element001"));
        System.out.println("zset add element002:"+jedis.zadd("zset",8.0,"element002"));
        System.out.println("zset add element003:"+jedis.zadd("zset",2.0,"element003"));
        System.out.println("zset add element004:"+jedis.zadd("zset",3.0,"element004"));
        System.out.println("query the zset:"+jedis.zrange("zset", 0, -1));

        System.out.println("====remove=====");
        System.out.println("zset remove element002:"+jedis.zrem("zset","element002"));
        System.out.println("query the zset:"+jedis.zrange("zset",0,-1));

        System.out.println("======query=======");
        System.out.println("count the element:"+jedis.zcard("zset"));
        System.out.println("count the element weight in 1.0-5.0:"+jedis.zcount("zset",1.0,5.0));
        System.out.println("query the zset element weight of element004:"+jedis.zscore("zset","element004"));
        //index=1 count=2
        System.out.println("query the key in 1-2:"+jedis.zrange("zset",1,2));
    }

    private void HashOperate() {
        System.out.println("======hash=========");
        System.out.println("empty the db"+jedis.flushDB());
        System.out.println("====add====");
        System.out.println("hash add element:"+jedis.hset("hashs","key01","value01"));
        System.out.println("hash add element:"+jedis.hset("hashs","key02","value02"));
        System.out.println("hash add element:"+jedis.hset("hashs","key03","value03"));
        System.out.println("add key04 and 4 key-value:"+jedis.hincrBy("hashs","key004",41));
        System.out.println("hashs list:"+jedis.hvals("hashs"));

        System.out.println("=====remove=======");
        System.out.println("remove the element:"+jedis.hdel("hashs","key02"));
        System.out.println("remove the element:"+jedis.hvals("hashs"));

        System.out.println("query the element exists:"+jedis.hexists("hashs","key01"));
        System.out.println("get the element:"+jedis.hget("hashs","key01"));
        System.out.println("get the mutiply element:"+jedis.hmget("hashs","key01","key03"));
        System.out.println("get the all key:"+jedis.hkeys("hashs"));
        System.out.println("get the all value:"+jedis.hvals("hashs"));

    }

    public static void main(String[] args) {
        new RedisClient().show();
    }
}
