package com.egeuni.earthquake;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkUtilities {

    private int[][][] data = Perceptron.andData;
    private double[] weights = Perceptron.INITIAL_WEIGHTS;
    private Perceptron perceptron = new Perceptron();
    private boolean errorFlag = true;
    private int epochNumber = 0;
    private double error = 0;
    private double[] adjustedWeights = null;
    private static NeuralNetworkUtilities singletonInstance = null;

    private NeuralNetworkUtilities() {
        run();
    }

    public static NeuralNetworkUtilities getSingletonInstance() {
        if(singletonInstance == null) {
            singletonInstance = new NeuralNetworkUtilities();
        }
        return singletonInstance;
    }

    private void run() {
        while (errorFlag) {
            enterLogForEpoch(epochNumber++);
            errorFlag = false;
            error = 0;
            for(int i = 0; i < data.length; i++) {
                double weightedSum = perceptron.calculateWeightedSum(data[i][0], weights);
                Log.d("MK", "weightedSum " + weightedSum);
                int result = perceptron.applyActivationFunction(weightedSum);
                error = data[i][1][0] - result;
                if(error != 0) errorFlag = true;
                adjustedWeights = perceptron.adjustWeights(data[i][0], weights, error);
                printLogForData(data[i], weights, result, error, weightedSum, adjustedWeights);
                weights = adjustedWeights;
            }
        }
    }

    public ArrayList<Event> extractRiskyEvents(List<TaskEntry> events) {
        ArrayList<Event> result = new ArrayList<Event>();
        for (TaskEntry t: events) {
            double res = (t.getHazardDepth() * weights[0]) + (t.getHazardMag() * weights[1]);
            if(res >= 1.0) {
                Event event = new Event(t.getPlace(), t.getDate(), t.getHour(), t.getMag(), t.getDepth(),t.getLatitude(),
                        t.getLongitude(),t.getHazardDepth(),t.getHazardMag());
                result.add(event);
            }
        }

        return result;
    }



    private void enterLogForEpoch(int epochNum) {
        Log.d("Mert", "----------------------------------------- #EpochNumber " + epochNum +
            "#-----------------------------------------");
    }

    private void printLogForData(int[][] data, double[] weights, int result,
                                 double error, double weightedSum, double[] adjustedWeights) {
        Log.d("Mert","Weight 0: " + weights[0]);
        Log.d("Mert","Weight 1: " + weights[1]);
        Log.d("Mert","Data 0: " + data[0][0]);
        Log.d("Mert","Data 1: " + data[0][1]);
        Log.d("Mert","Data 2: " + data[1][0]);
        Log.d("Mert","Result: " + result);
        Log.d("Mert","Error: " + error);
        Log.d("Mert","AdjustedWeight 0: " + adjustedWeights[0]);
        Log.d("Mert","AdjustedWeight 1: " + adjustedWeights[1]);
    }
}
