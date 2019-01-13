package io.gtw.infrastructure.preprocessrdf.utils

import scala.util.Properties

object EnvUtils {
  def inputBaseFromEnv: Option[String] = Properties.envOrNone("PREPROCESSRDF_INPUT_BASE")
  def inputWikidataRDFFromEnv: Option[String] = Properties.envOrSome("PREPROCESSRDF_INPUT_WIKIDATA_RDF", Some("E:\\gtw\\data\\sample.nt"))
  def outputBaseFromEnv: Option[String] = Properties.envOrNone("PREPROCESSRDF_OUTPUT_BASE")
  def outputWikidataRDFFromEnv: Option[String] = Properties.envOrSome("PREPROCESSRDF_OUTPUT_WIKIDATA_RDF", Some("xxx"))
  def outputWikidataSchemaFromEnv: Option[String] = Properties.envOrSome("PREPROCESSRDF_OUTPUT_WIKIDATA_SCHEMA", Some("xxx"))
  def languageFromEnv: Option[String] = Properties.envOrSome("PREPROCESSRDF_LANGUAGE", Some("en,zh"))
}
