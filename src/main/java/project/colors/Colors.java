package project.colors;

import org.fusesource.jansi.Ansi;

public class Colors {
    public static String paintRed(String str) { return Ansi.ansi().fg(Ansi.Color.RED).a(str).reset().toString(); }

    public static String paintWhite(String str) {
        return Ansi.ansi().fg(Ansi.Color.WHITE).a(str).reset().toString();
    }

    public static String paintYellow(String str) {
        return Ansi.ansi().fg(Ansi.Color.YELLOW).a(str).reset().toString();
    }

    public static String paintGreen(String str) {
        return Ansi.ansi().fg(Ansi.Color.GREEN).a(str).reset().toString();
    }

    public static String paintCyan(String str) {
        return Ansi.ansi().fg(Ansi.Color.CYAN).a(str).reset().toString();
    }
}
