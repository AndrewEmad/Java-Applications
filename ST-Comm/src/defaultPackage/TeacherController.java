package defaultPackage;



import java.util.Date;
//import java.util.Scanner;
import java.util.Vector;

public class TeacherController extends RegistrantController {

    public static boolean verify(String mail) {
        if (TeacherDBModel.setVerified(mail) == true) {
            return true;// requestVerifiedSuccessfully
        }
        return false;//requestNotVerified
    }

    public static boolean authenticate(String name, String password) {
        if (TeacherDBModel.authenticate(name, password)) {
            return true;  //return Teacher to the homepage
        }
        return false;
    }

    public static Vector<String> getPendingRequestList() {
        Vector<String> vector = new Vector<>();
        vector = TeacherDBModel.fetchRequests();
        return vector;    //return RequestList to admin
    }

    public static int createAccount(String name, Date birthdate, String gender, String mail, String country, String password) {
        int validate = ValidateInput(name, birthdate, gender, mail, country, password);
        boolean blacklist = TeacherDBModel.isBlackListed(mail);
        boolean exist = RegistrantDBModel.exists(name, mail);
        if (validate == -1 && blacklist == false && exist == false) {
            Teacher teacher = new Teacher();
            teacher.setInfo(name, birthdate, gender, mail, country, password, false);
            boolean save = TeacherDBModel.saveAccount(teacher);
            if (save == true) {
                return -1;//AccountPendingConfirmation
            } else {
                return 0;//Account not save in db

            }
        }
        if (validate != -1) {
            return validate;
        }
        if (blacklist == true) {
            return 1;
        }
        if (exist == true) {
            return 2;
        }
        return 0; //sorry email blocked , input not validate or this information has an account
    }

}
