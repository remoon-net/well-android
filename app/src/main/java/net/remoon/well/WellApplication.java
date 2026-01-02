package net.remoon.well;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import net.remoon.well.cmd.Cmd;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;

class WellMobileVPN implements net.remoon.well.cmd.MobileVPN {

    public Context ctx;

    WellMobileVPN(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void start() {
        Intent vpn = new Intent(ctx, WellVPNService.class);
        ctx.startService(vpn);
    }
}

public class WellApplication extends Application {

    public static String listenAddr;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("WellApplication", "App 启动，全局初始化");
        // 这里初始化 gomobile 或其他全局库
        File filesDir = getFilesDir();
        String path = filesDir.getAbsolutePath();
        path = path+"/pb_data";
        String[] args={"--dev","serve","--dir",path};
        String argsStr = "";
        try {
            argsStr = new JSONArray(args).toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        WellMobileVPN mvpn = new WellMobileVPN(getApplicationContext());
        Cmd.setMobileVPN(mvpn);
        listenAddr = Cmd.main(argsStr);
        Log.d("WellApplication", "还能继续走下去吗");
    }
}
