package erp.service;

import erp.exception.NotFoundException;
import erp.model.Examiner;
import erp.repository.ExaminerRepository;
import erp.util.IDGenerator;
import erp.util.Validator;

import java.util.List;

public class ExaminerService {

    private final ExaminerRepository repo;

    public ExaminerService(ExaminerRepository repo) { this.repo = repo; }

    public Examiner add(String name, String email, String department, String designation) {
        Validator.requireNonBlank(name,        "Name");
        Validator.requireValidEmail(email);
        Validator.requireNonBlank(department,  "Department");
        Validator.requireNonBlank(designation, "Designation");

        Examiner e = new Examiner(IDGenerator.nextExaminerId(), name, email, department, designation);
        repo.save(e);
        return e;
    }

    public Examiner findById(String id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NotFoundException("Examiner not found: " + id));
    }

    public List<Examiner> findAll() { return repo.findAll(); }

    public void delete(String id) {
        if (!repo.deleteById(id))
            throw new NotFoundException("Examiner not found: " + id);
    }
}
