/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestCoppeliaCST;

/**
 *
 * @author bruno
 */

import com.coppeliarobotics.remoteapi.zmq.*;

public class Environment {
    
    private RemoteAPIClient client;
    private RemoteAPIObjects._sim sim;
    
    public void startSimulation() throws java.io.IOException, co.nstant.in.cbor.CborException{
        client = new RemoteAPIClient();
        sim = client.getObject().sim();

        client.setStepping(true);
        sim.startSimulation();
    }
    
}
