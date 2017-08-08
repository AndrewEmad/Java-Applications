package defaultPackage;



import java.util.Date;

public class StudentController extends RegistrantController {

    public static int createAccount(String name, Date birthdate, String gender, String mail, String country, String password) {
        int validate = ValidateInput(name, birthdate, gender, mail, country, password);
        boolean blacklist = StudentDBModel.isBlackListed(mail);
        boolean exist = RegistrantDBModel.exists(name, mail);
        if (validate == -1 && blacklist == false && exist == false) {
            Student student = new Student();
            student.setInfo(name, birthdate, gender, mail, country, password, 0, "");
            boolean save = StudentDBModel.saveAccount(student);
            if (save == true) {
                return -1;//AccountCreatedSuccessfully
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
        return 0;              //Account Not Created as validation , exists or balcklisted
    }

    public static boolean saveScore(String name, int score, String gameName) {
        if (StudentDBModel.saveScore(name, score, gameName) == true) {
            return true;
        }
        return false;
    }

    public static boolean authenticate(String name, String password) {

        if (StudentDBModel.authenticate(name, password)) {
            return true;  //return Student to the homepage
        }
        return false;
    }

}
