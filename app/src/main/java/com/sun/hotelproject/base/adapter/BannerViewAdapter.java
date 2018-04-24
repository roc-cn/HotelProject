//package com.sun.hotelproject.base.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.support.v4.view.PagerAdapter;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.sun.hotelproject.entity.BannerModel;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BannerViewAdapter extends PagerAdapter {
//
//    private Context context;
//    private List<BannerModel> listBean;
//
//    public BannerViewAdapter(Activity context, List<BannerModel> list) {
//        this.context = context.getApplicationContext();
//        if (list == null || list.size() == 0) {
//            this.listBean = new ArrayList<>();
//        } else {
//            this.listBean = list;
//        }
//    }
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, final int position) {
//        if (listBean.get(position).getUrlType() == 0) {//图片
//            final ImageView imageView = new ImageView(context);
//            Glide.with(context).load(listBean.get(position).getBannerUrl())
//                    .skipMemoryCache(true)
//                    .into(imageView);
//            container.addView(imageView);
//
//            return imageView;
//        }else{//视频
//          /*  final VideoView videoView = new VideoView(context);
//            videoView.setVideoURI(Uri.parse(listBean.get(position).getBannerUrl()));
//            //开始播放
//            videoView.start();
//            container.addView(videoView);*/
//            final NiceVideoPlayer niceVideoPlayer =new NiceVideoPlayer(context);
//            niceVideoPlayer.setPlayerType(NiceVideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
//            niceVideoPlayer.setUp(listBean.get(position).getBannerUrl(), null);
//            TxVideoPlayerController controller = new TxVideoPlayerController(context);
//           // controller.setTitle("CHIC");
//            controller.setLenght(48000);
////            Glide.with(context)
////                    .load("http://imgsrc.baidu.com/image/c0%3Dshijue%2C0%2C0%2C245%2C40/sign=304dee3ab299a9012f38537575fc600e/91529822720e0cf3f8b77cd50046f21fbe09aa5f.jpg")
////                    .placeholder(R.drawable.img_default)
////                    .crossFade()
////                    .into(controller.imageView());
//            niceVideoPlayer.setController(controller);
//          //  niceVideoPlayer.start();
//            container.addView(niceVideoPlayer);
//            return niceVideoPlayer;
//        }
//
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        container.removeView((View) object);
//    }
//
//    @Override
//    public int getCount() {
//        return listBean.size();
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return view == (View) object;
//    }
//}