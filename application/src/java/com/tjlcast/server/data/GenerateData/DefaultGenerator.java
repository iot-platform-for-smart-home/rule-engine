package com.tjlcast.server.data.GenerateData;

import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import com.tjlcast.server.data_source.Item;
import org.apache.commons.lang3.RandomUtils;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Created by tangjialiang on 2018/5/22.
 */
public abstract class DefaultGenerator implements Runnable {
    private final int interTime ;
    public Subscription subscribe;
    public Observable<FromMsgMiddlerDeviceMsg> observe;

    DefaultGenerator(int interTime) {
        this.interTime = interTime ;
    }

    protected Observable<FromMsgMiddlerDeviceMsg> observe() {
        return Observable
                .interval(interTime, TimeUnit.MILLISECONDS)
                .delay(x -> Observable.timer(RandomUtils.nextInt(0, 10), TimeUnit.MICROSECONDS))
                .map(this::generateRandDeviceIdMsg)
                .map(this::occasionallyKV)
                .subscribeOn(Schedulers.trampoline())
                .observeOn(Schedulers.trampoline());
    }

    protected abstract void work(FromMsgMiddlerDeviceMsg msg) ;

    public final void cancel() {
        this.observe.unsubscribeOn(Schedulers.trampoline()) ;
        this.subscribe.unsubscribe();
    }

    private void consume() {
        this.observe = observe();
        this.subscribe = observe
                .subscribe(
                        this::work,
                        e -> System.out.println("Error emitting event " + e)
                );
    }

    @Override
    public void run() {
        consume() ;
    }

    private FromMsgMiddlerDeviceMsg generateRandDeviceIdMsg(long time) {
        int devicesNum = 10;    // 10 个设备
        int tenantId = 2;       // tenantId 为1
        return new FromMsgMiddlerDeviceMsg.Builder(tenantId, "5")
                .build();
    }

    private FromMsgMiddlerDeviceMsg occasionallyKV(FromMsgMiddlerDeviceMsg msg) {
        Long ts = System.currentTimeMillis()/1000L;
        String key = "x" ;
        String value = String.valueOf(RandomUtils.nextInt(0, 101) ) ;

        msg.getData().add(new Item(key, value, ts)) ;
        return msg ;
    }
}