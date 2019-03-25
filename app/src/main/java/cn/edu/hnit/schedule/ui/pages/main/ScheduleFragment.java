package cn.edu.hnit.schedule.ui.pages.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import cn.edu.hnit.schedule.custom.CourseView;
import cn.edu.hnit.schedule.custom.MyDialog;
import cn.edu.hnit.schedule.R;
import cn.edu.hnit.schedule.custom.MyFragment;
import cn.edu.hnit.schedule.databinding.FragmentScheduleBinding;
import cn.edu.hnit.schedule.model.Course;
import cn.edu.hnit.schedule.repository.DateRepository;
import cn.edu.hnit.schedule.repository.SettingRepository;
import cn.edu.hnit.schedule.ui.controller.CourseController;
import cn.edu.hnit.schedule.ui.pages.info.CourseInfoActivity;

import static android.view.View.inflate;

public class ScheduleFragment extends MyFragment {

    public int week;
    private CourseController courseController;
    private FragmentScheduleBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false);
        courseController = new CourseController(this);
        if (this.week == new DateRepository(getContext()).getCurrentWeek()) {
            markWeekDay();
        }
        initTextColor();
        loadCourses();
        return mBinding.getRoot();
    }

    //设置周数
    public void setWeek(int week) {
        this.week = week;
    }

    //标记星期
    private void markWeekDay() {
        Date d = new Date();
        SimpleDateFormat weekFormat = new SimpleDateFormat("E", Locale.CHINA);
        int bg = getGray();
        switch (weekFormat.format(d)) {
            case "周日":
                mBinding.cvSun.setCardBackgroundColor(bg);
                break;
            case "周一":
                mBinding.cvMon.setCardBackgroundColor(bg);
                break;
            case "周二":
                mBinding.cvTues.setCardBackgroundColor(bg);
                break;
            case "周三":
                mBinding.cvWed.setCardBackgroundColor(bg);
                break;
            case "周四":
                mBinding.cvThur.setCardBackgroundColor(bg);
                break;
            case "周五":
                mBinding.cvFri.setCardBackgroundColor(bg);
                break;
            case "周六":
                mBinding.cvSat.setCardBackgroundColor(bg);
                break;
        }
    }

    //初始化字体颜色
    private void initTextColor() {
        mBinding.setTextColor(getContentTextColor());
    }

    //加载课程
    private void loadCourses() {
        for (CourseView course: courseController.getCourses_()) {
            mBinding.schedule.addView(course);
            addOnClickCourseEvent(course);
        }
        for (CourseView course: courseController.getCourses()) {
            mBinding.schedule.addView(course);
            addOnClickCourseEvent(course);
        }
    }

    //添加点击事件
    private void addOnClickCourseEvent(CourseView view) {
        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), CourseInfoActivity.class);
            intent.putExtra("id", view.getId());
            startActivity(intent);
        });
    }

    @SuppressLint("WrongConstant")
    public void refreshUi() {
        Intent intent = new Intent("REFRESH_UI");
        intent.addFlags(0x01000000);
        if (getActivity() != null) {
            getActivity().sendBroadcast(intent);
        }
    }

}
