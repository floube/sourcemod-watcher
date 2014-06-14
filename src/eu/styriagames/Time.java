package eu.styriagames;

public class Time {
    public int hour, minute, second;
    
    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    public Time(long millis) {
        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(millis));
        
        String[] splitted = date.split(" ")[1].split(":");
        
        this.hour = Integer.parseInt(splitted[0]);
        this.minute = Integer.parseInt(splitted[1]);
        this.second = Integer.parseInt(splitted[2]);
    }
    
    public Time(String time, String split) {
        String[] splitted = time.split(split);
        
        this.hour = Integer.parseInt(splitted[0]);
        this.minute = Integer.parseInt(splitted[1]);
        this.second = Integer.parseInt(splitted[2]);
    }
    
    public boolean after(Time ref) {
        int secs = this.hour * 60 * 60 + this.minute * 60 + this.second;
        int secsRef = ref.hour * 60 * 60 + ref.minute * 60 + ref.second;
        
        return secs > secsRef;
    }
    
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
