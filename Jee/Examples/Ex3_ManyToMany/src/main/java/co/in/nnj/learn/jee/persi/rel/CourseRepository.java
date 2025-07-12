package co.in.nnj.learn.jee.persi.rel;

import java.util.Optional;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CourseRepository {

    @PersistenceContext
    EntityManager em;

    public Course createCourse(final Course course) {
        em.persist(course);
        return course;
    }

    public Optional<Course> findCourse(final Long id) {
        return Optional.ofNullable(em.find(Course.class, id));
    }

    public boolean addStudentToCourse(final Student student, final Long course_id) {
        return findCourse(course_id).map(course -> {
            course.getStudents().add(student);
            student.getCourses().add(course);
            em.persist(course);
            return true;
        }).orElse(false);
    }
}
