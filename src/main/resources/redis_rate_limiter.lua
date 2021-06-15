--- 资源唯一标识
local key = KEYS[1]
--- 时间窗最大并发数
local max_qps = tonumber(ARGV[1])
--- 时间窗
local window = tonumber(ARGV[2])
--- 时间窗内当前并发数
local current_qps = tonumber(redis.call('get', key) or 0)
if current_qps + 1 > max_qps then
    return false
else
    redis.call("incrby", key,1)
    local t = redis.call('ttl',key)
    if window > -1 and t == -1 then
        redis.call("expire", key,window)
    end
    return true
end