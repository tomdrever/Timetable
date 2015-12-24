package tomdrever.timetable;

public class PeriodData {
    private String periodName;
    public void setName(String name){
        this.periodName = name;
    }
    public String getName(){
        return periodName;
    }

    private String periodDescription;
    public void setDescription(String description){
        this.periodDescription = description;
    }
    public String getDescription(){
        return periodDescription;
    }

    private String periodSmallprint;
    public void setSmallprint(String smallprint){
        this.periodSmallprint = smallprint;
    }
    public String getSmallprint(){
        return periodSmallprint;
    }
}
