package com.huang.practice1.process;

import android.app.Activity;;
import android.os.Bundle;

import com.huang.practice1.R;

import java.util.*;

/**
 * Des:
 * Created by huang on 2018/10/19 0019 10:46
 */
public class ProcessActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ProcessView1 view1 = findViewById(R.id.pv);
        view1.setData(getTextShow(), isSelected());
    }

    public HashMap<Integer, List<String>> getTextShow() {
        List<String> text1 = Arrays.asList("执行通知");
        List<String> text2 = Arrays.asList("送达文书");
        List<String> text3 = Arrays.asList("强制措施", "财产调查", "解除措施");
        List<String> text4 = Arrays.asList("查询存款", "搜查", "传唤", "悬赏执行");
        List<String> text5 = Arrays.asList("查明的财产");
        List<String> text6 = Arrays.asList("查封", "扣押", "冻结", "扣划", "评估", "拍卖");
        List<String> text7 = Arrays.asList("执行和解", "终本约谈", "自动履行");
        HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        map.put(0, text1);
        map.put(1, text2);
        map.put(2, text3);
        map.put(3, text4);
        map.put(4, text5);
        map.put(5, text6);
        map.put(6, text7);
        return map;
    }

    public HashMap<Integer, List<Boolean>> isSelected() {
        List<Boolean> text1 = Arrays.asList(false);
        List<Boolean> text2 = Arrays.asList(false);
        List<Boolean> text3 = Arrays.asList(false, false, true);
        List<Boolean> text4 = Arrays.asList(true, false, false, false);
        List<Boolean> text5 = Arrays.asList(false);
        List<Boolean> text6 = Arrays.asList(false, false, false, false, false, false);
        List<Boolean> text7 = Arrays.asList(false, false, false);
        HashMap<Integer, List<Boolean>> map = new HashMap<Integer, List<Boolean>>();
        map.put(0, text1);
        map.put(1, text2);
        map.put(2, text3);
        map.put(3, text4);
        map.put(4, text5);
        map.put(5, text6);
        map.put(6, text7);
        return map;
    }
}