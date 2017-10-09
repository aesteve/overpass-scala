package com.github.aesteve.overpass

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.github.aesteve.overpass.results.OverpassQueryResult
import com.github.aesteve.overpass.utils._
import com.google.gson.Gson
import hu.supercluster.overpasser.library.query.OverpassQuery
import io.vertx.core.http.HttpHeaders._
import io.vertx.core.json.Json
import io.vertx.scala.core.Vertx
import io.vertx.scala.ext.web.client.{WebClient, WebClientOptions}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class OverpassClient(val vertx: Vertx, val backend: String, val searchPath: String) {

  private val opts =
    WebClientOptions().setDefaultHost(backend)
  private val client =
    WebClient.create(vertx, opts)

  private val mapper = Json.mapper.registerModule(DefaultScalaModule)

  def query(query: OverpassQuery): Future[Option[OverpassQueryResult]] =
    client.get(searchPath)
    .putHeader(CONTENT_TYPE.toString, "application/x-www-form-urlencoded")
    .putHeader(ACCEPT.toString, "application/json")
    //.putHeader(ORIGIN.toString, "chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop")
    //.putHeader(USER_AGENT.toString, "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36")
    .addQueryParam("data", query.build.replaceAll("\"", ""))
    .sendFuture
    .map {
      case resp if resp.statusCode == 200 =>
        Some(Json.decodeValue(resp.bodyAsString("UTF-8").get, classOf[OverpassQueryResult]))
      case _ => None
    }


}


object OverpassClient {

  def apply(backend: String, apiKey: String): OverpassClient =
    new OverpassClient(Vertx.vertx, backend, apiKey)

  def apply(vertx: Vertx, backend: String, apiKey: String): OverpassClient =
    new OverpassClient(vertx, backend, apiKey)

}