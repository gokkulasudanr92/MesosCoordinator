package org.renci.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

//import org.apache.mesos.v1.scheduler.SchedulerProtos;
import org.apache.mesos.v1.scheduler.SchedulerProtos.Event;
import org.apache.mesos.v1.scheduler.SchedulerProtos.Call;

@Provider
@Produces("application/x-protobuf")
@Consumes("application/x-protobuf")
public class PBMessageProcessor implements MessageBodyReader, MessageBodyWriter {
    @Override
    public boolean isReadable(Class type, Type type1, Annotation[] antns, MediaType mt) {
    	//System.out.println("====== isReadable() called");
        return  mt.toString().equals("application/x-protobuf"); 
    }

    @Override
    public Object readFrom(Class type, Type type1, Annotation[] antns, 
            MediaType mt, MultivaluedMap mm, InputStream inputStream) 
            throws IOException, WebApplicationException {
    	
    	//System.out.println("====== readFrom() called");
		/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		int read;
		long total = 0L;
		do {
			read = inputStream.read(buffer, 0, buffer.length);
			if (read > 0) {
				baos.write(buffer, 0, read);
				total += read;
			}
		} while (read > 0);
		return baos;  */
		
		return Event.parseFrom( inputStream );
    }

    
    
    
    @Override
    public boolean isWriteable(Class type, Type type1, Annotation[] antns, MediaType mt) {
    	//System.out.println("====== isWriteable() called");
    	return  mt.toString().equals("application/x-protobuf"); 
    }

    @Override
    public long getSize(Object t, Class type, Type type1, Annotation[] antns, MediaType mt) {  
    	//System.out.println("====== getSize() called");
    	int result=0;
    	
    	if( t instanceof Event ){
    		Event event = (Event)t;
    		byte [] bytes = event.toByteArray ();
    		result = bytes.length;
    	}else if( t instanceof Call ){
    		Call call = (Call)t;
    		byte [] bytes = call.toByteArray ();
    		result = bytes.length;
    	}
    	
    	return result;
    }

    @Override
    public void writeTo(Object t, Class type, Type type1, 
            Annotation[] antns, MediaType mt, 
            MultivaluedMap mm, OutputStream outStream) 
            throws IOException, WebApplicationException {
        
    	//System.out.println("====== writeTo() called");
    	if( t instanceof Event ){
    		Event event = (Event)t;
    		event.writeTo( outStream );
    	}else if( t instanceof Call ){
    		Call call = (Call)t;
    		call.writeTo( outStream );
    	}
    }  
}
