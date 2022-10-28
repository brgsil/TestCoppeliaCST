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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment {
    
    private RemoteAPIClient client;
    private RemoteAPIObjects._sim sim;
    private Long agentHandle;
    private Long applesTreeHandle;
    private Long targetHandle;
    
    public void startSimulation() throws java.io.IOException, CborException{
        client = new RemoteAPIClient();
        sim = client.getObject().sim();

        client.setStepping(false);
        sim.startSimulation();
        
        agentHandle = sim.getObject("/agent");
//        List<Float> dirr = new ArrayList();
//        dirr.add((float) 0.1);
//        dirr.add((float) 0.0);
//        dirr.add((float) 0.0);
//        sim.setObjectPosition(agentHandle, agentHandle, dirr);
        applesTreeHandle = sim.getObject("/apples");
        targetHandle = sim.getObject("/target");
        
        float startTime = sim.getSimulationTime();
        while(sim.getSimulationTime() - startTime < 1){}
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
        return euler.get(2);
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
    
    public void moveTo(float x, float y) throws CborException{
        List<Float> pos = sim.getObjectPosition(agentHandle, sim.handle_world);
        double goalPitch = Math.atan2(y - pos.get(1), x - pos.get(0));
        
        List<Float> euler = sim.getObjectOrientation(agentHandle, sim.handle_world);
        euler.set(2, (float) goalPitch);
        
        sim.setObjectOrientation(targetHandle, sim.handle_world, euler);
        List<Float> targetPos = Arrays.asList(new Float[]{x, y, (float) 0});
        sim.setObjectPosition(targetHandle, sim.handle_world, targetPos);
    }
    
    public void waitSim(float waitTime) throws CborException{
        float startTime = sim.getSimulationTime();
        while(sim.getSimulationTime() - startTime < waitTime){}
    }
}
