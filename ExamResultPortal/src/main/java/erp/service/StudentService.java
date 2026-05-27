package erp.service;

import erp.exception.NotFoundException;
import erp.model.Student;
import erp.repository.StudentRepository;
import erp.util.IDGenerator;
import erp.util.Validator;

import java.util.List;

public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    public Student register(String name, String email, String department,
                             int semester, String batch, String rollNumber) {
        Validator.requireNonBlank(name,        "Name");
        Validator.requireValidEmail(email);
        Validator.requireNonBlank(department,  "Department");
        Validator.requireRange(semester, 1, 8, "Semester");
        Validator.requireNonBlank(batch,       "Batch");
        Validator.requireNonBlank(rollNumber,  "Roll Number");

        Student s = new Student(IDGenerator.nextStudentId(), name, email,
                                department, semester, batch, rollNumber);
        repo.save(s);
        return s;
    }

    public Student findById(String id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NotFoundException("Student not found: " + id));
    }

    public List<Student> findAll()                        { return repo.findAll(); }
    public List<Student> findByDepartment(String dept)    { return repo.findByDepartment(dept); }
    public List<Student> findBySemester(int sem)          { return repo.findBySemester(sem); }

    public void updateSemester(String id, int sem) {
        Validator.requireRange(sem, 1, 8, "Semester");
        Student s = findById(id);
        s.setSemester(sem);
        repo.update(s);
    }

    public void delete(String id) {
        if (!repo.deleteById(id))
            throw new NotFoundException("Student not found: " + id);
    }
}
