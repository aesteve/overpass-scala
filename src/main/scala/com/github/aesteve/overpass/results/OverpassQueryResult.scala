package com.github.aesteve.overpass.results

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.module.scala.JsonScalaEnumeration
import com.github.aesteve.overpass.results.NodeRole.NodeRole
import com.github.aesteve.overpass.results.OsmType.OsmType

@JsonIgnoreProperties(ignoreUnknown = true)
case class Tags(
  `type`: String,
  amenity: String,
  building: String,
  @JsonProperty("building:levels") buildingLevels: String,
  name: String,
  phone: String,
  @JsonProperty("contact:email") contactEmail: String,
  website: String,
  @JsonProperty("addr:city") addressCity: String,
  @JsonProperty("addr:postcode") addressPostCode: String,
  @JsonProperty("addr:street") addressStreet: String,
  @JsonProperty("addr:housenumber") addressHouseNumber: String,
  @JsonProperty("wheelchair") wheelchair: String,
  @JsonProperty("wheelchair:description") wheelchairDescription: String,
  @JsonProperty("opening_hours") openingHours: String,
  @JsonProperty("internet_access") internetAccess: String,
  fee: String,
  operator: String,
  source: String,
  capacity: String,
  parking: String,
  supervised: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class Point(lat: Double, lon: Double)

object NodeRole extends Enumeration {
  type NodeRole = Value
  val from, to, via, inner, outer = Value
}
class NodeRoleEnum extends TypeReference[NodeRole.type]

@JsonIgnoreProperties(ignoreUnknown = true)
case class Member(
  @JsonScalaEnumeration(classOf[OsmTypeEnum])
  `type`: OsmType,
  ref: Long,
  @JsonScalaEnumeration(classOf[NodeRoleEnum])
  role: NodeRole
)

object OsmType extends Enumeration {
  type OsmType = Value
  val node, way, relation = Value
}
class OsmTypeEnum extends TypeReference[OsmType.type]

@JsonIgnoreProperties(ignoreUnknown = true)
case class Element(
  @JsonScalaEnumeration(classOf[OsmTypeEnum])
  `type`: OsmType,
  id: Long,
  center: Point,
  lat: Double,
  lon: Double,
  tags: Tags,
  @JsonDeserialize(contentAs = classOf[Long])
  nodes: List[Long],
  members: List[Member]
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class OverpassQueryResult(
  version: String,
  generator: String,
  // TODO osm3s
  elements: List[Element]
)

