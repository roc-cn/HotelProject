package com.sun.hotelproject.base;

import android.text.TextUtils;
import android.util.Log;



import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;



///**
// * @author sun
// * 时间：2017/11/22
// * TODO:基类网络 请求
// */
//public abstract class BasePresenter {
//
//    private Timer timer;
//
//    private JsonRequest request;
//
//    public void requestData(int what, Map<String, Object> map, String url) {
//        if (null == map)
//            throw new IllegalArgumentException("no enity");
//        if (what == HttpUrl.LOOP_WHAT) {
//            timer = new Timer();
//            timer.schedule(new LoopTimerTask(what, map, url), 2000, 3500);
//        } else {
//            requestPost(what, map, url);
//        }
//    }
//
//    private class LoopTimerTask extends TimerTask {
//        int what;
//        Map<String, Object> map;
//        String url;
//        int count = 0;
//
//        LoopTimerTask(int what, Map<String, Object> map, String url) {
//            this.what = what;
//            this.map = map;
//            this.url = url;
//        }
//
//        @Override
//        public void run() {
//            if (count > 30)
//                cancel();
//            requestPost(what, map, url);
//            count++;
//        }
//    }
//
//    private void requestPost(int what, Map<String, Object> map, String url) {
//        request = new JsonRequest(url, RequestMethod.POST);
//        request.setCancelSign(what);
//        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
//        request.set(map);
//        request.setRetryCount(3);
//        CallServer.getHttpclient().add(what, request, new HttpListener<JSONObject>() {
//            @Override
//            public void success(int what, Response<JSONObject> response) {
//                Log.d("BasePresenter",
//                    "success(BasePresenter.java:75)"+response.get().toJSONString());
//                if (response.get() != null) {
//                    if (what == HttpUrl.LOOP_WHAT) {//轮训任务
//                        JSONArray array = response.get().getJSONArray("varList");
//                        if (array.size() > 0) {
//                            JSONObject object = array.getJSONObject(0);
//                            if (TextUtils.equals(object.getString("PAYSTS"), "1")) {
//                                onAllSuccess(HttpUrl.LOOP_WHAT, object);
//                                cancelTimerTask();
//                            }
//                        }
//                    } else//其他网络任务
//                        onAllSuccess(what, response.get());
//                } else onFail("请求服务器异常!");
//            }
//
//            @Override
//            public void fail(int what, String e) {
//                onFail("网络或服务器异常!");
//            }
//        });
//
//    }
//
//    protected abstract void onAllSuccess(int what, JSONObject result);
//
//    protected abstract void onFail(String failStr);
//
//    public void cancelTimerTask() {
//        if (timer != null) {
//            Log.d("BasePresenter",
//                "cancelTimerTask(BasePresenter.java:105)TimerTask stop ……");
//            timer.cancel();
//            request.cancelBySign(HttpUrl.LOOP_WHAT);
//        }
//    }

//}
