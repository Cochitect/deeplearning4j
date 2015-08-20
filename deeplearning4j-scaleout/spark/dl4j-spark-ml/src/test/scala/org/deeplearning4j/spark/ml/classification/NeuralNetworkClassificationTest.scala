package org.deeplearning4j.spark.ml.classification

import org.apache.spark.Logging
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, Updater, NeuralNetConfiguration}
import org.deeplearning4j.nn.conf.layers.{OutputLayer, RBM}
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.spark.sql.sources.iris._
import org.deeplearning4j.spark.util.TestSparkContext
import org.junit.runner.RunWith
import org.nd4j.linalg.lossfunctions.LossFunctions
import org.scalatest._
import org.scalatest.junit.JUnitRunner
import org.springframework.core.io.ClassPathResource

/**
 * Test classification.
 */
@RunWith(classOf[JUnitRunner])
class NeuralNetworkClassificationTest
  extends FunSuite with TestSparkContext with Logging with Matchers {

  private def getConfiguration(): MultiLayerConfiguration = {
    new NeuralNetConfiguration.Builder()
      .seed(11L)
      .iterations(100)
      .weightInit(WeightInit.XAVIER)
      .activationFunction("relu")
      .k(1)
      .lossFunction(LossFunctions.LossFunction.RMSE_XENT)
      .learningRate(1e-3f)
      .optimizationAlgo(OptimizationAlgorithm.LINE_GRADIENT_DESCENT)
      .momentum(0.9)
      .updater(Updater.ADAGRAD)
      .constrainGradientToUnitNorm(true)
      .dropOut(0.5)
      .useDropConnect(true)
      .list(2)
      .layer(0, new RBM.Builder(RBM.HiddenUnit.RECTIFIED, RBM.VisibleUnit.GAUSSIAN)
        .nIn(4).nOut(3).build())
      .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MCXENT)
        .nIn(3).nOut(3).activation("softmax").build())
      .build()
  }

  test("iris") {
    val conf = getConfiguration()

    val path = new ClassPathResource("data/irisSvmLight.txt").getFile.toURI.toString
    val dataFrame = sqlContext.read.iris(path)
    val Array(trainDF, testDF) = dataFrame.randomSplit(Array(.6, .4), 11L)

    val classification = new NeuralNetworkClassification()
      .setFeaturesCol("features").setLabelCol("label")
      .setConf(conf)

    val model = classification.fit(trainDF)
    val predictions = model.transform(testDF)

    predictions.col("rawPrediction") should not be (null)
    predictions.col("prediction") should not be (null)
    predictions.show()
  }
}
