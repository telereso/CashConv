package io.telereso.cashconv.client.cache
//
//import io.telereso.cashconv.client.CashconvClientDatabaseDriverFactory
//import io.telereso.cashconv.client.cache.CashconvClientDatabase
//import com.squareup.sqldelight.db.SqlDriver
//
//internal class SharedDatabase(
//    private val driverProvider: suspend (schema: SqlDriver.Schema, databaseDriverFactory: CashconvClientDatabaseDriverFactory?) -> SqlDriver,
//    private val databaseDriverFactory: CashconvClientDatabaseDriverFactory?
//) {
//    private var database: CashconvClientDatabase? = null
//
//    private suspend fun initDatabase() {
//        if (database == null) {
//            database = Dao.getDatabase(driverProvider(CashconvClientDatabase.Schema, databaseDriverFactory))
//        }
//    }
//
//    suspend operator fun <R> invoke(block: suspend (CashconvClientDatabase) -> R): R {
//        initDatabase()
//        return block(database!!)
//    }
//
//    suspend fun <R> queries(block: suspend (CashconvClientDatabaseQueries) -> R): R {
//        initDatabase()
//        return block(database!!.cashconvClientDatabaseQueries)
//    }
//}
