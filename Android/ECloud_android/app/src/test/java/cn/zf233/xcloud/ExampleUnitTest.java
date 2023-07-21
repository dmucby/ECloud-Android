package cn.zf233.xcloud;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Intent;

import java.util.List;

import cn.zf233.xcloud.activity.ActivityHome;
import cn.zf233.xcloud.activity.ActivityLogin;
import cn.zf233.xcloud.activity.MainActivity;
import cn.zf233.xcloud.common.BaseResponse;
import cn.zf233.xcloud.common.Const;
import cn.zf233.xcloud.entity.File;
import cn.zf233.xcloud.entity.User;
import cn.zf233.xcloud.service.UserService;
import cn.zf233.xcloud.service.impl.UserServiceImpl;
import cn.zf233.xcloud.util.JumpActivityUtil;
import cn.zf233.xcloud.util.RequestUtil;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    private final UserService userService = new UserServiceImpl();

    /**
     * test okhttp
     */
    @Test
    public void test(){

        String url = "http://localhost:8089/xcloud/test";
//        RequestBody requestBody = new RequestBody();
        String url2 = "test";
        // 创建一个匿名内部类继承 TypeToken，并指定具体的泛型类型
//        TypeToken<ServerResponse<User>> typeToken = new TypeToken<ServerResponse<User>>() {};

        try {
            OkHttpClient client = new OkHttpClient();
            FormBody body = new FormBody.Builder()
                    .add("url2", url2)
                    .build();
            Request request = new Request.Builder()
                    .url(url).post(body)
                    .build();
            Response response;
            response = client.newCall(request).execute();
            String jsonRes = response.body().string();
            System.out.println(jsonRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testRegist(){
        User user = new User();

        String username = "cai1234566";
        String password = "83254729834792";

        user.setUsername(username);
        user.setPassword(password);
        String code = "12345";
        userService.regist(RequestUtil.getRequestUtil(), user, code);
    }

    @Test
    public void testLogin(){
        User user = new User();

        String username = "caicaicai";
        String password = "123455";

        user.setUsername(username);
        user.setPassword(password);

        BaseResponse<User> response = userService.login(RequestUtil.getRequestUtil(), user);
        System.out.println("11111--------------" + response.isSuccess());
        if (response.isSuccess()) {
            // 从返回的登陆数据获取用户的详细数据
            // TODO：不安全
            user.setId(response.getData().getId());
            System.out.println(user.getId());
            // 登录用户状态存储在CONTEXT 键："current user" 存储 user对象
            // 删除前一个登录状态
//            FileUtil.removeShared(ActivityLogin.this, Const.CURRENT_USER.getDesc());
//            // 保存当前user
//            FileUtil.outputShared(ActivityLogin.this, Const.CURRENT_USER.getDesc(), user);
//            MainActivity.mainActivity.finish();
//            // 跳转到用户的主界面
//            Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
//            intent.putExtra(Const.MSG.getDesc(), response.getMsg());
//            JumpActivityUtil.jumpActivity(this, intent, 100L, true);
            return;
        }
    }

    @Test
    public void testHome(){
        User user = new User();

        String username = "liu2";
        int parentId = -1;
        Integer id = 111;

        user.setUsername(username);
        user.setId(id);

        BaseResponse<List<File>> response = userService.home(RequestUtil.getRequestUtil(), user, parentId);

        System.out.println(response.getData());
        System.out.println(response.getAbsolutePath());
    }
}