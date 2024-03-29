package org.jenkinsci.plugins.udptrigger;

import java.io.IOException;
import java.util.logging.Logger;
import hudson.Extension;
import hudson.Plugin;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import jenkins.model.Jenkins;

/**
 *
 * @author David van Laatum
 */
@Extension
public class GlobalConfig extends Plugin {

  private static final Logger LOG
          = Logger.getLogger ( GlobalConfig.class.getName () );
  private final transient Listener listener = new Listener ();

  @Initializer ( after = InitMilestone.JOB_LOADED, fatal = false )
  public static void init () throws IOException, InterruptedException {
    Jenkins.getInstance ().getExtensionList ( Plugin.class )
            .get ( GlobalConfig.class ).listener.start ();
  }

  @Override
  public void stop () throws Exception {
    listener.shutdown ();
  }
}
