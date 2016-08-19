package Function;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;

/**
 * Created by olleh on 2016-08-19.
 */
public class isApplicationSentToBackground {

    public static boolean isApplicationSentToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

}
