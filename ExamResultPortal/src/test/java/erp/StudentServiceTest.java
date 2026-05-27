package erp;

import erp.exception.InvalidDataException;
import erp.exception.NotFoundException;
import erp.model.Student;
import erp.repository.StudentRepository;
import erp.service.StudentService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceTest {

    private static StudentService service;
    private static String enrolledId;

    @BeforeAll
    static void setup() {
        service = new StudentService(new StudentRepository());
    }

    @Test @Order(1)
    void testRegister() {
        Student s = service.register("Arjun Roy", "arjun@college.in",
                                      "ECE", 2, "2023-2027", "ECE/23/042");
        assertNotNull(s.getId());
        assertEquals("Arjun Roy", s.getName());
        enrolledId = s.getId();
    }

    @Test @Order(2)
    void testFindById() {
        assertEquals("Arjun Roy", service.findById(enrolledId).getName());
    }

    @Test @Order(3)
    void testNotFoundThrows() {
        assertThrows(NotFoundException.class, () -> service.findById("STU9999"));
    }

    @Test @Order(4)
    void testInvalidEmailThrows() {
        assertThrows(InvalidDataException.class,
                () -> service.register("Test", "not-email", "CSE", 1, "2024-2028", "R001"));
    }

    @Test @Order(5)
    void testInvalidSemesterThrows() {
        assertThrows(InvalidDataException.class,
                () -> service.register("Test2", "t@c.in", "ME", 9, "2024-2028", "R002"));
    }

    @Test @Order(6)
    void testDelete() {
        service.delete(enrolledId);
        assertThrows(NotFoundException.class, () -> service.findById(enrolledId));
    }
}
