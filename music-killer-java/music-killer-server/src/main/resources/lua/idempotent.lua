-- 幂等性检查Lua脚本
-- 使用Redis的SET NX EX原子操作实现高性能幂等检查

local key = KEYS[1]                    -- 幂等性key
local expireTime = tonumber(ARGV[1])   -- 过期时间(秒)

-- 尝试设置key,NX表示key不存在时才设置,EX设置过期时间
-- 返回值: 1表示设置成功(首次请求), 0表示key已存在(重复请求)
local result = redis.call('SET', key, '1', 'NX', 'EX', expireTime)

if result then
    return 1  -- 允许执行
else
    return 0  -- 重复请求,拒绝执行
end
