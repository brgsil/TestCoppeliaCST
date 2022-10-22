/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestCoppeliaCST;

/**
 *
 * @author bruno
 */

import co.nstant.in.cbor.CborException;
import com.coppeliarobotics.remoteapi.zmq.*;
import java.util.List;

public class Environment {
    
    private RemoteAPIClient client;
    private RemoteAPIObjects._sim sim;
    private Long agentHandle;
    private Long applesTreeHandle;
    
    public void startSimulation() throws java.io.IOException, CborException{
        client = new RemoteAPIClient();
        sim = client.getObject().sim();

        client.setStepping(true);
        sim.startSimulation();
        
        agentHandle = sim.getObject("/agent");
        applesTreeHandle = sim.getObject("/apples");
    }
    
    public void stopSimulation() throws CborException{
        sim.stopSimulation();
    }
    
    public List<Float> getAgentPosition() throws CborException{
        List<Float> pos = sim.getObjectPosition(agentHandle, sim.handle_world);
        return pos;
    }
    
    public float getAgentPitch() throws CborException{
        // Euler angles (alpha, beta and gamma)
        List<Float> euler = sim.getObjectOrientation(agentHandle, sim.handle_world);
        return euler.get(0);
    }
    
    public String getApplesInVision() throws CborException{
        Long firstApple = sim.getObjectChild(applesTreeHandle, 0);
        List<Float> posRelApple = sim.getObjectPosition(firstApple, agentHandle);
        float x = posRelApple.get(0);
        float y = posRelApple.get(1);
        if (x < 100 && (-0.5*x < y && y < 0.5*x)) {
            return "Is in vision";
        } else {
            return "Is not in vision";            
        }
    }
}
