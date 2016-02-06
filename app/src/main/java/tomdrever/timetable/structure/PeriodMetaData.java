package tomdrever.timetable.structure;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PeriodMetaData {
    public PeriodMetaData(String periodName){
        this.periodName = periodName;
        this.periodDescription = HashBiMap.create();
    }

    private String periodName;
    public void setName(String name){
        this.periodName = name;
    }
    public String getName(){
        return periodName;
    }

    private BiMap<String, Boolean> periodDescription;
    public void addDescriptionEntry(String description, Boolean isDisplayedDescription){
        periodDescription.put(description, isDisplayedDescription);
    }
    public void removeDescriptionEntry(String description){
        periodDescription.remove(description);
    }
}
