package com.lz.loadbalance;

import com.lz.common.URL;

import java.util.List;
import java.util.Random;

public class LoadBalance {
    public static URL random(List<URL> urlList){
        Random random = new Random();
        int i = random.nextInt(urlList.size());
        return urlList.get(i);
    }
}
