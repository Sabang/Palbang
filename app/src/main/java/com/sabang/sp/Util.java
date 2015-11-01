package com.sabang.sp;

/**
 * Created by cyc on 2015-11-01.
 */

//여러 클래스에서 쓸법한 함수들 static으로 구현(더이상의 복붙은 naver 하고싶다 ㅜㅜ)
public class Util {

    //맨뒤 *로바꾸기
    static public String hideEmailBack(String email){
        int length = email.length();
        if(length < 8){
            if(length == 3){
                int n0 = email.charAt(0)-'A';
                int n1 = email.charAt(1)-'A';
                int n2 = email.charAt(2)-'A';
                int a = (n0+n1+n2)%52;
                email += ('A'+a);
                length++;
            }

            while(length<8){
                email += "*";
                length++;
            }
        }

        return email.substring(0, length-4)+"****";
    }


}
