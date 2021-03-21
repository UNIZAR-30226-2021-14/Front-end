package eina.unizar.freshtech;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeguridadContraseña {

    public static int calcularSeguridad(String contraseña, boolean isAtLeast8, boolean hasMayus, boolean hasMinus, boolean hasNums, boolean hasEsp) {
        int porcentajeSegura = 0;
        if (contraseña.length() >= 8) {
            isAtLeast8 = true;
            porcentajeSegura += 20;
        }
        else isAtLeast8 = false;
        Pattern pat = Pattern.compile(".*[A-Z]+.*");
        Matcher mat = pat.matcher(contraseña);
        if (mat.matches()) {
            hasMayus = true;
            porcentajeSegura += 20;
        }
        else hasMayus = false;

        pat = Pattern.compile(".*[a-z]+.*");
        mat = pat.matcher(contraseña);
        if (mat.matches()) {
            hasMinus = true;
            porcentajeSegura += 20;
        }
        else isAtLeast8 = false;

        pat = Pattern.compile(".*[0-9]+.*");
        mat = pat.matcher(contraseña);
        if (mat.matches()) {
            hasNums = true;
            porcentajeSegura += 20;
        }
        else hasNums = false;

        pat = Pattern.compile(".*[!@#$%*]+.*");
        mat = pat.matcher(contraseña);
        if (mat.matches()) {
            hasEsp = true;
            porcentajeSegura += 20;
        }
        else hasEsp = false;

        return porcentajeSegura;
    }

    public static int colorProgressBar(int porcentajeSegura) {
        int color = R.color.Bad;
        if(porcentajeSegura == 20) color = R.color.Bad;
        if(porcentajeSegura == 40) color = R.color.BadAverage;
        if(porcentajeSegura == 60) color = R.color.Average;
        if(porcentajeSegura == 80) color = R.color.AverageGood;
        if(porcentajeSegura == 100) color = R.color.Good;
        return color;
    }
}
