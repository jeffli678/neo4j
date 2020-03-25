/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher

import org.neo4j.configuration.GraphDatabaseSettings
import org.neo4j.configuration.GraphDatabaseSettings.SYSTEM_DATABASE_NAME
import org.neo4j.graphdb.config.Setting

class CommunityWritePrivilegeAdministrationCommandAcceptanceTest extends CommunityAdministrationCommandAcceptanceTestBase {

  override def databaseConfig(): Map[Setting[_], Object] = super.databaseConfig() ++ Map(GraphDatabaseSettings.auth_enabled -> java.lang.Boolean.TRUE)

  test("should fail on granting write privileges from community") {
    // GIVEN
    selectDatabase(SYSTEM_DATABASE_NAME)

    // THEN
    assertFailure("GRANT WRITE ON GRAPH * ELEMENTS * (*) TO custom", "Unsupported administration command: GRANT WRITE ON GRAPH * ELEMENTS * (*) TO custom")
  }

  test("should fail on denying write privileges from community") {
    // GIVEN
    selectDatabase(SYSTEM_DATABASE_NAME)

    // THEN
    assertFailure("DENY WRITE ON GRAPH * ELEMENTS * (*) TO custom", "Unsupported administration command: DENY WRITE ON GRAPH * ELEMENTS * (*) TO custom")
  }

  test("should fail on revoking grant write privileges from community") {
    // GIVEN
    selectDatabase(SYSTEM_DATABASE_NAME)

    // THEN
    assertFailure("REVOKE GRANT WRITE ON GRAPH * ELEMENTS * (*) FROM custom", "Unsupported administration command: REVOKE GRANT WRITE ON GRAPH * ELEMENTS * (*) FROM custom")
  }

  test("should fail on revoking deny write privilege from community") {
    // GIVEN
    selectDatabase(SYSTEM_DATABASE_NAME)

    // THEN
    assertFailure("REVOKE DENY WRITE ON GRAPH * ELEMENTS * (*) FROM custom", "Unsupported administration command: REVOKE DENY WRITE ON GRAPH * ELEMENTS * (*) FROM custom")
  }

  test("should fail on revoking write privilege from community") {
    // GIVEN
    selectDatabase(SYSTEM_DATABASE_NAME)

    // THEN
    assertFailure("REVOKE WRITE ON GRAPH * ELEMENTS * (*) FROM custom", "Unsupported administration command: REVOKE WRITE ON GRAPH * ELEMENTS * (*) FROM custom")
    assertFailure("REVOKE WRITE ON GRAPH * ELEMENTS * (*) FROM $custom", "Unsupported administration command: REVOKE WRITE ON GRAPH * ELEMENTS * (*) FROM $custom")
  }
}
