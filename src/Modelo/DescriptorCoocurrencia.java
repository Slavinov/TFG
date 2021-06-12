/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Stanislav
 */
public class DescriptorCoocurrencia extends Descriptor{
    public float[] energy = new float[6];
    public float[] inertia = new float[6];
    public float[] correlation = new float[6];
    public float[] IDM = new float[6];
    public float[] entropy = new float[6];

    public float[] getEnergy() {
        return energy;
    }

    public void setEnergy(float[] energy) {
        this.energy = energy;
    }

    public float[] getInertia() {
        return inertia;
    }

    public void setInertia(float[] inertia) {
        this.inertia = inertia;
    }

    public float[] getCorrelation() {
        return correlation;
    }

    public void setCorrelation(float[] correlation) {
        this.correlation = correlation;
    }

    public float[] getIDM() {
        return IDM;
    }

    public void setIDM(float[] IDM) {
        this.IDM = IDM;
    }

    public float[] getEntropy() {
        return entropy;
    }

    public void setEntropy(float[] entropy) {
        this.entropy = entropy;
    }
    
    
}
