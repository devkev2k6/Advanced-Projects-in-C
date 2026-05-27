package erp.repository;

import erp.exception.DuplicateEntryException;
import erp.model.Exam;

import java.util.*;

public class ExamRepository implements Repository<Exam, String> {

    private final Map<String, Exam> store = new LinkedHashMap<>();

    @Override
    public void save(Exam e) {
        if (store.containsKey(e.getExamId()))
            throw new DuplicateEntryException("Exam ID already exists: " + e.getExamId());
        store.put(e.getExamId(), e);
    }

    @Override public Optional<Exam> findById(String id)  { return Optional.ofNullable(store.get(id)); }
    @Override public List<Exam>     findAll()             { return new ArrayList<>(store.values()); }
    @Override public void           update(Exam e)        { store.put(e.getExamId(), e); }
    @Override public boolean        deleteById(String id) { return store.remove(id) != null; }
    @Override public boolean        existsById(String id) { return store.containsKey(id); }

    public List<Exam> findBySubjectCode(String code) {
        List<Exam> out = new ArrayList<>();
        for (Exam e : store.values())
            if (e.getSubject().getSubjectCode().equalsIgnoreCase(code)) out.add(e);
        return out;
    }

    public List<Exam> findPublished() {
        List<Exam> out = new ArrayList<>();
        for (Exam e : store.values())
            if (e.isResultPublished()) out.add(e);
        return out;
    }
}
