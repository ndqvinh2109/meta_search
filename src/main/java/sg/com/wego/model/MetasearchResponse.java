package sg.com.wego.model;

import java.util.List;

public class MetasearchResponse {

    private String generatedId;
    private long offset;
    private List<ScheduleResponse> scheduleResponses;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public List<ScheduleResponse> getScheduleResponses() {
        return scheduleResponses;
    }

    public void setScheduleResponses(List<ScheduleResponse> scheduleResponses) {
        this.scheduleResponses = scheduleResponses;
    }

    public String getGeneratedId() {
        return generatedId;
    }

    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }
}
