package org.javaee7.jaxrs.fileupload;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.assertj.core.api.Condition;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Arun Gupta
 * @author Xavier Coulon
 */
@RunWith(Arquillian.class)
public class MyResourceTest {

  @Deployment(testable = false)
  public static WebArchive createDeployment()
  {
    return ShrinkWrap.create(WebArchive.class)
                     .addClasses(MyApplication.class, MyResource.class, ByFieldForm.class, InputData.class, OutputData.class);
  }

  private static WebTarget target;

  private static File      tempFile;
  private static int       fileSize = 10000;
  @ArquillianResource
  private URL              base;

  @BeforeClass
  public static void generateSampleFile() throws IOException
  {
    tempFile = File.createTempFile("javaee7samples", ".png");
    // fill the file with 1KB of content
    try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
      for (int i = 0; i < fileSize; i++) {
        outputStream.write(0);
      }
      outputStream.flush();
    }
    assertThat(tempFile).canRead()
                        .has(new Condition<File>() {

                          @Override
                          public boolean matches(File tempFile)
                          {
                            return tempFile.length() == fileSize;
                          }
                        });
  }

  @Before
  public void setUpClass() throws MalformedURLException
  {
    Client client = ClientBuilder.newClient();
    // target = client.target(URI.create(new URL(base, "webresources/endpoint").toExternalForm()));
    target = client.target("http://localhost:8080/upload-file/webresources/endpoint");
  }

  @Test
  public void shouldPostOctetStreamContentAsInputStream()
  {
    // when
    Long uploadedFileSize = target.path("/upload")
                                  .request()
                                  .post(Entity.entity(tempFile, MediaType.APPLICATION_OCTET_STREAM), Long.class);
    // then
    assertThat(uploadedFileSize).isEqualTo(fileSize);
  }

  @Test
  public void shouldNotPostImagePngContentAsInputStream()
  {
    // when
    final Response response = target.path("/upload")
                                    .request()
                                    .post(Entity.entity(tempFile, "image/png"));
    // then
    assertThat(response.getStatus()).isEqualTo(Status.UNSUPPORTED_MEDIA_TYPE.getStatusCode());
  }

  @Test
  public void shouldPostOctetStreamContentAsFile()
  {
    // when
    Long uploadedFileSize = target.path("/upload2")
                                  .request()
                                  .post(Entity.entity(tempFile, MediaType.APPLICATION_OCTET_STREAM), Long.class);
    // then
    assertThat(uploadedFileSize).isEqualTo(fileSize);
  }

  @Test
  public void shouldPostImagePngContentAsFile()
  {
    // when
    Long uploadedFileSize = target.path("/upload2")
                                  .request()
                                  .post(Entity.entity(tempFile, "image/png"), Long.class);
    // then
    assertThat(uploadedFileSize).isEqualTo(fileSize);
  }

  @Test
  public void testBigFile() throws FileNotFoundException
  {
    // FileInputStream fis = new FileInputStream(bigFile);
    System.out.println(tempFile.length());
    MultipartFormDataOutput mdo = new MultipartFormDataOutput();
    mdo.addFormData("name", "Test by setter", MediaType.TEXT_PLAIN_TYPE);
    mdo.addFormData("data", new FileInputStream(tempFile), MediaType.APPLICATION_OCTET_STREAM_TYPE);
    GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(mdo) {};

    Response post = target.path("/uploadMultipart")
                          .request(MediaType.MULTIPART_FORM_DATA_TYPE)
                          .accept(MediaType.MEDIA_TYPE_WILDCARD)
                          .post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));
    assertThat(post.getStatus()).isEqualTo(200);
    Long uploadedFileSize = post.readEntity(Long.class);
    assertThat(uploadedFileSize).isEqualTo(tempFile.length());

  }

}
