package com.github.aesteve.overpass

import java.net.URLEncoder

import io.vertx.scala.core.MultiMap

package object utils {

  implicit class RichMultiMap(map: Map[String, String]) {

    def multi: MultiMap = {
      val m = MultiMap.caseInsensitiveMultiMap
      map.foreach(pair => m.set(pair._1, pair._2))
      m
    }

  }

}
