package com.github.kewei1.thread;



import java.util.concurrent.*;



public class FutureUtil {

    /**
     * 创建两倍于系统线程数大小的线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*8);

    /**
     * 限时异步执行
     *
     * @param time 时间数，例：60
     * @param timeUnit 时间单位，例：TimeUnit.SECONDS
     */
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




    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {

            synchronizeExecute(2, TimeUnit.HOURS,()->{
                System.out.println("执行"+Thread.currentThread().getName());

                return null;
            });
        }
        executorService.shutdown();

    }

    public static void shutdown(){
        executorService.shutdown();
    }


}
