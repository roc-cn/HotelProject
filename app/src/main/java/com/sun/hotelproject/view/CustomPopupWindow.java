package com.sun.hotelproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.henry.calendarview.DatePickerController;
import com.henry.calendarview.DayPickerView;
import com.henry.calendarview.SimpleMonthAdapter;
import com.sun.hotelproject.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;

/**
 * @author sun
 * TODO 底部弹出的pop
 */
public class CustomPopupWindow extends PopupWindow implements View.OnClickListener {
    private View mPopView;
    private OnItemClickListener mListener;
    DayPickerView dayPicker;
    TextView cancel,crifrm;
   // Date startDate;
    Date endDate;

    private Context c;
    public CustomPopupWindow(Context context) {
        super(context);
        this.c=context;

        // TODO Auto-generated constructor stub
        init(context);
        setPopupWindow();
        cancel.setOnClickListener(this);
        crifrm.setOnClickListener(this);
    }
    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局

        mPopView = inflater.inflate(R.layout.popupwindow_view, null);
        dayPicker=mPopView.findViewById(R.id.dayPicker);
        cancel=mPopView.findViewById(R.id.cancel);
        crifrm=mPopView.findViewById(R.id.cirfim);

        Calendar now = Calendar.getInstance();
        DayPickerView.DataModel dataModel = new DayPickerView.DataModel();
        dataModel.yearStart = now.get(Calendar.YEAR);
        dataModel.monthStart = now.get(Calendar.MONTH);
        dataModel.monthCount = 6;
        dataModel.defTag = "";
        dataModel.leastDaysNum = 2;
        dataModel.mostDaysNum = 180;


        dayPicker.setParameter(dataModel, new DatePickerController() {
            @Override
            public void onDayOfMonthSelected(SimpleMonthAdapter.CalendarDay calendarDay) {
                calendarDay.getDate();
               //Toast.makeText(c, calendarDay.getDate()+"1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateRangeSelected(List<SimpleMonthAdapter.CalendarDay> selectedDays) {
                if (selectedDays != null) {
                 //   SimpleMonthAdapter.CalendarDay starttime = selectedDays.get(0);
                    SimpleMonthAdapter.CalendarDay endtime = selectedDays.get(selectedDays.size() - 1);
                  //  startDate = starttime.getDate();
                    endDate = endtime.getDate();

                }

               //Toast.makeText(c, startDate+"\n"+endDate+"2", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void alertSelectedFail(FailEven even) {
             // Toast.makeText(c, even.name(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置窗口的相关属性
     */
    @SuppressLint("InlinedApi")
    private void setPopupWindow() {
        this.setContentView(mPopView);// 设置View
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);// 设置弹出窗口的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);// 设置弹出窗口的高
        this.setFocusable(true);// 设置弹出窗口可
        this.setAnimationStyle(R.style.mypopwindow_anim_style);// 设置动画
        this.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
       // this.setBackgroundDrawable(new ColorDrawable(0x00000000));// 设置背景透明
        mPopView.setOnTouchListener(new View.OnTouchListener() {// 如果触摸位置在窗口外面则销毁

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int height = mPopView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    /**
     * 定义一个接口，公布出去 在Activity中操作按钮的单击事件
     */
    public interface OnItemClickListener {
        void setOnItemClick(View v,Date endDate);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (mListener != null) {
            mListener.setOnItemClick(v,endDate);
        }
    }
}
