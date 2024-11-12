package io.github.tt432.machinemax.utils.control;

/**
 * 一个简单的PD控制器，用于实现对各个可动部件的控制
 *
 * @author 甜粽子
 */
public class PDController {
    private static double P;
    private static double D;
    private static double error;//实时误差
    private static double error_last_frame;//前一次迭代的误差
    private static double error_speed;//误差变化率

    public static double PD(double p, double d, double target, double actual, double targetSpeed, double actualSpeed) {
        return p * (target - actual) - d * (targetSpeed - actualSpeed);
    }
}
