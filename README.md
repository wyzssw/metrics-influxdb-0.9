#metrics-influxdb-0.9
   提供metrics到influxdb-0.9的reporter

#以下特性
1. 支持influxdb 0.9新格式
2. 简单，代码量少，除了metrics-core，slf4j-api无额外依赖
3. 默认生成host、pid、appname等tag


#例子
```java
public class InfluxdbTest {
  private static MetricRegistry metrics = new MetricRegistry();

  public static void main(String[] args) throws InterruptedException {

    Influxdb influxdb = InfluxdbHttp.newBuilder().dbName("test").host("localhost").build();// dbname必填，必须在influxdb事先创建

    InfluxdbReporter reporter =
        InfluxdbReporter.forRegistry(metrics).appName("hello").convertRatesTo(TimeUnit.SECONDS)// appname必填
            .convertDurationsTo(TimeUnit.MILLISECONDS).build(influxdb);// rate用于度量频率，比如calls/second,duration用于度量经历的时间周期比如100ms

    final Timer timer = metrics.timer("timer1");// 名称不要带.号及其他特殊字符
    reporter.start(5, TimeUnit.SECONDS);// 每5秒发布一次，如果数据变化比较慢，可以将周期加大

    final Timer.Context context = timer.time();
    try {
      Thread.sleep(10);
    } finally {
      context.stop();
    }

    Counter pendingJobs = metrics.counter("counter1");// 名称不要带.号及其他特殊字符
    pendingJobs.inc();
    Thread.sleep(Integer.MAX_VALUE);

  }
}
```

