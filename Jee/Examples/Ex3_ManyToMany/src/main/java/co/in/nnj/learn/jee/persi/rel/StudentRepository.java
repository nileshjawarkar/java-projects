package co.in.nnj.learn.jee.persi.rel;

import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class StudentRepository {

    @PersistenceContext
    private EntityManager em;

    public Student createStudent(final Student student) {
        em.persist(student);
        return student;
    }

    public Optional<Student> findStudent(final Long studId) {
        return Optional.ofNullable(em.find(Student.class, studId));
    }

    public boolean addCourseToStudent(final Course course, final Long stud_id) {
        return findStudent(stud_id).map(stud -> {
            stud.getCourses().add(course);
            course.getStudents().add(stud);
            em.persist(stud);
            return true;
        }).orElse(false);
    }
}
