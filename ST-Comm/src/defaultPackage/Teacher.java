package defaultPackage;



import java.util.Date;

public class Teacher extends Registrant {

    private boolean verified;

    public Teacher() {
        verified = false;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setInfo(String name, Date birthdate, String gender, String mail, String country, String password, boolean verified) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.country = country;
        this.mail = mail;
        this.password = password;
        this.verified = verified;
    }
//	public static void main(String[]args)
//	{
//		Teacher t=new Teacher();
//		t.setInfo("mariam",null,"Female","mariam@gmail.com","Egypt","12345678kfdfh",true);
//		System.out.println(t.getMail());
//		System.out.println(t.getBirthdate());
//		System.out.println(t.getCountry());
//		System.out.println(t.getGender());
//		System.out.println(t.getName());
//		System.out.println(t.getPassword());
//		System.out.println(t.isVerified());
//	}
}
