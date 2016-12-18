package com.liws.rpc.test.testnetty.com.liws.rpc.test.testTmp;

/**
 * Created by liweisheng on 16/9/18.
 */

interface A{
    public void print();
}

interface B{
    public void print();
}
public class TestIntr implements A, B{
    @Override
    public void print() {

    }

    public static void main(){
        B a = new TestIntr();

    }
}


