package tomdrever.timetable.data;

import java.util.HashMap;
import java.util.Map;

public class PeriodMetaData {
    public PeriodMetaData(String periodName){
        this.periodName = periodName;
        this.periodDescription = new HashMap<>();
    }

    private String periodName;
    public void setName(String name){
        this.periodName = name;
    }
    public String getName(){
        return periodName;
    }

    private Map<String, Boolean> periodDescription;
    public void addDescriptionEntry(String description, Boolean isDisplayedDescription){
        periodDescription.put(description, isDisplayedDescription);
    }
    public void removeDescriptionEntry(String description){
        periodDescription.remove(description);
    }
}
