package org.javaee7.jaxrs.fileupload;

import javax.ws.rs.FormParam;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

public class ByFieldForm {
  
  @FormParam("name") private String name;
  @FormParam("data") 
  private InputPart data;
  /**
   * @return the name
   */
  public String getName()
  {
    return name;
  }
  /**
   * @param name the name to set
   */
  public void setName(String name)
  {
    this.name = name;
  }
  /**
   * @return the data
   */
  public InputPart getData()
  {
    return data;
  }
  /**
   * @param data the data to set
   */
  public void setData(InputPart data)
  {
    this.data = data;
  }
  
  

}
