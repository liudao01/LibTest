/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.os.Build;
import android.view.ContextThemeWrapper;

import java.io.IOException;

import httploglib.lib.R;
import io.mattcarroll.hover.HoverMenu;
import io.mattcarroll.hover.HoverView;
import io.mattcarroll.hover.window.HoverMenuService;
import lib.theming.HoverTheme;

/**
 * Demo {@link HoverMenuService}.
 *
 * 服务
 *
 */
public class DemoHoverMenuService extends HoverMenuService {

    private static final String TAG = "DemoHoverMenuService";
    private Notification notification;

    public static void showFloatingMenu(Context context) {
        Intent intent = new Intent(context, DemoHoverMenuService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    private DemoHoverMenu mDemoHoverMenu;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel("1","name",
                NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        notification = new Notification.Builder(getApplicationContext(),"1").build();
        startForeground(1, notification);



        Bus.getInstance().register(this);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        Bus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    protected Context getContextForHoverMenu() {
        return new ContextThemeWrapper(this, R.style.AppTheme);
    }

    @Override
    protected void onHoverMenuLaunched(@NonNull Intent intent, @NonNull HoverView hoverView) {
        hoverView.setMenu(createHoverMenu());
        hoverView.collapse();
    }

    private HoverMenu createHoverMenu() {
        try {
            mDemoHoverMenu = new DemoHoverMenuFactory().createDemoMenuFromCode(getContextForHoverMenu(), Bus.getInstance());
//            mDemoHoverMenuAdapter = new DemoHoverMenuFactory().createDemoMenuFromFile(getContextForHoverMenu());
            return mDemoHoverMenu;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onEventMainThread(@NonNull HoverTheme newTheme) {
        mDemoHoverMenu.setTheme(newTheme);
    }

}
