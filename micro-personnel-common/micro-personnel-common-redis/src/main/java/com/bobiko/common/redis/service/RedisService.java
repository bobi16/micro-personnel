package com.bobiko.common.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: bobiko
 * @CreateTime: 2025-03-27
 * @Description: Redis数据操作工具类
 */
@Component
public class RedisService
{
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOps;
    private final HashOperations<String, String, Object> hashOps;
    private final ListOperations<String, Object> listOps;
    private final SetOperations<String, Object> setOps;
    private final ZSetOperations<String, Object> zSetOps;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOps = redisTemplate.opsForValue();
        this.hashOps = redisTemplate.opsForHash();
        this.listOps = redisTemplate.opsForList();
        this.setOps = redisTemplate.opsForSet();
        this.zSetOps = redisTemplate.opsForZSet();
    }

    // ============================== 通用操作 ==============================

    /**
     * 设置过期时间
     * @param key 键
     * @param timeout 时间
     * @param unit 时间单位
     * @return 是否成功
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        try {
            if (timeout > 0) {
                return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, unit));
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取过期时间
     * @param key 键
     * @return 剩余时间(秒) -2表示key不存在，-1表示永久有效
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? -2 : expire;
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除key
     * @param keys 可以传一个或多个key
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(Arrays.asList(keys));
            }
        }
    }

    /**
     * 获取key的类型
     * @param key 键
     * @return 类型
     */
    public DataType type(String key) {
        return redisTemplate.type(key);
    }

    // ============================== String操作 ==============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : valueOps.get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            valueOps.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param timeout 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false失败
     */
    public boolean set(String key, Object value, long timeout) {
        try {
            if (timeout > 0) {
                valueOps.set(key, value, timeout, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return 增加后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        Long increment = valueOps.increment(key, delta);
        return increment == null ? 0 : increment;
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return 减少后的值
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        Long decrement = valueOps.decrement(key, delta);
        return decrement == null ? 0 : decrement;
    }

    // ============================== Hash操作 ==============================

    /**
     * HashGet
     * @param key 键
     * @param item 项
     * @return 值
     */
    public Object hget(String key, String item) {
        return hashOps.get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<String, Object> hmget(String key) {
        return hashOps.entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            hashOps.putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map 对应多个键值
     * @param timeout 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long timeout) {
        try {
            hashOps.putAll(key, map);
            if (timeout > 0) {
                expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            hashOps.put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建并设置时间
     * @param key 键
     * @param item 项
     * @param value 值
     * @param timeout 时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true成功 false失败
     */
    public boolean hset(String key, String item, Object value, long timeout) {
        try {
            hashOps.put(key, item, value);
            if (timeout > 0) {
                expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键
     * @param items 项 可以是多个 不能为null
     */
    public void hdel(String key, Object... items) {
        hashOps.delete(key, items);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键
     * @param item 项
     * @return true存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return hashOps.hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key 键
     * @param item 项
     * @param by 要增加几(大于0)
     * @return 增加后的值
     */
    public double hincr(String key, String item, double by) {
        return hashOps.increment(key, item, by);
    }

    /**
     * hash递减
     * @param key 键
     * @param item 项
     * @param by 要减少几(小于0)
     * @return 减少后的值
     */
    public double hdecr(String key, String item, double by) {
        return hashOps.increment(key, item, -by);
    }

    // ============================== Set操作 ==============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return Set集合
     */
    public Set<Object> sGet(String key) {
        try {
            return setOps.members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(setOps.isMember(key, value));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            Long count = setOps.add(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将set数据放入缓存并设置时间
     * @param key 键
     * @param timeout 时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, long timeout, Object... values) {
        try {
            Long count = setOps.add(key, values);
            if (timeout > 0) {
                expire(key, timeout, TimeUnit.SECONDS);
            }
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return set大小
     */
    public long sSize(String key) {
        try {
            Long size = setOps.size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
            Long count = setOps.remove(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================== List操作 ==============================

    /**
     * 获取list缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1代表所有值
     * @return 列表
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return listOps.range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return 长度
     */
    public long lSize(String key) {
        try {
            Long size = listOps.size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return 值
     */
    public Object lGetIndex(String key, long index) {
        try {
            return listOps.index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param value 值
     * @return 是否成功
     */
    public boolean lSet(String key, Object value) {
        try {
            listOps.rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置时间
     * @param key 键
     * @param value 值
     * @param timeout 时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, Object value, long timeout) {
        try {
            listOps.rightPush(key, value);
            if (timeout > 0) {
                expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     * @param key 键
     * @param values 值
     * @return 是否成功
     */
    public boolean lSet(String key, List<Object> values) {
        try {
            listOps.rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存并设置时间
     * @param key 键
     * @param values 值
     * @param timeout 时间(秒)
     * @return 是否成功
     */
    public boolean lSet(String key, List<Object> values, long timeout) {
        try {
            listOps.rightPushAll(key, values);
            if (timeout > 0) {
                expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index 索引
     * @param value 值
     * @return 是否成功
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            listOps.set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value的项
     * @param key 键
     * @param count 移除数量
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = listOps.remove(key, count, value);
            return remove == null ? 0 : remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================== ZSet操作 ==============================

    /**
     * 添加元素到有序集合
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return 是否成功
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return Boolean.TRUE.equals(zSetOps.add(key, value, score));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取有序集合的元素数量
     * @param key 键
     * @return 数量
     */
    public long zCard(String key) {
        try {
            Long size = zSetOps.size(key);
            return size == null ? 0 : size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取有序集合中指定分数区间的元素数量
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 数量
     */
    public long zCount(String key, double min, double max) {
        try {
            Long count = zSetOps.count(key, min, max);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 增加元素的分数
     * @param key 键
     * @param value 值
     * @param delta 增加的分数
     * @return 增加后的分数
     */
    public double zIncrBy(String key, Object value, double delta) {
        try {
            Double score = zSetOps.incrementScore(key, value, delta);
            return score == null ? 0 : score;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取有序集合中指定区间的元素(按分数从小到大)
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return 元素集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return zSetOps.range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取有序集合中指定分数区间的元素(按分数从小到大)
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 元素集合
     */
    public Set<Object> zRangeByScore(String key, double min, double max) {
        try {
            return zSetOps.rangeByScore(key, min, max);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取元素在有序集合中的排名(按分数从小到大)
     * @param key 键
     * @param value 值
     * @return 排名(从0开始)
     */
    public Long zRank(String key, Object value) {
        try {
            return zSetOps.rank(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从有序集合中移除元素
     * @param key 键
     * @param values 值
     * @return 移除的数量
     */
    public long zRemove(String key, Object... values) {
        try {
            Long count = zSetOps.remove(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除有序集合中指定排名区间的元素
     * @param key 键
     * @param start 开始排名
     * @param end 结束排名
     * @return 移除的数量
     */
    public long zRemoveRange(String key, long start, long end) {
        try {
            Long count = zSetOps.removeRange(key, start, end);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 移除有序集合中指定分数区间的元素
     * @param key 键
     * @param min 最小分数
     * @param max 最大分数
     * @return 移除的数量
     */
    public long zRemoveRangeByScore(String key, double min, double max) {
        try {
            Long count = zSetOps.removeRangeByScore(key, min, max);
            return count == null ? 0 : count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
