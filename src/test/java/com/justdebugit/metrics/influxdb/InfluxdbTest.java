package com.justdebugit.metrics.influxdb;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.justdebugit.metrics.influxdb.Influxdb;
import com.justdebugit.metrics.influxdb.InfluxdbHttp;
import com.justdebugit.metrics.influxdb.InfluxdbReporter;

public class InfluxdbTest {
  private static MetricRegistry metrics = new MetricRegistry();

  public static void main(String[] args) throws InterruptedException {

    Influxdb influxdb = InfluxdbHttp.newBuilder().dbName("test").host("localhost").build();// dbname必填，必须在influxdb事先创建

    InfluxdbReporter reporter =
        InfluxdbReporter.forRegistry(metrics).appName("hello").convertRatesTo(TimeUnit.SECONDS)// appname必填
            .convertDurationsTo(TimeUnit.MILLISECONDS).build(influxdb);// rate用于度量频率，比如calls/second,duration用于度量经历的时间周期比如100ms

    final Timer timer = metrics.timer("timer1");// 名称不要带.号及其他特殊字符
    reporter.start(5, TimeUnit.SECONDS);// 每5秒发布一次，如果数据变化比较慢，可以将周期加大

   ExecutorService executorService =  Executors.newFixedThreadPool(2);
   executorService.submit(new Runnable() {
    
    @Override
    public void run() {
      for (; ; ) {
        final Timer.Context context = timer.time();
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        } finally {
          context.stop();
        }
      }
    }
  });
   
  final Counter pendingJobs = metrics.counter("counter1");// 名称不要带.号及其他特殊字符
   executorService.submit(new Runnable() {
    
    @Override
    public void run() {
      for (; ; ) {
        pendingJobs.inc(new Random().nextInt(5));
        pendingJobs.dec(new Random().nextInt(5));
      }
      
    }
  });
   
   

    Thread.sleep(Integer.MAX_VALUE);

  }
}
