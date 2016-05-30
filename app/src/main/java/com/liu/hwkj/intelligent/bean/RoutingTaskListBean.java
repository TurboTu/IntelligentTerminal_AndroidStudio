package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 巡检任务列表
 */
public class RoutingTaskListBean  implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<RoutingTaskBean> NowTaskList;

    private List<RoutingTaskBean> FinishTaskList;
    private List<RoutingTaskBean> NoFinishTaskList;

    public List<RoutingTaskBean> getNoFinishTaskList() {
        return NoFinishTaskList;
    }

    public void setNoFinishTaskList(List<RoutingTaskBean> noFinishTaskList) {
        NoFinishTaskList = noFinishTaskList;
    }


    public List<RoutingTaskBean> getNowTaskList() {
        return NowTaskList;
    }

    public void setNowTaskList(List<RoutingTaskBean> nowTaskList) {
        NowTaskList = nowTaskList;
    }

    public List<RoutingTaskBean> getFinishTaskList() {
        return FinishTaskList;
    }

    public void setFinishTaskList(List<RoutingTaskBean> finishTaskList) {
        FinishTaskList = finishTaskList;
    }

}
