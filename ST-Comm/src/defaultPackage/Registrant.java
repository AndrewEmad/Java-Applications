package defaultPackage;



import java.util.Date;

public class Registrant {

    protected String name;
    protected Date birthdate;
    protected String gender;
    protected String mail;
    protected String country;
    protected String password;

    public Registrant() {
        name = gender = mail = country = password = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setInfo(String name, Date birthdate, String gender, String mail, String country, String password) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.country = country;
        this.mail = mail;
        this.password = password;
    }

}
