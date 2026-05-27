package erp.repository;

import erp.exception.DuplicateEntryException;
import erp.model.Student;

import java.util.*;

public class StudentRepository implements Repository<Student, String> {

    private final Map<String, Student> store = new LinkedHashMap<>();

    @Override
    public void save(Student s) {
        if (store.containsKey(s.getId()))
            throw new DuplicateEntryException("Student ID already exists: " + s.getId());
        store.put(s.getId(), s);
    }

    @Override public Optional<Student> findById(String id)  { return Optional.ofNullable(store.get(id)); }
    @Override public List<Student>     findAll()             { return new ArrayList<>(store.values()); }
    @Override public void              update(Student s)     { store.put(s.getId(), s); }
    @Override public boolean           deleteById(String id) { return store.remove(id) != null; }
    @Override public boolean           existsById(String id) { return store.containsKey(id); }

    public List<Student> findByDepartment(String dept) {
        List<Student> out = new ArrayList<>();
        for (Student s : store.values())
            if (s.getDepartment().equalsIgnoreCase(dept)) out.add(s);
        return out;
    }

    public List<Student> findBySemester(int sem) {
        List<Student> out = new ArrayList<>();
        for (Student s : store.values())
            if (s.getSemester() == sem) out.add(s);
        return out;
    }
}
