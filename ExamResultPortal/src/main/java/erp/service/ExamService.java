package erp.service;

import erp.exception.NotFoundException;
import erp.model.*;
import erp.repository.ExamRepository;
import erp.util.IDGenerator;
import erp.util.Validator;

import java.time.LocalDate;
import java.util.List;

public class ExamService {

    private final ExamRepository    repo;
    private final SubjectService    subjectService;
    private final ExaminerService   examinerService;

    public ExamService(ExamRepository repo, SubjectService subjectService,
                       ExaminerService examinerService) {
        this.repo            = repo;
        this.subjectService  = subjectService;
        this.examinerService = examinerService;
    }

    public Exam schedule(String subjectCode, ExamType examType,
                         LocalDate date, String examinerId) {
        Subject  subject  = subjectService.findByCode(subjectCode);
        Examiner examiner = examinerService.findById(examinerId);     // validates examiner exists

        Exam exam = new Exam(IDGenerator.nextExamId(), subject, examType, date, examiner.getId());
        repo.save(exam);
        return exam;
    }

    public Exam findById(String id) {
        return repo.findById(id)
                   .orElseThrow(() -> new NotFoundException("Exam not found: " + id));
    }

    public void publishResult(String examId) {
        Exam e = findById(examId);
        e.publishResult();
        repo.update(e);
    }

    public List<Exam> findAll()           { return repo.findAll(); }
    public List<Exam> findPublished()     { return repo.findPublished(); }

    public List<Exam> findBySubject(String code) {
        return repo.findBySubjectCode(code);
    }
}
