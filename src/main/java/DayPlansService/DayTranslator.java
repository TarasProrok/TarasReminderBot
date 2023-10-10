package DayPlansService;

import java.time.DayOfWeek;

public class DayTranslator {

    private DayTranslator() {
    }

    public static String getUkrDay(DayOfWeek dayOfWeek) {

        String ukrDay = "";

        switch (dayOfWeek) {
            case MONDAY:
                ukrDay = "Понеділок";
                break;
            case TUESDAY:
                ukrDay = "Вівторок";
                break;
            case WEDNESDAY:
                ukrDay = "Середу";
                break;
            case THURSDAY:
                ukrDay = "Четвер";
                break;
            case FRIDAY:
                ukrDay = "П'ятницю";
                break;
            case SATURDAY:
                ukrDay = "Суботу";
                break;
            case SUNDAY:
                ukrDay = "Неділю";
                break;
        }
        return ukrDay;
    }
}
