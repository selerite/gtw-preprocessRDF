package io.gtw.infrastructure.preprocessrdf

import io.gtw.infrastructure.preprocessrdf.utils.EnvUtils


case class Param (
                 inputBase: Option[String] = EnvUtils.inputBaseFromEnv,
                 inputWikidataRDF: Option[String] = EnvUtils.inputWikidataRDFFromEnv,
                 outputBase: Option[String] = EnvUtils.outputBaseFromEnv,
                 outputWikidataRDF: Option[String] = EnvUtils.outputWikidataRDFFromEnv,
                 outputWikidataSchema: Option[String] = EnvUtils.outputWikidataSchemaFromEnv,
                 language: Option[String] = EnvUtils.languageFromEnv
                 )
