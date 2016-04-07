package com.atide.bim.ui.message;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atide.bim.MyApplication;
import com.atide.bim.R;
import com.atide.bim.actionbar.MainActionBarActivity;
import com.atide.bim.entity.MessageEntity;
import com.atide.bim.entity.User;
import com.atide.bim.entity.UserEntity;
import com.atide.bim.helper.RequestMessageHelper;
import com.atide.bim.helper.SaveMarkHelper;
import com.atide.bim.helper.UserLabelViewHelper;
import com.atide.bim.request.DrawingMarkServiceRequest;
import com.atide.bim.ui.popup.HistoryVistorPopup;
import com.atide.bim.ui.popup.MessageChoiceUserPopup;
import com.atide.bim.utils.Utils;
import com.atide.bim.view.LabelGroup;
import com.atide.utils.net.webservice.WsRequest;
import com.atide.utils.net.webservice.WsResponseMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by atide on 2016/4/1.
 */
@EActivity(R.layout.send_message_layout)
public class SendMessageActivity extends MainActionBarActivity {
    private Context mContext;
    private ArrayList<UserEntity> userEntities;
    private ArrayList<MessageEntity> messageEntities;
    private HashMap<String,String> currentTheme;
    private ArrayList<UserEntity> currentUserEntities;
    private UserLabelViewHelper userLabelViewHelper;
    @Extra
    String imageKey;
    @Bean
    RequestMessageHelper requestMessageHelper;
    @Bean
    MessageAdapter adapter;
    @ViewById(R.id.message)
    ListView listView;
   @ViewById(R.id.userGroup)
    LabelGroup userGroup;
    @ViewById(R.id.text)
    EditText messageContent;
    @ViewById
    ProgressBar loadingBar;

