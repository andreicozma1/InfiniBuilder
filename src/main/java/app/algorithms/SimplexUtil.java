package app.algorithms;
import app.algorithms.SimplexNoise;

import java.util.Random;

public class SimplexUtil {

    SimplexNoise[] octaves;
    double[] frequencys;
    double[] amplitudes;

    int largestFeature;
    double persistence;
    int seed;

    public SimplexUtil(int largestFeature,double persistence, int seed){
        this.largestFeature=largestFeature;
        this.persistence=persistence;
        this.seed=seed;

        //recieves a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves=(int)Math.ceil(Math.log10(largestFeature)/Math.log10(2));

        octaves=new SimplexNoise[numberOfOctaves];
        frequencys=new double[numberOfOctaves];
        amplitudes=new double[numberOfOctaves];

        Random rnd = new Random(seed);

        for(int i=0;i<numberOfOctaves;i++){
            octaves[i]=new SimplexNoise(rnd.nextInt());

            frequencys[i] = Math.pow(2,i);
            amplitudes[i] = Math.pow(persistence,octaves.length-i);
        }
    }


    public double getNoise(double x, double y){

        double result=0;

        for(int i=0;i<octaves.length;i++){
            //double frequency = Math.pow(2,i);
            //double amplitude = Math.pow(persistence,octaves.length-i);

            result=result+octaves[i].eval(x/frequencys[i], y/frequencys[i])* amplitudes[i];
        }


        return result;

    }

    public double getNoise(double x,double y, double z){
        double result=0;
        for(int i=0;i<octaves.length;i++){
            double frequency = Math.pow(2,i);
            double amplitude = Math.pow(persistence,octaves.length-i);

            result=result+octaves[i].eval(x/frequency, y/frequency,z/frequency)* amplitude;
        }
        return result;
    }



}

