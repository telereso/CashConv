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
