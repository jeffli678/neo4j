/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compatibility.v3_4.runtime.slotted.pipes

import org.neo4j.cypher.internal.compatibility.v3_4.runtime.{LongSlot, SlotConfiguration, RefSlot}
import org.neo4j.cypher.internal.compatibility.v3_4.runtime.slotted.SlottedExecutionContext
import org.neo4j.cypher.internal.runtime.interpreted.ValueConversion.asValue
import org.neo4j.cypher.internal.runtime.interpreted.pipes.{Pipe, QueryState}
import org.neo4j.cypher.internal.runtime.interpreted.ExecutionContext
import org.neo4j.cypher.internal.v3_4.logical.plans.LogicalPlanId
import org.scalatest.mock.MockitoSugar

case class FakeSlottedPipe(data: Iterator[Map[String, Any]], slots: SlotConfiguration)
  extends Pipe with MockitoSugar {

  def internalCreateResults(state: QueryState): Iterator[ExecutionContext] = {
    data.map { values =>
      val result = SlottedExecutionContext(slots)

      values foreach {
        case (key, value) =>
          slots(key) match {
            case LongSlot(offset, _, _) if value == null =>
              result.setLongAt(offset, -1)

            case LongSlot(offset, _, _) =>
              result.setLongAt(offset, value.asInstanceOf[Number].longValue())

            case RefSlot(offset, _, _) =>
              result.setRefAt(offset, asValue(value))
          }
      }
      result
    }
  }

  var id = LogicalPlanId.DEFAULT
}
