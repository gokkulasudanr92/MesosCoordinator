package org.renci.resource;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.*;

import java.net.*;
import java.util.*;

import org.apache.mesos.v1.Protos.Offer;
import org.apache.mesos.v1.Protos.Resource;
import org.apache.mesos.v1.Protos.Value;
import org.apache.mesos.v1.Protos.Value.Type;
import org.apache.mesos.v1.Protos.Value.Range;

import org.apache.mesos.v1.scheduler.SchedulerProtos;
import org.apache.mesos.v1.scheduler.SchedulerProtos.Event;

 
@Path("/api")
public class CoordinatorResource {
    @Context
    private UriInfo uriInfo;
    @Context
    private Request request;
    
    public CoordinatorResource(){}
    
    @GET
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public String getXML() {
    	return "Hello, how are you.\n";
    }
    
	@POST
	//@Consumes( { MediaType.APPLICATION_XML/*for java client*/, MediaType.APPLICATION_JSON/*for curl JSON*/ } )
	//@Produces(MediaType.APPLICATION_XML)
	@Consumes("application/x-protobuf")
	@Produces("application/x-protobuf")
	public Response process( Event event ) throws URISyntaxException {
		int tp = event.getType().getNumber();
		System.out.println("Received event, type: "+tp);
		
		switch( tp ){
		case Event.Type.OFFERS_VALUE:
			if( !event.hasOffers() )
				return Response.status(400).entity("No Offers information\n").build();
			
			Event.Offers ofs = event.getOffers();
			for( Offer of : ofs.getOffersList() ){
				System.out.println("===========One Offer Information:===========");
				System.out.println( "	Offer ID: "+of.getId().getValue() ); 
				System.out.println( "	Framework ID: "+of.getFrameworkId().getValue() );
				System.out.println( "	Agent ID: "+of.getAgentId().getValue() ); 
				System.out.println( "	Hostname: "+of.getHostname() );
				
				System.out.println( "	Resources:" );
				for( Resource rse : of.getResourcesList() ){
					switch( rse.getType().getNumber() ){
					case Type.SCALAR_VALUE:
						System.out.println( "		Name: "+rse.getName()+", "+rse.getScalar().getValue() );
						break;
					case Type.RANGES_VALUE:
						System.out.println( "		Name: "+rse.getName() );
						for( Range rg : rse.getRanges().getRangeList() )
							System.out.println( "			"+rg.getBegin() + " - " + rg.getEnd() );
						break;
					case Type.SET_VALUE:
						System.out.println( "		Name: "+rse.getName()+", "+rse.getSet() );
					}
				}
			}
			break;
		default:
			return Response.status(400).entity("The input type: "+tp+" is not supported.\n").build();
		}
		
		Response res = Response.ok().entity("Processed\n").build(); 
		return res;
	}
}
