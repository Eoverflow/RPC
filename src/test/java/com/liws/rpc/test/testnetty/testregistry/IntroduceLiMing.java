package com.liws.rpc.test.testnetty.testregistry;

/**
 * Created by liweisheng on 16/9/19.
 */
public class IntroduceLiMing implements IntroduceSelf {
    @Override
    public String report() {
        return "I am Li Ming,Li Hua's father,hehe";
    }

    @Override
    public void print(String msg) {
        System.out.println("Send " + msg + " to LiMing");
    }

    @Override
    public void print(String msg, String ps) {
        System.out.println("Send " +  msg + " to LiMing, PS:" + ps);
    }
}
