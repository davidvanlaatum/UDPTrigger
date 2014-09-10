package org.jenkinsci.plugins.udptrigger;

import hudson.Extension;
import hudson.model.BuildableItem;
import hudson.model.Item;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author David van Laatum
 */
public final class ListenerTrigger extends Trigger<BuildableItem> {

  private String magic;

  @DataBoundConstructor
  public ListenerTrigger ( String magic ) {
    this.magic = magic;
  }

  public String getMagic () {
    return magic;
  }

  @Extension
  public static final class DescriptorImpl extends TriggerDescriptor {

    @Override
    public String getDisplayName () {
      return "UDP Trigger";
    }

    @Override
    public boolean isApplicable ( Item item ) {
      return item instanceof BuildableItem;
    }

  }
}
