package com.platform.demo.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.platform.app.config.PlatformType;
import com.platform.app.utils.PayObservable;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.platform.sdk.wx.WXApi;

/**
 * 微信支付的回调，除了支付回调时在这个activity  其余的都在WXEntryActivity
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXApi.getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WXApi.getWXAPI().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            /*
              0 支付成功
             -1 发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
             -2 用户取消 发生场景：用户不支付了，点击取消，返回APP。
            */
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                PayObservable.getInstance().publishSuccess(PlatformType.WEIXIN, "支付成功");
            } else {
                PayObservable.getInstance().publishFailed(PlatformType.WEIXIN, "支付失败");
            }
        }
        finish();
    }
}