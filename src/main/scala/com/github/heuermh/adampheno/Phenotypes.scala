/**
  *  Copyright 2016 held jointly by the individual authors.
  * 
  *  Licensed under the Apache License, Version 2.0 (the "License");
  *  you may not use this file except in compliance with the License.
  *  You may obtain a copy of the License at
  * 
  *  http://www.apache.org/licenses/LICENSE-2.0
  * 
  *  Unless required by applicable law or agreed to in writing, software
  *  distributed under the License is distributed on an "AS IS" BASIS,
  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *  See the License for the specific language governing permissions and
  *  limitations under the License.
  */
package com.github.heuermh.adampheno

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import org.bdgenomics.utils.cli._
import org.bdgenomics.utils.misc.Logging

import org.kohsuke.args4j.{ Argument, Option }

import org.phenopackets.api.PhenoPacket

import org.phenopackets.api.io.JsonReader
//import org.phenopackets.api.io.RdfReader
import org.phenopackets.api.io.YamlReader

import scala.collection.JavaConverters._

/**
 * Phenotypes.
 * 
 * @author  Michael Heuer
 */
object Phenotypes extends BDGCommandCompanion {
  val commandName = "adam-pheno"
  val commandDescription = "Load phenotypes from Phenotype eXchange Format (PXF) files"

  def apply(cmdLine: Array[String]) = {
    new Phenotypes(Args4j[PhenotypesArgs](cmdLine))
  }
}

/**
 * Phenotypes command line arguments.
 */
class PhenotypesArgs extends Args4jBase {
  @Option(required = true, name = "-format", usage = "Input format, one of {json, rdf, yaml} (required)")
  var inputFormat: String = _

  @Argument(required = true, metaVar = "INPUT", usage = "Input file(s) in one of the Phenotype eXchange Format (PXF) formats (required)", multiValued = true)
  var inputPaths: java.util.List[String] = _
}

/**
 * Phenotypes.
 */
class Phenotypes(val args: PhenotypesArgs) extends BDGSparkCommand[PhenotypesArgs] with Logging {
  val companion = Phenotypes

  def run(sc: SparkContext) {
    val inputPaths: Seq[String] = args.inputPaths.asScala
    def readJson(): RDD[PhenoPacket] = {
      sc.makeRDD(inputPaths.map(JsonReader.readFile(_)))
    }
    //def readRdf(): RDD[PhenoPacket] = {
    //  sc.makeRDD(Seq(RdfReader.readFile(args.inputPath)))
    //}
    def readYaml(): RDD[PhenoPacket] = {
      sc.makeRDD(inputPaths.map(YamlReader.readFile(_)))
    }

    val phenotypes = args.inputFormat match {
      case "json"        => readJson()
      //case "rdf"         => readRdf()
      case "yaml"        => readYaml()
      case _ => throw new IllegalArgumentException("Invalid input format")
    }

    System.out.println(phenotypes.count())
  }
}
