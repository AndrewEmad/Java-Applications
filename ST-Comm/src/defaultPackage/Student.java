package defaultPackage;



import java.util.Date;

public class Student extends Registrant {

    private int score;
    private String badge;

    public Student() {
        score = 0;
        badge = "";
    }

    public void setInfo(String name, Date birthdate, String gender, String mail, String country, String password, int score, String badge) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.mail = mail;
        this.country = country;
        this.password = password;
        this.score = score;
        this.badge = badge;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
//	public static void main(String[]args)
//	{
//		Student t=new Student();
//		t.setInfo("mariam",null,"Female","mariam@gmail.com","Egypt","12345678kfdfh",24,"specialist");
//		System.out.println(t.getName());
//		System.out.println(t.getBirthdate());
//		System.out.println(t.getGender());
//		System.out.println(t.getMail());
//		System.out.println(t.getCountry());
//		System.out.println(t.getPassword());
//		System.out.println(t.getScore());
//		System.out.println(t.getBadge());
//		
//	}

}
