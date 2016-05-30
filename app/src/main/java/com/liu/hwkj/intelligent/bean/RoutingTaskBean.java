package com.liu.hwkj.intelligent.bean;

import java.io.Serializable;

/**
 * 巡检任务
 */
public class RoutingTaskBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ID = ""; //任务ID
	private String TID = ""; //保留ID
	private String TaskName = ""; //巡检任务名称
	private String TaskType = ""; //任务类型代码
	private String TaskContent = ""; //巡检内容
	private String TaskDay = ""; //任务日期，保留
    private String BegTime = ""; //任务开始时间
    private String EndTime = ""; //任务结束时间
	private String TaskBegTime = ""; //任务开始时间
	private String TaskEndTime = ""; //任务结束时间
	private String PointNum = ""; //巡检点数量
	private String MapLine = ""; //路径坐标集，字符串格式，用|分割
	private String MapArea = ""; //电子围栏坐标集，字符串格式，用|分割
	private String UserID = ""; //巡检员ID
	private String User_Name = ""; //巡检员姓名
	private String HasInspection = ""; //已巡检的巡检点数量
	private String CreateTime = ""; //任务添加时间
    private String DelMark = ""; //
    private String InTime = ""; //入场时间
    private String OutTime = ""; //出场时间
    private String IsDayTask = ""; //日巡检任务与临时任务标识，1为日常巡检任务
    private String Organization_Name = ""; //巡检员所属班组

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTID() {
        return TID;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getTaskContent() {
        return TaskContent;
    }

    public void setTaskContent(String taskContent) {
        TaskContent = taskContent;
    }

    public String getTaskDay() {
        return TaskDay;
    }

    public void setTaskDay(String taskDay) {
        TaskDay = taskDay;
    }

    public String getBegTime() {
        return BegTime;
    }

    public void setBegTime(String begTime) {
        BegTime = begTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getTaskBegTime() {
        return TaskBegTime;
    }

    public void setTaskBegTime(String taskBegTime) {
        TaskBegTime = taskBegTime;
    }

    public String getTaskEndTime() {
        return TaskEndTime;
    }

    public void setTaskEndTime(String taskEndTime) {
        TaskEndTime = taskEndTime;
    }

    public String getPointNum() {
        return PointNum;
    }

    public void setPointNum(String pointNum) {
        PointNum = pointNum;
    }

    public String getMapLine() {
        return MapLine;
    }

    public void setMapLine(String mapLine) {
        MapLine = mapLine;
    }

    public String getMapArea() {
        return MapArea;
    }

    public void setMapArea(String mapArea) {
        MapArea = mapArea;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public String getHasInspection() {
        return HasInspection;
    }

    public void setHasInspection(String hasInspection) {
        HasInspection = hasInspection;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getDelMark() {
        return DelMark;
    }

    public void setDelMark(String delMark) {
        DelMark = delMark;
    }

    public String getInTime() {
        return InTime;
    }

    public void setInTime(String inTime) {
        InTime = inTime;
    }

    public String getOutTime() {
        return OutTime;
    }

    public void setOutTime(String outTime) {
        OutTime = outTime;
    }

    public String getIsDayTask() {
        return IsDayTask;
    }

    public void setIsDayTask(String isDayTask) {
        IsDayTask = isDayTask;
    }

    public String getOrganization_Name() {
        return Organization_Name;
    }

    public void setOrganization_Name(String organization_Name) {
        Organization_Name = organization_Name;
    }


}
