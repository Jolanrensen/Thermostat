package nl.tue.student.thermostat;

/**
 * Created by s154563 on 11-6-2016.
 */
public class Time {
    int minutes = 0;
    int hours = 0;
    int days = 0; //day 0 is monday

    public void setTime(int days, int hours, int minutes) {
        days = this.days;
        hours = this.hours;
        minutes = this.hours;
    }

    public void setTime(String day, String time) {
        switch (day) {
            case "Monday":
                days = 0;
                break;
            case "Tuesday":
                days = 1;
                break;
            case "Wednesday":
                days = 2;
                break;
            case "Thursday":
                days = 3;
                break;
            case "Friday":
                days = 4;
                break;
            case "Saturday":
                days = 5;
                break;
            case "Sunday":
                days = 6;
                break;
        }
        String[] timeSplit = time.split(":");
        hours = Integer.parseInt(timeSplit[0]);
        minutes = Integer.parseInt(timeSplit[1]);
    }

    public void increaseTime() {
        minutes++;
        if (minutes == 60) {
            minutes = 0;
            hours++;
        }
        if (hours == 24) {
            hours = 0;
            days++;
        }
        if (days == 7) {
            days = 0;
        }
    }

    public String getMinutesString() {
        String minutesS = Integer.toString(minutes);
        if (minutesS.length() == 1) {
            minutesS = "0" + minutesS;
        }
        return minutesS;
    }

    public String getHoursString() {
        String hoursS = Integer.toString(hours);
        if (hoursS.length() == 1) {
            hoursS = "0" + hoursS;
        }
        return hoursS;
    }

    public String getDaysString() {
        switch (days) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
            default:
                return "";
        }
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getDays() {
        return days;
    }
}
