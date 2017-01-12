package org.javaee7.jaxrs.fileupload;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

/**
 * @author Xavier Coulon
 */
@Path("/endpoint")
public class MyResource {

  @POST
  @Path("/uploadMultipart")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postMultipart(@MultipartForm ByFieldForm form)
  {

    try {
      InputData input = parse(form.getData());
      OutputData output = new OutputData()
                          .withName(form.getName())
                          .withContentType(form.getData().getMediaType())
                          .withItems(input.getItems());
      return Response.ok()
                     .entity(output)
                     .build();
    } catch (JAXBException | IOException e) {
      return Response.serverError()
                     .entity(e.getMessage())
                     .build();

    }


  }

  private InputData parse(InputPart part) throws JAXBException, IOException
  {
    JAXBContext jaxbc = JAXBContext.newInstance(InputData.class);
    Unmarshaller unmarshaller = jaxbc.createUnmarshaller();

    try (InputStream stream = part.getBody(InputStream.class, null)) {
      StreamSource source = new StreamSource(stream);
      return unmarshaller.unmarshal(source, InputData.class)
                         .getValue();
    } finally {
      if (unmarshaller instanceof Closeable) {
        ((Closeable) unmarshaller).close();
      }
    }
  }

  @POST
  @Path("/upload")
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postOctetStream(InputStream content)
  {
    try (Reader reader = new InputStreamReader(content)) {
      int totalsize = 0;
      int count = 0;
      final char[] buffer = new char[256];
      while ((count = reader.read(buffer)) != -1) {
        totalsize += count;
      }
      return Response.ok(totalsize)
                     .build();
    } catch (IOException e) {
      e.printStackTrace();
      return Response.serverError()
                     .build();
    }
  }

  @POST
  @Path("/upload2")
  @Consumes({MediaType.APPLICATION_OCTET_STREAM, "image/png"})
  @Produces(MediaType.TEXT_PLAIN)
  public Response postImageFile(File file)
  {
    try (Reader reader = new FileReader(file)) {
      int totalsize = 0;
      int count = 0;
      final char[] buffer = new char[256];
      while ((count = reader.read(buffer)) != -1) {
        totalsize += count;
      }
      return Response.ok(totalsize)
                     .build();
    } catch (IOException e) {
      e.printStackTrace();
      return Response.serverError()
                     .build();
    }
  }

}
