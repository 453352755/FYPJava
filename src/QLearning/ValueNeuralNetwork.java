/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QLearning;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

/**
 *
 * @author dong
 */
public class ValueNeuralNetwork {

    public static void main(String[] args) {

// create training set (logical XOR function)
        DataSet trainingSet = new DataSet(6, 1);
        trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));


// create multi layer perceptron
        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 6, 2, 1);
// learn the training set
        myMlPerceptron.learn(trainingSet);

// test perceptron
        System.out.println("Testing trained neural network");
        testNeuralNetwork(myMlPerceptron, trainingSet);

// save trained neural network
        myMlPerceptron.save("myMlPerceptron.nnet");

// load saved neural network
        NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

// test loaded neural network
        System.out.println("Testing loaded neural network");
        testNeuralNetwork(loadedMlPerceptron, trainingSet);

    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

        for (DataSetRow dataRow : testSet.getRows()) {
            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
            System.out.println(" Output: " + Arrays.toString(networkOutput));
        }

    }
}
