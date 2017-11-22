-- apply for lock
local key = KEYS[1]
local res = redis.call('exists', key)

-- 锁被占用，申请失败
if res == 1 then
return -1
-- 锁可以被申请
else
local setres = redis.call('set', key, 0)
if setres['ok'] == 'OK' then
return 0
end
end
return -1