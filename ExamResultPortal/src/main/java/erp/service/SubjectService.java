package erp.service;

import erp.exception.NotFoundException;
import erp.model.Subject;
import erp.repository.SubjectRepository;
import erp.util.IDGenerator;
import erp.util.Validator;

import java.util.List;

public class SubjectService {

    private final SubjectRepository repo;

    public SubjectService(SubjectRepository repo) { this.repo = repo; }

    public Subject add(String codePrefix, String name, int credits, int semester, String dept) {
        Validator.requireNonBlank(name,        "Subject name");
        Validator.requireRange(credits, 1, 6,  "Credits");
        Validator.requireRange(semester, 1, 8, "Semester");
        Validator.requireNonBlank(dept,        "Department");

        Subject s = new Subject(IDGenerator.nextSubjectCode(codePrefix), name, credits, semester, dept);
        repo.save(s);
        return s;
    }

    public Subject findByCode(String code) {
        return repo.findById(code)
                   .orElseThrow(() -> new NotFoundException("Subject not found: " + code));
    }

    public List<Subject> findAll()               { return repo.findAll(); }
    public List<Subject> findBySemester(int sem)  { return repo.findBySemester(sem); }
}
