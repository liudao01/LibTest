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

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import de.greenrobot.event.EventBus;
import io.mattcarroll.hover.Content;
import io.mattcarroll.hover.content.menus.DoNothingMenuAction;
import io.mattcarroll.hover.content.menus.Menu;
import io.mattcarroll.hover.content.menus.MenuItem;
import io.mattcarroll.hover.content.menus.MenuListContent;
import io.mattcarroll.hover.content.menus.ShowSubmenuMenuAction;
import io.mattcarroll.hover.content.toolbar.ToolbarNavigator;
import lib.carsh.CarshNavigatorContent;
import lib.iputil.IpSwitchNavigatorContent;
import lib.net.HttpNavigatorContent;
import lib.theming.HoverThemeManager;

/**
 * Can create a Hover menu from code or from file.
 * <p>
 * 悬浮窗创建工厂  菜单创建工厂
 */
public class DemoHoverMenuFactory {

    /**
     * Example of how to create a menu in code.
     *
     * @return HoverMenuAdapter
     */
    public DemoHoverMenu createDemoMenuFromCode(@NonNull Context context, @NonNull EventBus bus) throws IOException {
        //二级菜单
        Menu drillDownMenuLevelTwo = new Menu("Demo Menu - Level 2", Arrays.asList(
                new MenuItem(UUID.randomUUID().toString(), "Google", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Amazon", new DoNothingMenuAction())
        ));
        ShowSubmenuMenuAction showLevelTwoMenuAction = new ShowSubmenuMenuAction(drillDownMenuLevelTwo);

        //一级菜单
        Menu drillDownMenu = new Menu("Demo Menu", Arrays.asList(
                new MenuItem(UUID.randomUUID().toString(), "GPS", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Cell Tower Triangulation", new DoNothingMenuAction()),
                new MenuItem(UUID.randomUUID().toString(), "Location Services", showLevelTwoMenuAction)//展示二级菜单
        ));

        MenuListContent drillDownMenuNavigatorContent = new MenuListContent(context, drillDownMenu);

        ToolbarNavigator toolbarNavigatorContent = new ToolbarNavigator(context);
        toolbarNavigatorContent.pushContent(drillDownMenuNavigatorContent);

        Map<String, Content> demoMenu = new LinkedHashMap<>();

        demoMenu.put(DemoHoverMenu.NET, new HttpNavigatorContent(context, Bus.getInstance()));//网络导航
        demoMenu.put(DemoHoverMenu.CARSH_ID, new CarshNavigatorContent(context, Bus.getInstance()));//carsh 导航
        demoMenu.put(DemoHoverMenu.IP_SWITCH, new IpSwitchNavigatorContent(context,Bus.getInstance()));//ip 切换导航
//        demoMenu.put(DemoHoverMenuAdapter.MENU_ID, toolbarNavigatorContent);
//        demoMenu.put(DemoHoverMenuAdapter.PLACEHOLDER_ID, new PlaceholderNavigatorContent(context, bus));

        return new DemoHoverMenu(context, "kitchensink",demoMenu, HoverThemeManager.getInstance().getTheme());
    }

}
