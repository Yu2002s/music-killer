-- 限流 Lua 脚本
-- KEYS[1]: 限流key
-- ARGV[1]: 限流阈值（最大请求次数）
-- ARGV[2]: 时间窗口（秒）

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local window = tonumber(ARGV[2])

-- 获取当前请求次数
local current = redis.call('get', key)

if current and tonumber(current) >= limit then
    -- 超过限流阈值，返回 0
    return 0
else
    -- 未超过限流阈值
    -- 增加请求次数
    current = redis.call('incr', key)

    -- 如果是第一次请求，设置过期时间
    if tonumber(current) == 1 then
        redis.call('expire', key, window)
    end

    -- 返回 1 表示允许请求
    return 1
end
