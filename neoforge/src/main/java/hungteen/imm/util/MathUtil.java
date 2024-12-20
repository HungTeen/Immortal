package hungteen.imm.util;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.List;

/**
 * @program Immortal
 * @author HungTeen
 * @create 2023-06-04 22:10
 **/
public class MathUtil {

    /**
     * 用于生成一个平滑的数组，数组值从 [min, max] 之间均匀分布。
     * @param i 当前索引，[0, len)。
     * @param len 数组长度。
     * @param min 最小值。
     * @param max 最大值。
     * @return 第 i 个元素的值。
     */
    public static int uniformArrayValue(int i, int len, int min, int max){
        return min + (max - min) * i / (len - 1);
    }

    public static int[] uniformArray(int len, int min, int max){
        return uniformArray(len, min, max, false);
    }

    public static int[] uniformArray(int len, int min, int max, boolean check){
        if(check) {
            assert (max - min) % (len - 1) == 0;
        }
        int[] array = new int[len];
        for(int i = 0; i < len; i++){
            array[i] = uniformArrayValue(i, len, min, max);
        }
        return array;
    }

    public static int[] sameArray(int len, int value){
        int[] array = new int[len];
        Arrays.fill(array, value);
        return array;
    }

    /**
     * 生成一个动态权重数组，用于调整难度。<br>
     * 以高斯函数为例，截取其中一个长度为 n 的区间，对其采样可以得到一个动态权重数组。<br>
     * 更偏向于高斯函数中心的位置具有更大的权重。<br>
     * factor 值越大，高斯函数的中心会逐渐偏向数组右边。<br>
     * @param factor 值越大，数组后面也越大。
     * @param n 数组的长度。
     */
    public static List<Integer> genLinearWeights(int n, float factor, double std) {
        return Arrays.stream(generateGaussianArray(n, n * factor, std))
                .boxed()
                .map(x -> (int) (x * 100))
                .toList();
    }

    /**
     * 生成一个高斯数组。
     * @param n 数组长度。
     * @param mean 均值，中心位置，取值于[0,n]。
     * @param std 标准差，越大数组之间的差距越小。
     */
    public static double[] generateGaussianArray(int n, double mean, double std) {
        double[] weights = new double[n];
        double sum = 0;

        for (int i = 0; i < n; i++) {
            double x = i;
            weights[i] = (1 / (std * Math.sqrt(2 * Math.PI)))
                    * Math.exp(-Math.pow(x - mean, 2) / (2 * Math.pow(std, 2)));
            sum += weights[i];
        }

        // 归一化权重数组（可选，确保总和为 1）。
        for (int i = 0; i < n; i++) {
            weights[i] /= sum;
        }

        return weights;
    }

    public static boolean inSlotArea(double x, double y, int posX, int posY, int lenX, int lenY){
        return x >= posX && x <= posX + lenX && y >= posY && y <= posY + lenY;
    }
    public static Direction[] getHorizontalDirections(){
        return Direction.stream().filter(dir -> dir.getAxis().isHorizontal()).toArray(Direction[]::new);
    }

    public static double horizontalLength(Vec3 vec){
        return Math.sqrt(vec.x() * vec.x() + vec.z() * vec.z());
    }

    public static double log2(double x){
        return Math.log(x) / Math.log(2);
    }

    public static float toRadian(float degree){
        return degree * ((float)Math.PI / 180F);
    }

    public static double toRadian(double degree){
        return degree * (Math.PI / 180D);
    }

    public static Vec3 rotateHorizontally(Vec3 vec, double degree){
        final double sin = Math.sin(toRadian(degree));
        final double cos = Math.cos(toRadian(degree));
        return new Vec3(vec.z * cos - vec.x * sin, vec.y, vec.x * cos + vec.z * sin);
    }

    public static int getBarLen(float num, float maxNum, int maxLen) {
        return getBarLen(maxNum == 0 ? 1F : num / maxNum, maxLen);
    }

    public static int getBarLen(float percent, int maxLen) {
        if (percent >= 0) {
            int len = Mth.floor(percent * maxLen);
            if (len <= 0 && percent != 0) {
                return 1;
            } else if(len >= maxLen && percent < 1) {
                return maxLen - 1;
            }
            return Mth.clamp(len, 0, maxLen);
        } else {
            return 0;
        }
    }

    public static AABB getUpperAABB(Vec3 pos, float width, float height){
        return new AABB(pos.x() - width, pos.y(), pos.z() - width, pos.x() + width, pos.y() + height, pos.z() + width);
    }

    public static AABB getAABB(Vec3 pos, double horizontalRange, double verticalRange) {
        return new AABB(pos.x() - horizontalRange, pos.y() - verticalRange, pos.z() - horizontalRange, pos.x() + horizontalRange, pos.y() + verticalRange, pos.z() + horizontalRange);
    }

    public static boolean inAABB(Vec3 pos, AABB aabb){
        return aabb.contains(pos);
    }

}
