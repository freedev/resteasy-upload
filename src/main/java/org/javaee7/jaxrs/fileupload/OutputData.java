package org.javaee7.jaxrs.fileupload;

import java.util.List;

import javax.ws.rs.core.MediaType;

public class OutputData {
  private String       name;
  private MediaType    contentType;
  private List<String> items;

  public OutputData withName(String name)
  {
    this.name = name;
    return this;
  }

  public OutputData withContentType(MediaType contentType)
  {
    this.contentType = contentType;
    return this;
  }

  public OutputData withItems(List<String> items)
  {
    this.items = items;
    return this;
  }

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
   * @return the contentType
   */
  public MediaType getContentType()
  {
    return contentType;
  }

  /**
   * @param contentType the contentType to set
   */
  public void setContentType(MediaType contentType)
  {
    this.contentType = contentType;
  }

  /**
   * @return the items
   */
  public List<String> getItems()
  {
    return items;
  }

  /**
   * @param items the items to set
   */
  public void setItems(List<String> items)
  {
    this.items = items;
  }

}
