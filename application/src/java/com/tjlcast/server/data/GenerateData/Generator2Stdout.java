package com.tjlcast.server.data.GenerateData;

import com.tjlcast.server.data_source.FromMsgMiddlerDeviceMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tangjialiang on 2018/5/22.
 */
public class Generator2Stdout extends DefaultGenerator {

    // 系统日志
    private static final Logger LOGGER = LoggerFactory.getLogger(Generator2Stdout.class) ;

    public Generator2Stdout(int interTime) {
        super(interTime);
    }

    @Override
    protected void work(FromMsgMiddlerDeviceMsg msg) {
        System.out.println(Thread.currentThread().getName()) ;
        System.out.println(msg) ;
    }

    public static void main(String[] args) throws InterruptedException {
        Generator2Stdout generator2Stdout = new Generator2Stdout(10);
        Thread thread = new Thread(generator2Stdout);
        thread.start();

        Thread.sleep(5000);
        generator2Stdout.subscribe.unsubscribe();

        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println(Thread.currentThread().getName()) ;
        }
    }
}
