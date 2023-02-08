package com.github.kewei1.thread;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.concurrent.*;
import java.util.function.*;

public class FutureUtil {

    private static final Log log = LogFactory.get();

    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors()*8, Runtime.getRuntime().availableProcessors()*8, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());


    public static void executorCount(String name){
        log.info(name+"活动线程数" + executor.getActiveCount());
        log.info(name+"任务数" + executor.getTaskCount());
        log.info(name+"完成任务数" + executor.getCompletedTaskCount());
        log.info(name+"核心线程数" + executor.getCorePoolSize());
        log.info(name+"最大线程数" + executor.getLargestPoolSize());
        log.info(name+"线程池大小" + executor.getPoolSize());
    }


    /**
     * 异步执行<br>
     *     public static Runnable runnable = () -> {<br>
     *         System.out.println("执行");<br>
     *     };<br>
     * 调用方式<br>
     *    doRrnnable(()-{<br>
     *     System.out.println("执行");<br>
     *    });<br>
     * @param runnable Runnable

     * @author kewei
     * @since 2023/02/08
     */
    public static void doRrnnable(Runnable runnable){
        executorCount("提交前----");
        log.info("-------- 提交runnable 开始 ----------");
        executor.execute(runnable);
        log.info("-------- 提交runnable 成功 ----------");
    }



    /**
     * 异步执行<br>
     *     public static Callable callable = () -> {<br>
     *         System.out.println("执行");<br>
     *         return "执行";<br>
     *     };<br>
     * 调用方式<br>
     *     doCallable(()-{<br>
     *         System.out.println("执行");<br>
     *     });<br>
     *
     * @param callable Callable
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doCallable(Callable callable) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交callable 开始 ----------");
        Future future = executor.submit(callable);
        log.info("-------- 提交callable 成功 ----------");
        return future;
    }


    /**
     * 异步执行 消费<br>
     *    public static Consumer consumer = (x) -> { <br>
     *     System.out.println("执行"+x);<br>
     *     };<br>
     * 调用方式<br>
     *     doConsumer((x) -> {<br>
     *     System.out.println("执行"+x);<br>
     *     });<br>
     *
     * @param consumer 消费者
     * @author kewei
     * @since 2023/02/08
     */
    public void doConsumer(Consumer consumer){
        executorCount("提交前----");
        log.info("-------- 提交consumer 开始 ----------");
        consumer.accept("consumer");
        executor.execute(()->{
            consumer.accept("consumer");
        });
        log.info("-------- 提交consumer 成功 ----------");
    }


/**
     * 异步执行 供给<br>
     *     public static Supplier supplier = () -> {<br>
     *         System.out.println("执行");<br>
     *         return "执行";<br>
     *     };<br>
     * 调用方式<br>
     *     doSupplier(() -> {<br>
     *         System.out.println("执行");<br>
     *         return "执行";<br>
     *     });<br>
     *
     * @param supplier 供给者
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doSupplier(Supplier supplier) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交supplier 开始 ----------");

        Future future = doCallable(() -> {
            return supplier.get();
        });

        log.info("-------- 提交supplier 成功 ----------");
        return future;
    }


    /**
     * 异步执行 函数<br>
     *     public static Function function = (x) -> {<br>
     *         System.out.println("执行"+x);<br>
     *         return "执行";<br>
     *     };<br>
     * 调用方式<br>
     *     doFunction((x) -> {<br>
     *         System.out.println("执行"+x);<br>
     *         return "执行";<br>
     *     });<br>
     *
     * @param function 函数
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doFunction(Function function) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交function 开始 ----------");
        Future future = doCallable(() -> {
            return function.apply("function");
        });
        log.info("-------- 提交function 成功 ----------");
        return future;
    }


    /**
     * 异步执行 消费<br>
     *    public static BiConsumer biConsumer = (x,y) -> { <br>
     *     System.out.println("执行"+x+y);<br>
     *     };<br>
     * 调用方式<br>
     *     doBiConsumer((x,y) -> {<br>
     *     System.out.println("执行"+x,y);<br>
     *     });<br>
     *
     * @param BiConsumer 消费者
     * @author kewei
     * @since 2023/02/08
     */
    public void doBiConsumer(BiConsumer biConsumer){
        executorCount("提交前----");
        log.info("-------- 提交BiConsumer 开始 ----------");
        executor.execute(()->{
            biConsumer.accept("BiConsumer","BiConsumer");
        });
        log.info("-------- 提交BiConsumer 成功 ----------");
    }



    /**
     * 异步执行 断言<br>
     *    public static BiPredicate biPredicate = (x,y) -> { <br>
     *     System.out.println("执行"+x+y);<br>
     *     return true;<br>
     *     };<br>
     * 调用方式<br>
     *     doBiPredicate((x,y) -> {<br>
     *     System.out.println("执行"+x,y);<br>
     *     return true;<br>
     *     });<br>
     *
     * @param BiPredicate 断言者
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doBiPredicate(BiPredicate biPredicate) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交BiPredicate 开始 ----------");
        Future future = doCallable(() -> {
            return biPredicate.test("BiPredicate","BiPredicate");
        });
        log.info("-------- 提交BiPredicate 成功 ----------");
        return future;
    }


    /**
     * 异步执行 函数<br>
     *     public static BiFunction biFunction = (x,y) -> {<br>
     *         System.out.println("执行");<br>
     *         return "执行";<br>
     *     };<br>
     * 调用方式<br>
     *     doBiFunction((x,y) -> {<br>
     *         System.out.println("执行");<br>
     *         return "执行";<br>
     *     });<br>
     *
     * @param biFunction 函数
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doBiFunction(BiFunction biFunction) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交BiFunction 开始 ----------");
        Future future = doCallable(() -> {
            return biFunction.apply("BiFunction","BiFunction");
        });
        log.info("-------- 提交BiFunction 成功 ----------");
        return future;
    }



    /**
     * 异步执行 断言<br>
     *    public static Predicate predicate = (x) -> { <br>
     *     System.out.println("执行");<br>
     *     return true;<br>
     *     };<br>
     * 调用方式<br>
     *     doPredicate((x) -> {<br>
     *     System.out.println("执行");<br>
     *     return true;<br>
     *     });<br>
     *
     * @param predicate 断言者
     * @return Future
     * @throws Exception Exception
     * @since 2023/02/08
     */
    public static Future doPredicate(Predicate predicate) throws Exception {
        executorCount("提交前----");
        log.info("-------- 提交Predicate 开始 ----------");
        Future future = doCallable(() -> {
            return predicate.test("Predicate");
        });
        log.info("-------- 提交Predicate 成功 ----------");
        return future;
    }
}



