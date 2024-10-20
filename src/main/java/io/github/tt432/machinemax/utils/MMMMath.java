package io.github.tt432.machinemax.utils;

import static java.lang.Math.exp;

public class MMMMath {
    /**
     * 取符号，但与signum不同，这个方法的输出值与输入值的关系是连续的
     * 即，±0左右不会发生跳变，而是得到一个0附近的数字
     * @param a 要取符号的值
     * @return 取得的值，介于[-1,1]之间
     */
    public static double sigmoidSignum(double a){
        return (2/(1+exp(-a))-1);
    }
}
