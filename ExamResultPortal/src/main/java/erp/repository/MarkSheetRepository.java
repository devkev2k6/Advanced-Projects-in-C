package erp.repository;

import erp.exception.DuplicateEntryException;
import erp.model.MarkSheet;

import java.util.*;

public class MarkSheetRepository implements Repository<MarkSheet, String> {

    private final Map<String, MarkSheet> store = new LinkedHashMap<>();

    @Override
    public void save(MarkSheet ms) {
        if (store.containsKey(ms.getMarkSheetId()))
            throw new DuplicateEntryException("MarkSheet already exists: " + ms.getMarkSheetId());
        store.put(ms.getMarkSheetId(), ms);
    }

    @Override public Optional<MarkSheet> findById(String id)    { return Optional.ofNullable(store.get(id)); }
    @Override public List<MarkSheet>     findAll()               { return new ArrayList<>(store.values()); }
    @Override public void                update(MarkSheet ms)    { store.put(ms.getMarkSheetId(), ms); }
    @Override public boolean             deleteById(String id)   { return store.remove(id) != null; }
    @Override public boolean             existsById(String id)   { return store.containsKey(id); }

    public List<MarkSheet> findByStudentId(String studentId) {
        List<MarkSheet> out = new ArrayList<>();
        for (MarkSheet ms : store.values())
            if (ms.getStudentId().equals(studentId)) out.add(ms);
        return out;
    }

    public List<MarkSheet> findByExamId(String examId) {
        List<MarkSheet> out = new ArrayList<>();
        for (MarkSheet ms : store.values())
            if (ms.getExam().getExamId().equals(examId)) out.add(ms);
        return out;
    }

    // Check if a mark entry already exists for this student+exam combination
    public boolean existsByStudentAndExam(String studentId, String examId) {
        for (MarkSheet ms : store.values())
            if (ms.getStudentId().equals(studentId) && ms.getExam().getExamId().equals(examId))
                return true;
        return false;
    }
}
