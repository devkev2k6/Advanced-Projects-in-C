package erp.repository;

import erp.exception.DuplicateEntryException;
import erp.model.Examiner;

import java.util.*;

public class ExaminerRepository implements Repository<Examiner, String> {

    private final Map<String, Examiner> store = new LinkedHashMap<>();

    @Override
    public void save(Examiner e) {
        if (store.containsKey(e.getId()))
            throw new DuplicateEntryException("Examiner ID already exists: " + e.getId());
        store.put(e.getId(), e);
    }

    @Override public Optional<Examiner> findById(String id)  { return Optional.ofNullable(store.get(id)); }
    @Override public List<Examiner>     findAll()             { return new ArrayList<>(store.values()); }
    @Override public void               update(Examiner e)    { store.put(e.getId(), e); }
    @Override public boolean            deleteById(String id) { return store.remove(id) != null; }
    @Override public boolean            existsById(String id) { return store.containsKey(id); }
}
