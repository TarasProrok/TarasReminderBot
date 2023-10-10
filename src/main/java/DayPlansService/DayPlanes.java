package DayPlansService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class DayPlanes {

    private DayPlanes() {
    }

    public static String getDay(DayOfWeek dayOfWeek) {
        return DayTranslator.getUkrDay(dayOfWeek);
    }

    static String delim = "----------------------\n";
    static String max = "\uD83D\uDC66\uD83C\uDFFC <b>Макс:</b>\n";
    static String anna = "\uD83D\uDC67\uD83C\uDFFB <b>Анюта:</b>\n";
    static String schoolSixLessons = "🏫 Школа - 14:10\n";
    static String schoolSevenLessons = "🏫 Школа - 15:05\n";
    static String schoolEightLessons = "🏫 Школа - 16:00\n";
    static String eng = "🇬🇧 Англійська - ";
    static String pool = "\uD83C\uDFCA\u200D♂️ Басейн - 17:30-19:45\n";
    static String dances = "\uD83D\uDC83\uD83C\uDFFB Танці - ";
    static String germ = "\uD83C\uDDE9\uD83C\uDDEA Німецька - ";
    static String dayTitle;

    public static String getDayPlanes(DayOfWeek dayOfWeek) {
        String dayPlanes = "";

        if (dayOfWeek.equals(DayOfWeek.from(LocalDateTime.now()))) {
            dayTitle = "<i>Плани на " + getDay(dayOfWeek) + ":</i>\n";
        } else {
            dayTitle = "<i>Нагадую\nплани на завтра, " + getDay(dayOfWeek) + ":</i>\n";
        }
        switch (dayOfWeek) {
            case MONDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                schoolSevenLessons +
                                pool +
                                delim +
                                anna +
                                schoolSevenLessons +
                                delim;
                break;
            case TUESDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                schoolSevenLessons +
                                eng + "16:00-17:00\n" +
                                pool +
                                delim +
                                anna +
                                schoolSevenLessons +
                                dances + "19:00-20:30\n(Текстильник)\n" +
                                delim;
                break;
            case WEDNESDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                schoolSixLessons +
                                germ + "15:00-16:00\n" +
                                pool +
                                delim +
                                anna +
                                schoolEightLessons +
                                eng + "17:00-18:00\n" +
                                delim;
                break;
            case THURSDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                schoolSevenLessons +
                                eng + "16:00-17:00\n" +
                                pool +
                                delim +
                                anna +
                                schoolEightLessons +
                                dances + "17:30-19:00\n(Коновальця)\n" +
                                delim;
                break;
            case FRIDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                schoolSevenLessons +
                                pool +
                                delim +
                                anna +
                                schoolSixLessons +
                                dances + "19:00-20:30\n(Текстильник)\n" +
                                delim;
                break;
            case SATURDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                max +
                                "\uD83C\uDFCA\u200D♂️ Басейн - 11:00-12:45\n" +
                                delim +
                                anna +
                                eng + "10:45-11:45\n" +
                                germ + "12:00-13:00\n" +
                                delim;
                break;
            case SUNDAY:
                dayPlanes =
                        dayTitle +
                                delim +
                                "<b>НЕДІЛЯ!\nВИХІДНИЙ!</b>\n" +
                                delim;
                break;
            default:
                dayPlanes = "Ой-ой!\nСталася помилка!\nВиправлю як найшвидше!";
        }
        return dayPlanes;
    }
}
