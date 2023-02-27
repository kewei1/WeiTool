package com.github.kewei1.sort;

import com.sun.javaws.Main;

import java.util.Arrays;

/**
 * @author kewei
 * @since 2023/02/06
 * 九大排序算法
 */
public class WeiSort {



    //For example, given nums = [0, 1, 0, 3, 12], after calling your function, nums should be [1, 3, 12, 0, 0].
    public static void moveZeroes(int[] nums){
        int idx = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[idx++] = num;
            }
        }
        //打印 nums
        System.out.println(Arrays.toString(nums));



        while (idx < nums.length) {
            nums[idx++] = 0;
        }

        //打印 nums
        System.out.println(Arrays.toString(nums));
    }


    public static void main(String[] args) {
        moveZeroes(new int[]{0, 1, 0, 3, 12,12,23,232});
    }


    //排序 - 冒泡排序(Bubble Sort)
    public static void bubbleSort(int[] arr) {
        //外层循环控制排序趟数
        for (int i = 0; i < arr.length - 1; i++) {
            //内层循环控制每一趟排序多少次
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    //排序 - 快速排序(Quick Sort)
    public static void quickSort(int[] a, int l, int r) {

        if (l < r) {
            int i,j,x;

            i = l;
            j = r;
            x = a[i];
            while (i < j) {
                while(i < j && a[j] > x)
                    j--; // 从右向左找第一个小于x的数
                if(i < j)
                    a[i++] = a[j];
                while(i < j && a[i] < x)
                    i++; // 从左向右找第一个大于x的数
                if(i < j)
                    a[j--] = a[i];
            }
            a[i] = x;
            quickSort(a, l, i-1); /* 递归调用 */
            quickSort(a, i+1, r); /* 递归调用 */
        }
    }


    //排序 - 插入排序(Insertion Sort)
    public static void insertSort(int[] a, int n) {
        int i, j, k;

        for (i = 1; i < n; i++) {

            //为a[i]在前面的a[0...i-1]有序区间中找一个合适的位置
            for (j = i - 1; j >= 0; j--)
                if (a[j] < a[i])
                    break;

            //如找到了一个合适的位置
            if (j != i - 1) {
                //将比a[i]大的数据向后移
                int temp = a[i];
                for (k = i - 1; k > j; k--)
                    a[k + 1] = a[k];
                //将a[i]放到正确位置上
                a[k + 1] = temp;
            }
        }
    }





    //排序 - 选择排序(Selection Sort)


    //排序 - 希尔排序(Shell Sort)

    //排序 - 归并排序(Merge Sort)

    //排序 - 堆排序(Heap Sort)

    //排序 - 基数排序(Radix Sort)

    //排序 - 桶排序(Bucket Sort)
}
