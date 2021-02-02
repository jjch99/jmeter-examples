# 工具

## ab

http://httpd.apache.org/docs/current/programs/ab.html

```bash
ab -n 1000000 -c 200 -k URL
```

## wrk

https://github.com/wg/wrk

```bash
wrk -t8 -c200 -d10m --latency URL
```

## http_load

http://acme.com/software/http_load/

根据url列表进行测试：可用于根据线上access_log提取url，到线下进行测试
```
http_load -p 200 -f 100000 urls.txt 
```

## siege

https://www.joedog.org/siege-home/

```
siege -c 200 -r 500 -f urls.txt -i -b
siege -c 200 -t 5   -f urls.txt -i -b
```

## locust

https://docs.locust.io/en/stable/index.html

# 数据采集&诊断

- [dstat](http://dag.wiee.rs/home-made/dstat/)  
```
dstat -tlcymsrn --tcp 2 300
dstat -tlcymsrn --tcp --nocolor --output dstat_$(hostname)_$(date "+%Y%m%d_%H%M%S").csv 2 300 >/dev/null 2>&1 &
```
- [vjtop](https://github.com/vipshop/vjtools/tree/master/vjtop)
```
./vjtop.sh -d 2 PID
```
- [arthas](https://arthas.aliyun.com/doc/quick-start.html)  
