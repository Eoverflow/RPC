package com.liws.rpc.test.testnetty.testregistry;

/**
 * Created by liweisheng on 16/9/19.
 */
public class IntroduceLihua implements IntroduceSelf {
    @Override
    public String report() {
        return "I am Li Hua";
    }

    @Override
    public void print(String msg) {
        System.out.println("Send " + msg + " to LiHua");
    }

    @Override
    public void print(String msg, String ps) {
        System.out.println("Send " +  msg + " to LiHua, PS:" + ps);
    }
}
