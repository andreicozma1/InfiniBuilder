package app.algorithms;

import java.util.Random;

public class SimplexUtil {

    // These arrays are used to be able to interpolate multiple simplex noise algorithms
    // for more realistic terrain generation
    SimplexNoise[] octaves;
    double[] frequencies;
    double[] amplitudes;

    // These global variables hold the settings passed to the SimplexUtil constructor
    int largestFeature;
    double persistence;
    int seed;

    public SimplexUtil(int largestFeature, double persistence, int seed) {
        this.largestFeature = largestFeature;
        this.persistence = persistence;
        this.seed = seed;

        // receives a number (eg 128) and calculates what power of 2 it is (eg 2^7)
        int numberOfOctaves = (int) Math.ceil(Math.log10(largestFeature) / Math.log10(2));

        // initialize our arrays based on the number of octaves calculated above
        octaves = new SimplexNoise[numberOfOctaves];
        frequencies = new double[numberOfOctaves];
        amplitudes = new double[numberOfOctaves];

        // Create a new random number generator based on our given seed.
        // This will in turn always generate the same octaves when the same seed
        // is passed to the constructor
        Random rnd = new Random(seed);

        // Iterate through our number of octaves,
        // create a new instance of our SimplexNoise algorithm based on the new seed
        for (int i = 0; i < numberOfOctaves; i++) {
            octaves[i] = new SimplexNoise(rnd.nextInt());
            // at each iteration, save the values corresponding to our calculations
            frequencies[i] = Math.pow(2, i);
            amplitudes[i] = Math.pow(persistence, octaves.length - i);
        }
    }


    /**
     * Primary getNoise function used in EnvironmentUtil to retrieve a Y terrain coordinate
     * based on two given axis coordinates. (In environment we use X and Z)
     * @param x
     * @param y
     * @return
     */
    public double getNoise(double x, double y) {
        double result = 0;

        for (int i = 0; i < octaves.length; i++) {
            // For all of our octaves, evaluate our SimplexNoise algorithm
            // perform transformations based on the given frequencies and amplitudes previously calculated
            result = result + octaves[i].eval(x / frequencies[i], y / frequencies[i]) * amplitudes[i];
        }

        return result;
    }

    /**
     * 3D variation of the noise algorithm.
     * Instead of returning a Y coordinate as a height map,
     * this function's output can be determined as a 'density' at a given 3D point in space.
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double getNoise(double x, double y, double z) {
        double result = 0;
        for (int i = 0; i < octaves.length; i++) {
            double frequency = Math.pow(2, i);
            double amplitude = Math.pow(persistence, octaves.length - i);

            result = result + octaves[i].eval(x / frequency, y / frequency, z / frequency) * amplitude;
        }
        return result;
    }
}

