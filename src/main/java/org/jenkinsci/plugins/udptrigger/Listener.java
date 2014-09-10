package org.jenkinsci.plugins.udptrigger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import hudson.model.AbstractProject;
import hudson.triggers.Trigger;
import jenkins.model.Jenkins;

/**
 * @author David van Laatum
 */
public class Listener extends Thread {

  private static final Logger LOG
          = Logger.getLogger ( Listener.class.getName () );

  public Listener () {
    setDaemon ( true );
    setName ( "UDP Trigger" );
  }

  @Override
  public synchronized void start () {
    LOG.log ( Level.INFO, "Starting listener" );
    super.start ();
  }

  public synchronized void shutdown () {
    LOG.log ( Level.INFO, "Stopping listener" );
    interrupt ();
  }

  @Override
  public void run () {
    try {
      DatagramSocket serverSocket = new DatagramSocket ( 9876 );
      byte[] receiveData = new byte[1024];
      while ( !isInterrupted () ) {
        DatagramPacket receivePacket = new DatagramPacket ( receiveData,
                                                            receiveData.length );
        serverSocket.receive ( receivePacket );
        String sentence = new String ( receivePacket.getData (), 0,
                                       receivePacket.getLength () );
        InetAddress IPAddress = receivePacket.getAddress ();
        int port = receivePacket.getPort ();
        LOG.log ( Level.INFO, "RECEIVED: {0} from {1}:{2}", new Object[]{
          sentence, IPAddress.getHostAddress (), port } );

        for ( AbstractProject<?, ?> p : Jenkins.getInstance ().getItems (
                AbstractProject.class ) ) {
          Trigger t = p.getTrigger ( ListenerTrigger.class );
          if ( t instanceof ListenerTrigger ) {
            if ( ( (ListenerTrigger) t ).getMagic ()
                    .equalsIgnoreCase ( sentence ) ) {
              p.scheduleBuild ( 0, new ListenerCause ( "Triggered by "
                                                               + IPAddress
                                .getHostAddress () ) );
            }
          }
        }
      }
    } catch ( SocketException ex ) {
      LOG.log ( Level.SEVERE, null, ex );
    } catch ( IOException ex ) {
      LOG.log ( Level.SEVERE, null, ex );
    }
  }

}
