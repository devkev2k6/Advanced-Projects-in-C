package erp.model;

public class Examiner extends User {

    private String designation;

    public Examiner(String id, String name, String email,
                    String department, String designation) {
        super(id, name, email, department);
        this.designation = designation;
    }

    @Override
    public String getRole() { return "EXAMINER"; }

    public String getDesignation() { return designation; }

    @Override
    public String toString() {
        return super.toString() + " | " + designation;
    }
}
