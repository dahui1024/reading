-- releae lock
local key = KEYS[1]
local res = redis.call('del', key, 1)
if res == 1 then
return 0
end
return -1