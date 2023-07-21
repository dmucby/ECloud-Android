package cn.zf233.xcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.appcompat.app.AppCompatActivity;

import cn.zf233.xcloud.R;
import cn.zf233.xcloud.common.Const;
import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.service.UserService;
import cn.zf233.xcloud.service.impl.UserServiceImpl;
import cn.zf233.xcloud.util.FileUtil;
import cn.zf233.xcloud.util.RequestUtil;

public class WelcomeActivity extends AppCompatActivity {

    private final UserService userService = new UserServiceImpl();

    private View welcomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeLayout = findViewById(R.id.welcomeLayout);
        new Thread(new WelcomeRunnable()).start();
    }

    // callback
    // 阻塞式地从消息队列中接收消息并进行处理的“线程” ——【Thread+Looper】
    // looper 加锁队列 保证UI界面线程安全 同时只有一个页面被更改
    private void jumpActivity(Intent intent, String msg) {
        /**
         * 1.判定是否已有Looper并Looper.prepare()
         * 2.做一些准备工作(如暴露handler等)
         * 3.调用Looper.loop()，线程进入阻塞态
         */
        Looper.prepare();
        new Handler().postDelayed(() -> {
            // 淡入淡出动画
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(300);
            alphaAnimation.setFillAfter(true);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    /**
                     * 1. 打包数据，跳转到MAIN页面
                     */
                    intent.putExtra(Const.MSG.getDesc(), msg);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            welcomeLayout.startAnimation(alphaAnimation);

        }, 1500);
        Looper.loop();
    }

    // check user login status
    private class WelcomeRunnable implements Runnable {

        @Override
        public void run() {
            Intent intent;
            String msg;
            // 测试使用
//            FileUtil.removeShared(WelcomeActivity.this, Const.CURRENT_USER.getDesc());
            // 获取登录状态
            User user = FileUtil.inputShared(WelcomeActivity.this, Const.CURRENT_USER.getDesc(), User.class);
            if (user == null) {
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
                jumpActivity(intent, null);
                return;
            }
            // 自动登录
            BaseResponse<User> response = userService.login(RequestUtil.getRequestUtil(), user);
            if (!response.isSuccess()) {
                FileUtil.removeShared(WelcomeActivity.this, Const.CURRENT_USER.getDesc());
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
                msg = response.getMsg();
            } else if (response.isSuccess()) {
                // 登陆成功 跳转HOME页面
                user.setId(response.getData().getId());
                FileUtil.removeShared(WelcomeActivity.this, Const.CURRENT_USER.getDesc());
                FileUtil.outputShared(WelcomeActivity.this, Const.CURRENT_USER.getDesc(), user);
                intent = new Intent(WelcomeActivity.this, ActivityHome.class);
                msg = response.getMsg();
            } else {
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
                msg = "服务器未响应";
            }
            // msg 显示时间
            jumpActivity(intent, msg);
        }
    }
}