package cn.zf233.xcloud.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang.StringUtils;

import cn.zf233.xcloud.R;
import cn.zf233.xcloud.common.Const;
import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.service.UserService;
import cn.zf233.xcloud.service.impl.UserServiceImpl;
import cn.zf233.xcloud.util.FileUtil;
import cn.zf233.xcloud.util.JumpActivityUtil;
import cn.zf233.xcloud.util.RequestUtil;
import cn.zf233.xcloud.util.ToastUtil;

public class ActivityLogin extends AppCompatActivity {

    private final UserService userService = new UserServiceImpl();

    private EditText usernameLoginText;
    private EditText passwordLoginText;
    private View loginUserLayout;
    private Animation clickAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLoginText = findViewById(R.id.usernameLoginText);
        passwordLoginText = findViewById(R.id.passwordLoginText);
        // 登录按钮
        loginUserLayout = findViewById(R.id.loginUserLayout);
        clickAnimation = AnimationUtils.loadAnimation(this, R.anim.click);

        // login
        loginUserLayout.setOnClickListener(v -> {
            loginUserLayout.startAnimation(clickAnimation);
            String username = usernameLoginText.getText().toString().trim();
            String password = passwordLoginText.getText().toString().trim();
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                ToastUtil.showShortToast("用户名密码不可为空");
                return;
            }
            if (username.length() < 4 || password.length() < 5) {
                ToastUtil.showShortToast("用户名密码格式有误");
                return;
            }
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            BaseResponse<User> response = userService.login(RequestUtil.getRequestUtil(), user);
            if (response.isSuccess()) {
                // 从返回的登陆数据获取用户的详细数据
                // TODO：不安全
                user.setId(response.getData().getId());
                // 登录用户状态存储在CONTEXT 键："current user" 存储 user对象
                // 删除前一个登录状态
                FileUtil.removeShared(ActivityLogin.this, Const.CURRENT_USER.getDesc());
                // 保存当前user
                FileUtil.outputShared(ActivityLogin.this, Const.CURRENT_USER.getDesc(), user);
                MainActivity.mainActivity.finish();
                // 跳转到用户的主界面
                Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
                intent.putExtra(Const.MSG.getDesc(), response.getMsg());
                JumpActivityUtil.jumpActivity(this, intent, 100L, true);
                return;
            }
            ToastUtil.showLongToast(response.getMsg());
        });
    }
}

