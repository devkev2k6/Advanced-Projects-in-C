package erp.model;

public abstract class User {

    private String id;
    private String name;
    private String email;
    private String department;

    public User(String id, String name, String email, String department) {
        this.id         = id;
        this.name       = name;
        this.email      = email;
        this.department = department;
    }

    public abstract String getRole();

    public String getId()         { return id; }
    public String getName()       { return name; }
    public String getEmail()      { return email; }
    public String getDepartment() { return department; }

    public void setName(String name)             { this.name       = name; }
    public void setEmail(String email)           { this.email      = email; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s | %s", getRole(), id, name, email, department);
    }
}
