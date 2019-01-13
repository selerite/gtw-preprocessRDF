package io.gtw.infrastructure.preprocessrdf

import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scopt.OptionParser

object PreprocessRDF extends LazyLogging {
  def main(args: Array[String]) {
    val parser = new OptionParser[Param] ("scopt") {
      head("Attribution Module")
      opt[String]('i', "inputBase").action((x, c) => c.copy(inputBase = Some(x))).text("input base. [HDFS address].")
      opt[String]('d', "inputWikidataRDF").action((x, c) => c.copy(inputWikidataRDF = Some(x))).text("input wikidata path.")
      opt[String]('o', "outputBase").action((x, c) => c.copy(outputBase = Some(x))).text("output base. [HDFS address].")
      opt[String]('t', "outputWikidataRDF").action((x, c) => c.copy(outputWikidataRDF = Some(x))).text("output rdf path of the result that need put to.")
      opt[String]('s', "outputWikidataSchema").action((x, c) => c.copy(outputWikidataSchema = Some(x))).text("output schema path of the result that need put to.")
      opt[String]('l', "language").action((x, c) => c.copy(language = Some(x))).text("language supported.")
    }
    parser.parse(args, Param()) match {
      case Some(param) =>
        run(param)
      case None =>
        parser.showUsageAsError()
        logger.error("parser is error.")
    }
  }

  def run(param: Param): Unit = {
    val inputBase: Option[String] = param.inputBase
    val inputWikidataRDF: Option[String] = param.inputWikidataRDF
    val outputBase: Option[String] = param.outputBase
    val outputWikidataRDF: Option[String] = param.outputWikidataRDF
    val outputWikidataSchema: Option[String] = param.outputWikidataSchema
    val language: Option[String] = param.language

    val inputSource: String = inputBase match {
      case Some(x) => x
      case None => ""
    }

    val inputWikidataRDFPath: String = inputWikidataRDF match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"set env or cmd args.")
    }

    val outputSource: String = outputBase match {
      case Some(x) => x
      case None => ""
    }

    val outputWikidataRDFPath: String = outputWikidataRDF match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"set env or cmd args.")
    }

    val outputWikidataSchemaPath: String = outputWikidataSchema match {
      case Some(x) => x
      case None => throw new IllegalArgumentException(s"set env or cmd args.")
    }

    val languageList: Array[String] = language match {
      case Some(x) => x.split(',')
      case None => Array()
    }

    val inputWikidataRDFAddress: String = inputSource + inputWikidataRDFPath
    val outputWikidataRDFAddress: String = outputSource + outputWikidataRDFPath
    val outputWikidataSchemaAddress: String = outputSource + outputWikidataSchemaPath

    val sparkConf = new SparkConf().setAppName("Attribution")
    // val sparkConf = new SparkConf().setAppName("Attribution").setMaster("local[1]")
    val sparkContext = new SparkContext(sparkConf)

    /* - remove external object type - */
    val wikidataSourceRDFMapRDD: RDD[String] = sparkContext.textFile(inputWikidataRDFAddress)
    val wikidataCleanedRDD = wikidataSourceRDFMapRDD.map(removeExternalInformation(_, languageList)).filter(_ != "")

    /* - output cleaned data - */
    // wikidataCleanedRDD.foreach(println)
    wikidataCleanedRDD.saveAsTextFile(outputWikidataRDFAddress)

    /* - generate schema - */
    val schemaRDD: RDD[String] = wikidataSourceRDFMapRDD.map(generate_schema).distinct().filter(_ != "").repartition(1)
    /* - output schema - */
    // schemaRDD.foreach(println)
    schemaRDD.saveAsTextFile(outputWikidataSchemaAddress)

  }

  def removeExternalInformation(line: String, languageList: Array[String]): String = {
    val indexOfExternal: Int = line.indexOf("^^")
    val cleanedLine: String = indexOfExternal match {
      case x if x >= 0 => line.substring(0, x) + " ."
      case x if x < 0 => line
    }
    if (!cleanedLine.contains("\"@"))
      cleanedLine
    else {
      for (language <- languageList)
        if (cleanedLine.contains("\"@" + language))
          return line
      ""
    }
  }

  def generate_schema(line: String): String = {
    val tokens: Array[String] = line.split(" ", 3)
    val predicate: String = tokens(1)
    val objectt_with_dot: String =  tokens(2)
    if (objectt_with_dot.contains("\"@"))
      predicate + ": string @lang ."
    else
      ""
  }
}
