package cn.edu.hnit.schedule.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.arch.lifecycle.AndroidViewModel;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import cn.edu.hnit.schedule.repository.SettingRepository;
import cn.edu.hnit.schedule.service.KeepAliveService;

public class SettingListViewModel extends AndroidViewModel {

    private SettingRepository mRepository;

    public SettingListViewModel(@NonNull Application application) {
        super(application);
        mRepository = new SettingRepository(application);
    }

    //暗色主题
    public boolean getDarkThemeOption() {
        return mRepository.getSwitchOption("ui_dark_theme");
    }

    public void setDarkThemeOption(boolean bool) {
        mRepository.setSwitchOption("ui_dark_theme", bool);
        refreshUi();
    }

    //不显示非本周的课程
    public boolean getNotCurrentWeekOption() {
        return mRepository.getSwitchOption("ui_not_current_week");
    }

    public void setNotCurrentWeekOption(boolean bool) {
        mRepository.setSwitchOption("ui_not_current_week", bool);
        refreshUi();
    }

    //字体
    public int getTextSizeOption() {
        return mRepository.getSeekBarOption("ui_text_size") - 10;
    }

    public String getTextSize() {
        return mRepository.getSeekBarOption("ui_text_size") + "sp";
    }

    public void setTextSizeOption(int num) {
        mRepository.setSeekBarOption("ui_text_size", num);
        refreshUi();
    }

    //小部件右侧课程状态
    public boolean getWidgetStatusOption() {
        return mRepository.getSwitchOption("widget_course_status");
    }

    public void setWidgetStatusOption(boolean bool) {
        mRepository.setSwitchOption("widget_course_status", bool);
        updateWidget();
        if (!bool) {
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getApplication(), KeepAliveService.class));
            builder.setMinimumLatency(10 * 1000);
            builder.setOverrideDeadline(30 * 1000);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            JobScheduler scheduler = (JobScheduler) getApplication().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        }
    }

    //小部件显示日期
    public boolean getWidgetDateOption() {
        return mRepository.getSwitchOption("widget_date");
    }

    public void setWidgetDateOption(boolean bool) {
        mRepository.setSwitchOption("widget_date", bool);
        updateWidget();
    }

    //小部件跳转
    public boolean getWidgetJumpOption() {
        return mRepository.getSwitchOption("widget_jump");
    }

    public void setWidgetJumpOption(boolean bool) {
        mRepository.setSwitchOption("widget_jump", bool);
        updateWidget();
    }

    //从备份服务器同步
    public boolean getSyncOption() {
        return mRepository.getSwitchOption("sync_from_backup_server");
    }

    public void setSyncOption(boolean bool) {
        mRepository.setSwitchOption("sync_from_backup_server", bool);
    }

    //自动更新
    public boolean getAppUpdateOption() {
        return mRepository.getSwitchOption("app_update");
    }

    public void setAppUpdateOption(boolean bool) {
        mRepository.setSwitchOption("app_update", bool);
    }

    //刷新小部件
    @SuppressLint("WrongConstant")
    private void updateWidget() {
        Intent intent = new Intent("WIDGET_UPDATE");
        intent.addFlags(0x01000000);
        getApplication().sendBroadcast(intent);
    }

    @SuppressLint("WrongConstant")
    private void refreshUi() {
        Intent intent = new Intent("REFRESH_UI");
        intent.addFlags(0x01000000);
        getApplication().sendBroadcast(intent);
    }

}
