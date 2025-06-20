package co.in.nnj.learn.jee.port.input.controller;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import co.in.nnj.learn.jee.domain.service.DepartmentService;
import co.in.nnj.learn.jee.domain.valueobjects.Department;
import co.in.nnj.learn.jee.domain.valueobjects.DepartmentType;

@Path("department")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController {

    @Inject
    DepartmentService departmentSrv;

    @Context
    UriInfo uriInfo;

    @POST
    public Response createDepartment(final JsonObject jsonInput) {
        final String name = jsonInput.containsKey("name") ? jsonInput.getString("name") : null;
        final String function = jsonInput.containsKey("function") ? jsonInput.getString("function") : null;
        if (name == null || name.isEmpty() || function == null || function.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error - name and function are mandatory attributes.")
                    .build();
        }

        try {
            final DepartmentType type = DepartmentType.valueOf(function);
            final Department created = departmentSrv.create(new Department(name, type));
            final JsonObject jsonObject = Json.createObjectBuilder()
                    .add("name", created.name())
                    .add("function", created.function().toString())
                    .add("id", created.id().toString()).build();

            return Response.status(Response.Status.CREATED).entity(jsonObject)
                    .location(uriInfo.getBaseUriBuilder().path(DepartmentController.class)
                            .path(DepartmentController.class, "findDepartment").build(created.id()))
                    .build();
        } catch (final IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(String.format("Error - [%s] is not valid value for function.", function))
                    .build();
        }
    }

    @GET
    @Path("{id}")
    public Response findDepartment(@PathParam("id") final String id) {
        final UUID uuid = UUID.fromString(id);
        final Department dept = departmentSrv.find(uuid);
        return Response.ok().entity(dept).build();
    }

    Response listToResponse(final List<Department> depts) {
        if (depts == null || depts.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        final JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (final Department departmentDTO : depts) {
            arrayBuilder.add(Json.createObjectBuilder()
                    .add("id", departmentDTO.id().toString())
                    .add("name", departmentDTO.name())
                    .add("function", departmentDTO.function().toString()).build());
        }
        return Response.ok().entity(arrayBuilder.build()).build();
    }

    @GET
    @Path("{name}")
    public Response findDepartmentByName(@PathParam("name") final String name) {
        final List<Department> depts = departmentSrv.findByName(name);
        return listToResponse(depts);
    }

    @GET
    public Response findAll() {
        final List<Department> depts = departmentSrv.findAll();
        return listToResponse(depts);
    }
}
