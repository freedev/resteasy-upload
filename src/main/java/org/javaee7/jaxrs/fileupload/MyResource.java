package org.javaee7.jaxrs.fileupload;

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
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * @author Xavier Coulon
 */
@Path("/endpoint")
public class MyResource {

  @POST
  @Path("/uploadMultipart")
  @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.WILDCARD, MediaType.TEXT_PLAIN, MediaType.APPLICATION_OCTET_STREAM})
  @Produces(MediaType.TEXT_PLAIN)
  public Response postMultipart(MultipartFormDataInput input)
  {

    try {
      // input.getFormDataMap().get("name");
      InputStream image = input.getFormDataPart("data", new GenericType<InputStream>() {});
      int size = 10_000_000;
      long totalByte = 0;
      byte[] buffer = new byte[size];
      int count = image.read(buffer);
      while (count != -1) {
        totalByte += count;
        count = image.read(buffer);
        System.out.println(totalByte);
      }
      image.close();
      return Response.ok()
                     .entity(Long.toString(totalByte))
                     .build();
    } catch (Exception e) {
      return Response.serverError()
                     .entity(e.getMessage())
                     .build();
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
