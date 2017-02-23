package com.example.raffledemo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tvStart;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tvNotice;
    
    private List<TextView> views = new LinkedList<>();//所有的视图
    private int timeC= 100;//变色时间间隔
    private int lightPosition = 0;//当前亮灯位置,从0开始
    private int runCount = 8;//需要转多少圈
    private int lunckyPosition = 4;//中奖的幸运位置,从0开始
    private int lastPosition = 3;
    private TimeCount count,slowCount;
    private static boolean isStop = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init(){
		tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tv6 = (TextView) findViewById(R.id.tv6);
        tv7 = (TextView) findViewById(R.id.tv7);
        tv8 = (TextView) findViewById(R.id.tv8);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
        views.add(tv1);
        views.add(tv2);
        views.add(tv3);
        views.add(tv4);
        views.add(tv5);
        views.add(tv6);
        views.add(tv7);
        views.add(tv8);
        
        count = new TimeCount(timeC*9,timeC);
        slowCount = new TimeCount((timeC+100)*9,timeC+100);
        try {
            tvStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvNotice.setText("");
                    runCount = 8;
                    lunckyPosition = 7;
                    if(isStop){
                    	count.start();
                    	isStop = false;
                    	tvStart.setText("结束");
                    }else{
                    	count.onFinish();
                    	isStop = true;
                    	tvStart.setText("开始");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private int randomNum(int minNum,int maxNum){
		int max = maxNum;
        int min = minNum;
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
	}
	
	class TimeCount extends CountDownTimer{

		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			lightPosition = 0;
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if(runCount>0){
				if (lightPosition>0){
					for (TextView tv : views) {
						tv.setBackgroundColor(Color.TRANSPARENT);
					}
                }
                if (lightPosition<8){
                    views.get(randomNum(0,7)).setBackgroundColor(Color.RED);
                }
                
			}else if(runCount == 0){
				
				if (lightPosition<=lunckyPosition){
                    if (lightPosition>0){
                    	for (TextView tv : views) {
    						tv.setBackgroundColor(Color.TRANSPARENT);
    					}
                    }
                    if (lightPosition<8){
                    	lunckyPosition = randomNum(0,7);
                        views.get(lunckyPosition).setBackgroundColor(Color.RED);
                    }
                }
			}
			
			lightPosition ++ ;
		}

		@Override
		public void onFinish() {
			//如果不是最后一圈，需要还原最后一块的颜色
            TextView tvLast= views.get(lunckyPosition);
            if(!isStop){
            	if (runCount!=0){
            		tvLast.setBackgroundColor(Color.TRANSPARENT);
            		//最后几转速度变慢
            		if (runCount<3){ 
            			timeC = 150;
            		}
            		new TimeCount(timeC*9,timeC).start();
            		runCount--;
            	}
            }else{
            	tvLast.setBackgroundColor(Color.TRANSPARENT);
            }
            //如果是最后一圈且计时也已经结束
            if ((runCount==0&&lightPosition==8)|| isStop ){
            	views.get(lastPosition).setBackgroundColor(Color.RED);
                tvStart.setClickable(true);
                tvStart.setEnabled(true);
                tvNotice.setText("恭喜你抽中: "+views.get(lastPosition).getText().toString());
                if (lunckyPosition!=views.size())
                    tvLast.setBackgroundColor(Color.TRANSPARENT); 

            }
			
		}
		
	}
}
