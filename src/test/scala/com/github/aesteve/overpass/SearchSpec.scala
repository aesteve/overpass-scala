package com.github.aesteve.overpass

import com.github.aesteve.overpass.results.OsmType
import hu.supercluster.overpasser.library.query.OverpassQuery
import hu.supercluster.overpasser.library.output.OutputFormat.JSON
import hu.supercluster.overpasser.library.output.OutputVerbosity.BODY
import hu.supercluster.overpasser.library.output.OutputModificator.CENTER
import hu.supercluster.overpasser.library.output.OutputOrder.QT
import org.scalatest.{AsyncFlatSpec, Inspectors, Matchers}

class SearchSpec extends AsyncFlatSpec with Matchers with Inspectors {

  val overpass = OverpassClient("overpass-api.de", "/api/interpreter")

  "We " should " be able to make a simple search" in {
    val query = new OverpassQuery()
      .format(JSON)
      .timeout(30)
      .filterQuery()
      .node()
      .amenity("parking")
      .tagNot("access", "private")
      .boundingBox(
        47.48047027491862, 19.039797484874725,
        47.51331674014172, 19.07404761761427
      )
      .end()
      .output(BODY, CENTER, QT, 100)
    overpass.query(query)
      .map { res =>
        assert(res.isDefined)
        forAll(res.get.elements) { elem =>
          assert(elem.id > 0)
          elem.`type` match {
            case OsmType.node =>
              assert(elem.lat > 0.0d)
              assert(elem.lon > 0.0d)
            case OsmType.way =>
              assert(elem.center != null)
              assert(elem.center.lat > 0.0d)
              assert(elem.center.lon > 0.0d)
              forAll(elem.nodes) { n =>
                assert(n > 0)
              }
            case OsmType.relation =>
              assert(elem.center != null)
              assert(elem.center.lat > 0.0d)
              assert(elem.center.lon > 0.0d)
              forAll(elem.members) { member =>
                assert(member.ref > 0L)
              }
          }
        }
      }
  }

}
