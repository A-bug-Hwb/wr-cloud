package com.wr.utils;

import java.util.concurrent.ThreadLocalRandom;

public class NumUtil {

    public static Integer getFourNum(){
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }
}
