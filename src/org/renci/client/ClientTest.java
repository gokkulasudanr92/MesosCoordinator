package org.renci.client;

import java.net.URI;
import java.util.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*; 

import org.glassfish.jersey.client.ClientConfig;

import org.apache.mesos.v1.Protos.Offer;
import org.apache.mesos.v1.Protos.OfferID;
import org.apache.mesos.v1.Protos.FrameworkID;
import org.apache.mesos.v1.Protos.AgentID;
import org.apache.mesos.v1.Protos.Resource;
import org.apache.mesos.v1.Protos.Value;
import org.apache.mesos.v1.Protos.Value.Type;
import org.apache.mesos.v1.Protos.Value.Scalar;
import org.apache.mesos.v1.Protos.Value.Range;

import org.apache.mesos.v1.scheduler.SchedulerProtos;
import org.apache.mesos.v1.scheduler.SchedulerProtos.Event;
import org.apache.mesos.v1.scheduler.SchedulerProtos.Event.Offers;
import org.apache.mesos.v1.Protos.Value.Type;
import org.renci.resource.*;

public class ClientTest {
	public static void main(String[] args) {
        ClientConfig config = new ClientConfig().register( PBMessageProcessor.class );
        Client client = ClientBuilder.newClient(config);
        WebTarget target = client.target( getBaseURI() ); 
        Invocation.Builder invocationBuilder = target.path("api").request( "application/x-protobuf" );
        
        Resource rse1 = Resource.newBuilder().setName("cpu").setType(Type.SCALAR).
        		setScalar(Scalar.newBuilder().setValue(1)).build();
        Resource rse2 = Resource.newBuilder().setName("memory").setType(Type.SCALAR).
        		setScalar(Scalar.newBuilder().setValue(100)).build();
        
        Offer of = Offer.newBuilder().
        		setId( OfferID.newBuilder().setValue("12214-23523-O235235") ).
        		setFrameworkId( FrameworkID.newBuilder().setValue("12124-235325-32425") ).
        		setAgentId( AgentID.newBuilder().setValue("12325-23523-S23523") ).
        		setHostname("agent.host").addResources( rse1 ).addResources( rse2 ).build();
        
        Event event = Event.newBuilder().setType(Event.Type.OFFERS).setOffers( Offers.newBuilder().addOffers( of ) ).build(); 
        
        Response response = invocationBuilder.post( Entity.entity(event, "application/x-protobuf") );
        
        System.out.println( response.readEntity(String.class) );
	}
	
	
    private static URI getBaseURI() {
        return UriBuilder.fromUri( "http://165.124.159.8:8080/MesosCoordinator" ).build();
    }
}

/* curl  usage cases:
	GET with JSON: curl  http://165.124.159.8:8080/MesosCoordinator/api
	
 */
