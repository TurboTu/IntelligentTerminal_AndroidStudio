package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 巡检任务列表
 */
public class RoutingPointListBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<RoutingPointBean> TaskPoint;

    public List<RoutingPointBean> getTaskPoint() {
        return TaskPoint;
    }

    public void setTaskPoint(List<RoutingPointBean> taskPoint) {
        TaskPoint = taskPoint;
    }



}
