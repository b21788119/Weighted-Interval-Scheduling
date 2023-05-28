public class Assignment implements Comparable
{
    private String name;   //Explanation of the assignment.
    private String start;  // Start time of assignment
    private int duration;  // Total time required to complete the assignment.
    private int importance; // Will be used to calculate the weight of assignment.
    private boolean maellard;

    // Getters
    public String getName() { return this.name;}
    public String getStartTime() { return this.start;}
    public int getDuration() {  return this.duration;}
    public int getImportance() { return this.importance;}
    public boolean isMaellard() { return this.maellard;}
    public String getFinishTime()
    {
        //We will deal with the hours only because duration is an int value.
        String hours = this.start.charAt(0)+""+this.start.charAt(1);
        int convertedHours = Integer.parseInt(hours); //Conversion from string to int.
        int afterDuration = convertedHours+this.duration; //Calculates the finishing hours.
        afterDuration = afterDuration % 24; //After 24,we will reset the hours.
        boolean control = (afterDuration/10) == 0;  // To calculate digit number.
        String minutes = this.start.charAt(3)+""+this.start.charAt(4);
        String finishTime = "";

        if(control)
        {
            //If we have only one digit,we have to have an additional zero while converting.
            finishTime+="0"+String.valueOf(afterDuration)+":"+minutes;
        }
        else
            {
            //We do not need an extra operation here.
            finishTime+=String.valueOf(afterDuration)+":"+minutes;
            }
        return finishTime;
    }
    public double getWeight()
    {
        //  weight = (importance×(maellard ? 1001 : 1)) / duration
        int variable = 1001; // Value depends on the maellard.
        if(!maellard)
        {
            variable = 1;  //If maellard is false,variable value will be 1.
        }
        return (double)(importance * variable) / (double)duration;
    }
    // Override methods
    @Override
    public int compareTo(Object o)
    {   //Function from comparable interface to use sorting methods.
        if(o instanceof Assignment)
        {  //Object o must be an assignment object to be compared.
            Assignment a = (Assignment)(o);
            return getFinishTime().compareTo(a.getFinishTime()); //Comparison of 2 strings.
        }
        return -9999; //If object is not instance of Assignment class.
    }
    @Override
    public String toString()
    {
        //Override method to print an assignment object.
        return "Assignment{name='"+name+"', start='"+start+"', duration="+
                duration+", importance="+importance+", maellard="+maellard
                + ", finish="+getFinishTime()+", weight="+getWeight()+"}";
    }
}
