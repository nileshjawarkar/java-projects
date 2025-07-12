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

@Path("student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentController {
    @Inject
    StudentRepository repo;

    @Context
    UriInfo uriInfo;

    @POST
    public Response createStudent(final Student student) {
        final Student newStudent = repo.createStudent(student);
        final URI uri = uriInfo.getBaseUriBuilder().path(StudentController.class)
                .path(StudentController.class, "getStudent")
                .build(newStudent.getId());
        return Response.created(uri).build();
    }

    @GET
    @Path("{id}")
    public Response getStudent(@PathParam("id") final Long id) {
        return repo.findStudent(id).map(stud -> {
            final JsonArray courses = stud.getCourses().stream().map(course -> {
                return Json.createObjectBuilder()
                    .add("id", course.getId())
                    .add("title", course.getTitle())
                    .add("description", course.getDescription())
                    .build();
            }).collect(JsonCollectors.toJsonArray());

            return Response.ok(Json.createObjectBuilder()
                .add("id", stud.getId())
                .add("lname", stud.getLname())
                .add("fname", stud.getFname())
                .add("courses", courses)
                .build());
        }).orElseGet(Response::noContent).build();
    }

    @POST
    @Path("{id}/course")
    public Response addCourseToStudent(@PathParam("id") final Long studId, final Course course) {
        if(repo.addCourseToStudent(course, studId)) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
