package eina.unizar.freshtech;

public class GeneradorContraseñas {

    public static String MAYUSCULAS = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";

    public static String MINUSCULAS = "abcdefghijklmnñopqrstuvwxyz";

    public static String NUMEROS = "0123456789";

    public static String ESPECIALES = "!@#$%*";

    public static String getPassword(int length) {
        return getPassword(true, true, true, true, length);
    }

    public static String getPassword(boolean mayus, boolean minus, boolean nums, boolean esp, int length) {
        String key = "";
        if(mayus) key+= MAYUSCULAS;
        if(minus) key+= MINUSCULAS;
        if(nums) key+= NUMEROS;
        if(esp) key+= ESPECIALES;

        String pswd = "";

        for (int i = 0; i < length; i++) {
            pswd+=(key.charAt((int)(Math.random() * key.length())));
        }

        return pswd;
    }
}
