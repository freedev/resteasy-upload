package org.javaee7.jaxrs.fileupload;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InputData {
  
  private List<String> items;

  /**
   * @return the items
   */
  @XmlElementWrapper(name = "items")
  @XmlElement(name = "item")
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
