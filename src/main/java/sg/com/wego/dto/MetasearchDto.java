package sg.com.wego.dto;

import java.util.List;

public class MetasearchDto {

    private long offset;
    private List<ScheduleDto> scheduleDtoList;

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public List<ScheduleDto> getScheduleDtoList() {
        return scheduleDtoList;
    }

    public void setScheduleDtoList(List<ScheduleDto> scheduleDtoList) {
        this.scheduleDtoList = scheduleDtoList;
    }

}
