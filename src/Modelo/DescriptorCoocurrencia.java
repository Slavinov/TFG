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
    public int distancia = 1;
    
    public float[] getEnergy() {
        return energy;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int nivel) {
        this.distancia = nivel;
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
    
    public float[][] devolverMatriz(){
        float[][] resultado = new float[5][];
        resultado[0] = this.energy;
        resultado[1] = this.inertia;
        resultado[2] = this.correlation;
        resultado[3] = this.IDM;
        resultado[4] = this.entropy;
                
        return resultado;
    }
}
