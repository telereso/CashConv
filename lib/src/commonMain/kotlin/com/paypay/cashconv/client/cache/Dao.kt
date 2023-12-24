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