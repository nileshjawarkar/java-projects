package co.in.nnj.learn.jee.persi.rel;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.stream.JsonCollectors;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("course")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CourseController {

    @Inject
    CourseRepository repo;

    @Context
    UriInfo uriInfo;

    @POST
    public Response createCourse(final Course course) {
        final Course newCourse = repo.createCourse(course);
        final URI uri = uriInfo.getBaseUriBuilder()
                .path(CourseController.class)
                .path(CourseController.class, "getCourse")
                .build(newCourse.getId());

        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public Response getCourse(@PathParam("id") final Long id) {
        return repo.findCourse(id).map(course -> {
            final JsonArray students = course.getStudents().stream().map(stud -> {
                return Json.createObjectBuilder()
                        .add("id", stud.getId())
                        .add("fname", stud.getFname())
                        .add("lname", stud.getLname())
                        .build();
            }).collect(JsonCollectors.toJsonArray());

            return Response.ok(Json.createObjectBuilder()
                    .add("id", course.getId())
                    .add("title", course.getTitle())
                    .add("description", course.getDescription())
                    .add("students", students).build());
        }).orElseGet(Response::noContent).build();
    }

    @POST
    @Path("{id}/student")
    public Response addStudentToCourse(@PathParam("id") final Long courseId, final Student student) {
        if (repo.addStudentToCourse(student, courseId)) {
            return Response.ok(Json.createObjectBuilder().add("id", student.getId())).build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