    @Click(R.id.addUser)
    void choiceUsers(final View view){

        if (userEntities != null && userEntities.size()>0){
            new MessageChoiceUserPopup(mContext).setContentChangeListener(usersCheckedChangeListener).setContent(userEntities, currentUserEntities).showPopupWindow(view);
            return;
        }
        loadingBar.setVisibility(View.VISIBLE);
        requestMessageHelper.getSectRelevantUnitUser(new RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                loadingBar.setVisibility(View.GONE);
                try {
                    String result = new JSONObject(msg).optString("data", "");
                    Gson gson = new Gson();

                    Type type = new TypeToken<ArrayList<UserEntity>>() {
                    }.getType();

                    userEntities = gson.fromJson(result, type);
                    new MessageChoiceUserPopup(mContext).setContentChangeListener(usersCheckedChangeListener).setContent(userEntities, currentUserEntities).showPopupWindow(view);
                }catch (Exception e){
                    e.printStackTrace();
                    Utils.showMsg("请求出现错误，请联系管理员！");
                }
            }

            @Override
            public void fail(String msg) {
                loadingBar.setVisibility(View.GONE);
                Utils.showMsg(msg);
            }
        });
    }

    @Click(R.id.send)
    void sendMessage(){
        String content = messageContent.getText().toString();
        if (content.equals("")){
            Utils.showMsg("消息体不能为空！");
            return;
        }
        messageContent.setText("");
        requestMessageHelper.sendMessage(new RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                requestMessage();
            }

            @Override
            public void fail(String msg) {
                Utils.showMsg(msg);
            }
        }, content);
    }

    @ItemClick(R.id.message)
    void itemClick(final MessageEntity messageEntity){
        new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setIcon(android.R.drawable.stat_sys_warning)
                .setMessage("是否删除此消息？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageEntities.remove(messageEntity);
                        adapter.reload(messageEntities);
                        requestMessageHelper.deleteMessage(messageEntity.getMessageid());
                        dialog.dismiss();

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @AfterViews
    void initUI(){
        mContext = this;
        requestMessageHelper.setImageKey(imageKey);
        if (MyApplication.getInstance().getThemeDatas()==null || MyApplication.getInstance().getThemeDatas().size()<1){
            Toast.makeText(mContext,"主题获取失败！",Toast.LENGTH_LONG).show();
            return;
        }
        userLabelViewHelper = new UserLabelViewHelper(userGroup);
        currentTheme = MyApplication.getInstance().getThemeDatas().get(0);
        requestMessageHelper.setThemeId(currentTheme.get("id"));
        listView.setAdapter(adapter);
        requestMessage();
        requestCurrentUser();

    }

    @Override
    public String fetchTitle() {
        if (MyApplication.getInstance().getThemeDatas()==null || MyApplication.getInstance().getThemeDatas().size()<1){
            return "";
        }
        return MyApplication.getInstance().getThemeDatas().get(0).get("name");
    }

    @Override
    public Activity fetchClass() {
        return this;
    }

    @Override
    public void setRightButtonOnClickListener() {
        rightButton.setText("切换");
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HistoryVistorPopup(mContext).setListener(new ThemeChangeListener() {
                    @Override
                    public void themeChange(HashMap<String, String> theme) {
                        if (theme == currentTheme) {
                            return;
                        }
                        currentTheme = theme;
                        requestMessageHelper.setThemeId(currentTheme.get("id"));
                        titleTextView.setText(currentTheme.get("name"));
                        adapter.reload(null);
                        requestMessage();
                        requestCurrentUser();
                    }
                }).setIsThemeData(true).showPopupWindow(v);
            }
        });
        rightButton1.setVisibility(View.VISIBLE);
        rightButton1.setText("刷新");
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestMessage();
            }
        });
    }



    private void addUsers(ArrayList<UserEntity> userEntities){
        if (userEntities == null || userEntities.size()<1)
            return;
        if (currentUserEntities == null)
            currentUserEntities = new ArrayList<>();
        for (UserEntity entity : userEntities){
            if (!currentUserEntities.contains(entity))
            {
                currentUserEntities.add(entity);
                userLabelViewHelper.addUser(entity);
            }
        }

    }

    /**
     * 获取当前下的消息
     *
     */
    private void requestMessage(){

        loadingBar.setVisibility(View.VISIBLE);
        requestMessageHelper.requestMessage(new RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                loadingBar.setVisibility(View.GONE);
                try {
                    String result = new JSONObject(msg).optString("data", "");
                    Gson gson = new Gson();

                    Type type = new TypeToken<ArrayList<MessageEntity>>() {
                    }.getType();

                    messageEntities = gson.fromJson(result, type);
                    adapter.reload(messageEntities);
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                    Utils.showMsg("请求出现错误，请联系管理员！");
                }
                adapter.reload(null);
            }

            @Override
            public void fail(String msg) {
                loadingBar.setVisibility(View.GONE);
                adapter.reload(null);
                Utils.showMsg(msg);
            }
        });
    }

    /**
     * 获取当前主题下的用户
     */
    private void requestCurrentUser(){
        requestMessageHelper.requestGetUsers(new RequestOnFinishListener() {
            @Override
            public void finish(String msg) {
                try {
                    String result = new JSONObject(msg).optString("data", "");
                    Gson gson = new Gson();

                    Type type = new TypeToken<ArrayList<UserEntity>>() {
                    }.getType();
                    ArrayList<UserEntity> tmp = gson.fromJson(result,type);
                    if (currentUserEntities != null)
                        currentUserEntities.clear();
                    userGroup.removeAllViews();
                    addUsers(tmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String msg) {
            }
        });
    }

    //处理选中用户，删除用户功能
    private UsersCheckedChangeListener usersCheckedChangeListener = new UsersCheckedChangeListener() {
        @Override
        public void checked(HashMap<Integer, Boolean> checked) {
            String userIds = "";
            ArrayList<UserEntity> tmpUserEntities = new ArrayList<>();
            for (Map.Entry<Integer,Boolean> entry : checked.entrySet()){
                if (entry.getValue()){
                    tmpUserEntities.add(userEntities.get(entry.getKey()));
                    userIds += userEntities.get(entry.getKey()).getUserid() + ",";
                }
            }
            addUsers(tmpUserEntities);
            if (userIds.length()>0) {
                userIds = userIds.substring(0, userIds.length() - 1);
                requestMessageHelper.requestAddUsers(null, userIds);
                if (currentUserEntities == null) {

                    return;
                }
            }
            userIds = "";
            Iterator<UserEntity> iterator = currentUserEntities.iterator();
            while (iterator.hasNext()){
                UserEntity entity = iterator.next();
                if (!tmpUserEntities.contains(entity)){
                    if(userLabelViewHelper.deleteUser(entity)){
                        iterator.remove();
                        userIds += entity.getUserid() + ",";
                    }
                }
            }
            if (userIds.length()<1)
                return;
            userIds = userIds.substring(0,userIds.length()-1);
            requestMessageHelper.requestDeleteUsers(null, userIds);
        }
    };

    public static interface ThemeChangeListener{
        void themeChange(HashMap<String,String> theme);
    }

    public static interface UsersCheckedChangeListener{
        void checked(HashMap<Integer, Boolean> checked);
    }

    public static interface RequestOnFinishListener{
        void finish(String msg);
        void fail(String msg);
    }

}
