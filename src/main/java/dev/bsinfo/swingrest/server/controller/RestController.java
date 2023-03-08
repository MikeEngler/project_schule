package dev.bsinfo.swingrest.server.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.bsinfo.swingrest.model.Ablesung;
import dev.bsinfo.swingrest.model.Kunde;
import dev.bsinfo.swingrest.server.Server;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/hausverwaltung")
public class RestController {

	// @Inject :(
	// Database database;

	@POST
	@Path("/kunden")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createKunde(Kunde kunde) {
		if (kunde == null)
			return Response.status(400).entity("Body may not be empty").build();
		Server.DATABASE.getKunden().put(kunde.getId().toString(), kunde);
		ResponseBuilder response = Response.created(null);
		response.entity(kunde);
		return response.build();
	}

	@POST
	@Path("/ablesungen")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createAblesung(Ablesung ablesung) {
		if (ablesung == null)
			return Response.status(400).entity("Body may not be empty").build();
		Kunde kunde = ablesung.getKunde();
		if (kunde == null 
				|| !Server.DATABASE.getKunden().containsKey(kunde.getId().toString()))
			return Response.status(404).entity("Kunde konnte nicht gefunden werden").build();

		Server.DATABASE.getAblesungen().put(ablesung.getId().toString(), ablesung);
		ResponseBuilder response = Response.created(null);
		response.entity(ablesung);
		return response.build();

	}

	@PUT
	@Path("/kunden")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateKunde(Kunde kunde) {
		if (kunde == null)
			return Response.status(400).entity("Body may not be empty").build();
		if (!Server.DATABASE.getKunden().containsKey(kunde.getId().toString()))
			return Response.status(404).entity("Kunde konnte nicht gefunden werden").build();
		Server.DATABASE.getKunden().put(kunde.getId().toString(), kunde);
		return Response.status(200).entity("Updated").build();
	}

	@PUT
	@Path("/ablesungen")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateAblesung(Ablesung ablesung) {
		if (ablesung == null)
			return Response.status(400).entity("Body may not be empty").build();
		if (ablesung.getId() == null 
				|| !Server.DATABASE.getAblesungen().containsKey(ablesung.getId().toString()))
			return Response.status(404).entity("Ablesung konnte nicht gefunden werden").build();
		Server.DATABASE.getAblesungen().put(ablesung.getId().toString(), ablesung);
		return Response.ok("Updated").build();
	}

	@DELETE
	@Path("/ablesungen/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Ablesung deleteAblesung(@PathParam("id") String id) {
		return Server.DATABASE.getAblesungen().remove(id);
	}

	@DELETE
	@Path("/kunden/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Kunde deleteKunde(@PathParam("id") String id) {
		return Server.DATABASE.getKunden().remove(id);
	}

	@GET
	@Path("/kunden")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Kunde> getKunden() {
		return Server.DATABASE.getKunden().values();
	}

	@GET
	@Path("/kunden/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getKunde(@PathParam("id") String id) {
		if (!Server.DATABASE.getKunden().containsKey(id))
			return Response.status(404).entity("Kunde konnte nicht gefunden werden").build();
		return Response.ok(Server.DATABASE.getKunden().get(id)).build();
	}

	@GET
	@Path("/ablesungenVorZweiJahrenHeute")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Ablesung> getAblesungenVorZweiJahrenBisHeute() {
		return Server.DATABASE.getAblesungen().values().stream()
				.filter(ablesung -> LocalDate.now().getYear() - 2 <= ablesung.getDatum().getYear())
				.collect(Collectors.toList());
	}

	@GET
	@Path("/ablesungen/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAblesungen(@PathParam("id") String id) {
		if (!Server.DATABASE.getAblesungen().containsKey(id))
			return Response.status(404).entity("Ablesung konnte nicht gefunden werden").build();
		return Response.ok(Server.DATABASE.getAblesungen().get(id)).build();
	}

	@GET
	@Path("/ablesungen")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Ablesung> getAblesung(@QueryParam("kunde") String id, @QueryParam("beginn") String beginn,
			@QueryParam("ende") String ende) {

		// Validation
		try {
			LocalDate beginnDate = LocalDate.parse(beginn);
			LocalDate endeDate = LocalDate.parse(ende);
			// Filtering
			Stream<Ablesung> zeitraum = Server.DATABASE.getAblesungen().values().stream()
					.filter(ablesung -> ablesung.getDatum().isAfter(beginnDate))
					.filter(ablesung -> ablesung.getDatum().isBefore(endeDate));

			// Results empty -> 404
			List<Ablesung> ablesungen = zeitraum.toList();
			if (ablesungen.isEmpty()) {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
			return ablesungen;
		} catch (DateTimeParseException e) {
			// Format not ISO Date throw 400
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}

	}

}
