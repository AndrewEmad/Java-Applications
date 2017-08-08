package defaultPackage;



import java.util.Date;

public class RegistrantController {

    public static boolean authenticate(String name, String password) {
        if (RegistrantDBModel.authenticate(name, password)) {
            return true;
        }
        return false;
    }

    protected static int ValidateInput(String name, Date birthdate, String gender, String mail, String country, String password) {
        String format1 = "^[a-zA-Z0-9\\s]+";//name format
        String format3 = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";//email
        boolean b1 = (name.matches(format1) && (!(name.charAt(0) >= '0' && name.charAt(0) <= '9')));
        boolean b2 = password.length() >= 8;
        boolean b3 = (country.length() > 0);
        boolean b4 = (gender.toUpperCase().equals("MALE") || gender.toUpperCase().equals("FEMALE"));
        boolean b5 = mail.matches(format3);

        if (b1 == false) {
            return 3;
        }
        if (b2 == false) {
            return 4;
        }
        if (b3 == false) {
            return 5;
        }
        if (b4 == false) {
            return 6;
        }
        if (b5 == false) {
            return 7;
        }

        return -1;
    }

}
