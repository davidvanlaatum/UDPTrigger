package org.jenkinsci.plugins.udptrigger;

import hudson.model.Cause;

/**
 *
 * @author David van Laatum
 */
public class ListenerCause extends Cause {

  private String description;

  public ListenerCause ( String description ) {
    this.description = description;
  }

  @Override
  public String getShortDescription () {
    return description;
  }

}
