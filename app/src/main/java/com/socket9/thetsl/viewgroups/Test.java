package com.socket9.thetsl.viewgroups;

/**
 * Created by Euro (ripzery@gmail.com) on 6/22/2016 AD.
 */

public class Test {

    public Test(){

    }

    private boolean validateCitizenID(long citizenID){
        long base = 100000000000l;
        int currentBase;
        int sum = 0;
        for(int i = 13; i > 1; i--) {
            currentBase = (int)Math.floor(citizenID/base);
            citizenID = citizenID - currentBase*base;
            sum += currentBase*i;
            base = base/10;
        }
        int checkBit = (11 - (sum%11))%10;
        return checkBit == 9;
    }
}
