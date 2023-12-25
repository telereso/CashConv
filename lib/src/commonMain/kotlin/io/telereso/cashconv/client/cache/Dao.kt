/*
 * MIT License
 *
 * Copyright (c) 2023 Telereso
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.telereso.cashconv.client.cache
//
//import com.squareup.sqldelight.ColumnAdapter
//import com.squareup.sqldelight.db.SqlDriver
//import io.telereso.cashconv.models.RocketLaunch
//import io.telereso.cashconv.models.fromJson
//import io.telereso.cashconv.models.toJson
//
//
//
///**
// * This class's visibility is set to internal, which means it is only accessible from within the multiplatform module.
// */
//internal class Dao(val database: SharedDatabase) {
//
//    companion object {
//        const val DATABASE_NAME = "cashconv-client.db"
//
//        // TODO Remove sample
////        val rocketLaunchAdapter = object : ColumnAdapter<RocketLaunch, String> {
////            override fun decode(databaseValue: String): RocketLaunch {
////                return if (databaseValue.isEmpty()) {
////                    RocketLaunch()
////                } else {
////                    RocketLaunch.fromJson(databaseValue)
////                }
////            }
////
////            override fun encode(value: RocketLaunch): String {
////                return value.toJson()
////            }
////        }
//
////        fun getDatabase(databaseDriver: SqlDriver): CashconvClientDatabase {
////
////            return CashconvClientDatabase(
////                databaseDriver,
////                RocketLaunchT.Adapter(rocketLaunchAdapter)
////            )
////        }
//    }
//
//    /**
//     * Add a function to clear all the tables in the database in a single SQL transaction
//     */
//    internal suspend fun clearDatabase() {
//        database.queries {
//            it.transaction {
//                it.removeAllRocketLaunch()
//            }
//        }
//    }
//
//    // TODO Remove sample
//    internal suspend fun getAllRocketLaunches(): List<RocketLaunch> {
//        return database.queries {
//            it.selectAllRocketLaunch().executeAsList().map { r ->
//                r.rocketLaunch!!
//            }
//        }
//    }
//
//    // TODO Remove sample
//    internal suspend fun insertRocketLaunches(launches: List<RocketLaunch>) {
//        database.queries {
//            it.transaction {
//                launches.forEachIndexed { index, rocketLaunch ->
//                    it.insertRocketLaunch(index.toLong(), rocketLaunch)
//                }
//            }
//        }
//    }
//}