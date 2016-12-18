package com.liws.rpc.ipc.task;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by liweisheng on 16/9/21.
 */
public class RequestFuture<T> implements Future<T> {
    private volatile  T result;
    private volatile boolean done;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        synchronized (this){
            while(null == result){
                super.wait();
            }

            return result;

        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        long waitTime = unit.toNanos(timeout);
        long startTime = System.nanoTime();

        if(null != result){
            return result;
        }

        if(Thread.currentThread().isInterrupted()){
            throw new InterruptedException();
        }

        synchronized (this){
            while(null == result){
                try {
                    super.wait(waitTime / 1000000,(int)(waitTime % 1000000));
                } catch (InterruptedException e) {
                    long currentTime = System.nanoTime();
                    if(currentTime - startTime - waitTime > 0){
                        return result;
                    }
                }
            }

            return result;

        }
    }

    synchronized  public void set(T result){
        this.result = result;
    }

    synchronized  public boolean setNX(T result){
        if(null == this.result){
            this.result = result;
            return true;
        }
        return false;
    }

    synchronized public void setDone(boolean done){
        this.done = done;
    }
}
