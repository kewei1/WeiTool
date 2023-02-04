package com.github.kewei1.thread;



import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.GlobalThreadPool;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.GlobalLogFactory;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.github.kewei1.log.WeiLog;
import com.google.common.util.concurrent.Futures;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;


public class FutureUtil {

    private static final Log log = LogFactory.get();





    //test2
    @Test
    public void  test2(){
        log.error("test{}{}{}{}","test","test","test","test");
        log.info("test");
        log.warn("test");
        log.debug("test");
    }




    //test
    @Test
    public void  test() throws Exception {


        runnable.run();

        Callable callable = () -> {
            System.out.println("执行");
            return "执行";
        };

        Object call = callable.call();

        System.out.println(call);



        List<String> list = new ArrayList<>(10);
        list.add("1");


        list.stream().forEach((e)->{

        });

        list.stream().forEach(e1->{

        });

        list.stream().map(e->{
            return e;
        }).max((e1,e2)->{
            return 1;
        });

        //Runnable
        Runnable runnable = () -> {
            System.out.println("执行");
        };

        Runnable runnable1 = () -> {
            System.out.println("执行");
        };

        //Comparator
        Comparator<String> comparator = (x, y) -> {
            return 1;
        };








    }





























    //Runnable
    public static Runnable runnable = () -> {
        System.out.println("执行");
    };

    //Callable
   Callable callable = () -> {
        System.out.println("执行");
        return "执行";
    };

    //Consumer
    Consumer consumer = (x) -> {
        System.out.println("执行");
    };

    //
    Supplier supplier = () -> {
        System.out.println("执行");
        return "执行";
    };
    //Function
    Function function = (x) -> {
        System.out.println("执行");
        return "执行";
    };

    //BiConsumer
    BiConsumer biConsumer = (x,y) -> {
        System.out.println("执行");
    };
    //BiFunction
    BiFunction biFunction = (x,y) -> {
        System.out.println("执行");
        return "执行";
    };


    //BiPredicate
    BiPredicate biPredicate = (x,y) -> {
        System.out.println("执行");
        return true;
    };
    //Predicate
    Predicate predicate = (x) -> {
        System.out.println("执行");
        return true;
    };




    /**
     * 创建两倍于系统线程数大小的线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*8);


    @Test
    public void test3(){
           System.out.println(Runtime.getRuntime().availableProcessors());
    }





    public void fun() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行");
            }
        });
    }



    public static <T> Object synchronizeExecute(long time, TimeUnit timeUnit, Callable<T> callable) {
        Future future = executorService.submit(callable);
        try {
            return future.get(time, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            future.cancel(true);
            System.out.println("执行超时");
            return null;
        }
    }

    public static void synchronizeExecute( Runnable callable) {
        Future future = executorService.submit(callable);
    }


    public static void shutdown(){
        executorService.shutdown();
    }


    //测试、


    public static void main(String[] args) throws Exception {


        for (int i = 0;i<1000;i++){
            ThreadUtil.execAsync(() -> {
                ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                int number = threadLocalRandom.nextInt(20) + 1;
                System.out.println(number);
            });

            execute(()->{
                System.out.println(UUID.randomUUID());
                return "ssjskdhks";
            });

            execute2(()->{
                System.out.println(UUID.randomUUID());
                return null;
            });
            System.out.println("当前第：" + i + "个线程");
        }

        GlobalThreadPool.shutdown(false);
        shutdown();
        System.out.println("shutdown()");

        thenRunAsync(()->{
            System.out.println("cf1");
            return "333";
        },()->{
            System.out.println("cf2");
        });

        System.out.println(ThreadUtil.currentThreadGroup().activeCount());


    }


    public static  String execute(Callable callable) throws Exception {
        Future<String> future = executorService.submit(callable);
        //这里需要返回值时会阻塞主线程
        return future.get();
    }

    public static  void execute2(Callable callable) throws Exception {
        executorService.submit(callable);
    }


    public static void thenRunAsync(Supplier supplier , Runnable runnable) throws Exception {

        CompletableFuture<Integer> cf1 = CompletableFuture.supplyAsync(supplier);

        CompletableFuture<Void> cf2 = cf1.thenRunAsync(runnable);

        //等待任务1执行完成
        System.out.println("cf1结果->" + cf1.get());
        //等待任务2执行完成
        System.out.println("cf2结果->" + cf2.get());
    }



}



