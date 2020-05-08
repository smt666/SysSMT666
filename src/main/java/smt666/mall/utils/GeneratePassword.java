package smt666.mall.utils;

import java.util.Date;
import java.util.Random;
import java.util.Scanner;

/**
 * 由吸引生成一个密码。（该密码指的是后台管理的密码，一般会通过邮件发送给用户确认）
 * @author 卢2714065385
 * @date 2020-05-04 20:42
 */
public class GeneratePassword {

    /** 指定可选的字符。 */
    public final static String[] WORD = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n","o", "p", "q",
            "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "￡", "@", " ", "'",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "-", "=", ">", "~", ".", "*", "#", "%", "^", "+"};
    public final static String[] SIGN = {
            "1", "2", "3", "4", "5", "6", "7", "8", "9","a", "b", "c", "d", "e", "f",
            "_", "-", "=", "'", "[", "]", "{", "}", "?", ":", "|", "!", "&",
            "$", "<", ">", "~", ".", "*", "#", "%", "^", "+", " ", "￡", "@",
            "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G",
            "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X"};
    public final static String[] CODE = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n","o", "p", "q",
            "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0","_", "-", "=", "'", "[", "]", "{", "}", "?", ":",
            "$", "<", ">", "~", ".", "*", "#", "%", "^", "+", " ", "￡", "@", "!", "&"};

    public static String randomPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        boolean flag = false;
        // 生成20位的密码，其中随机生成的有15位。
        int length = random.nextInt(15) + 20;
        // 生成每一个字符。
        for (int i=0; i< length; i++) {
            if (flag) {
                stringBuffer.append(WORD[random.nextInt(WORD.length)]);
            } else {
                stringBuffer.append(SIGN[random.nextInt(SIGN.length)]);
            }
            flag = !flag;
        }
        return stringBuffer.toString();
    }

    public static String randomCode() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        // 生成10位的密码，其中随机生成的有9位。
        int len = random.nextInt(3) +8 ;
        // 生成每一个字符。
        for (int i=0; i< len; i++) {
            stringBuffer.append(CODE[random.nextInt(CODE.length)]);
        }
        return stringBuffer.toString();
    }

    public static void getA(){
        // 定义一个字符型数组用来存放密码数据随机取值的内容
        char[] pardStore = new char[80];
        // 把所有的大写字母放进去
        char beg = 'A';
        for(int i=0;i<=25;i++) {
            pardStore[i]=beg;
            beg=(char)(beg+1);
        }
        System.out.print(pardStore);
        // 把所有的小写字母放进去
        char beg1 = 'a';
        for(int i=26;i<=51;i++) {
            pardStore[i]=beg1;
            beg1=(char)(beg1+1);
        }
        // 把0到9放进去
        char beg2 = '0';
        for(int i=52;i<=61;i++) {
            pardStore[i]=beg2;
            beg2=(char)(beg2+1);
        }
        // 分别以1、2、3作为种子数 生成6位随机密码
        Random  random = new Random(3);
        for(int i=1 ; i<=6;i++) {
            int n =random.nextInt(62);
            System.out.print(pardStore[n]);
        }
        for(int i=0;i<=71;i++) {
            pardStore[i]=beg;
            beg=(char)(beg+1);
        }
        System.out.print(pardStore);
    }


}
