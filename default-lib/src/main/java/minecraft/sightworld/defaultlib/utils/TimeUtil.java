package minecraft.sightworld.defaultlib.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

@UtilityClass
public class TimeUtil {
    public String formatTimeElapsed(long startTimeMillis) {
        long elapsedTimeMillis = System.currentTimeMillis() - startTimeMillis;

        long days = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis);
        long hours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis) % 60;

        String result = "";
        if (days > 0) {
            result += days + " " + getNounForm(days, "день", "дня", "дней") + ", ";
        }
        if (hours > 0) {
            result += hours + " " + getNounForm(hours, "час", "часа", "часов") + ", ";
        }
        if (minutes > 0) {
            result += minutes + " " + getNounForm(minutes, "минута", "минуты", "минут") + ", ";
        }
        if (seconds > 0 || result.isEmpty()) {
            result += seconds + " " + getNounForm(seconds, "секунда", "секунды", "секунд");
        }

        return result;
    }

    private String getNounForm(long number, String form1, String form2, String form5) {
        number = Math.abs(number) % 100;
        long remainder = number % 10;
        if (number > 10 && number < 20) {
            return form5;
        }
        if (remainder > 1 && remainder < 5) {
            return form2;
        }
        if (remainder == 1) {
            return form1;
        }
        return form5;
    }

    public void getWeeksBetweenDates() {

    }
}
