package erp.repository;

import erp.exception.DuplicateEntryException;
import erp.model.Subject;

import java.util.*;

public class SubjectRepository implements Repository<Subject, String> {

    private final Map<String, Subject> store = new LinkedHashMap<>();

    @Override
    public void save(Subject s) {
        if (store.containsKey(s.getSubjectCode()))
            throw new DuplicateEntryException("Subject code already exists: " + s.getSubjectCode());
        store.put(s.getSubjectCode(), s);
    }

    @Override public Optional<Subject> findById(String code) { return Optional.ofNullable(store.get(code)); }
    @Override public List<Subject>     findAll()             { return new ArrayList<>(store.values()); }
    @Override public void              update(Subject s)     { store.put(s.getSubjectCode(), s); }
    @Override public boolean           deleteById(String c)  { return store.remove(c) != null; }
    @Override public boolean           existsById(String c)  { return store.containsKey(c); }

    public List<Subject> findBySemester(int sem) {
        List<Subject> out = new ArrayList<>();
        for (Subject s : store.values())
            if (s.getSemester() == sem) out.add(s);
        return out;
    }
}
