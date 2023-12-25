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

package io.telereso.cashconv.client

import com.squareup.sqldelight.Transacter
import com.squareup.sqldelight.db.SqlCursor
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.db.SqlPreparedStatement
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await


actual interface Parcelable

actual suspend fun provideDbDriver(
    schema: SqlDriver.Schema,
    databaseDriverFactory: CashconvClientDatabaseDriverFactory?
): SqlDriver {

    return initSqlDriver(schema).await()
}

actual class CashconvClientDatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // Dumpy object, for js will be using provideDbDriver(SqlDriver.Schema)
        return object : SqlDriver {
            override fun close() {

            }

            override fun currentTransaction(): Transacter.Transaction? {
                TODO("Not yet implemented")
            }

            override fun execute(
                identifier: Int?,
                sql: String,
                parameters: Int,
                binders: (SqlPreparedStatement.() -> Unit)?
            ) {

            }

            override fun executeQuery(
                identifier: Int?,
                sql: String,
                parameters: Int,
                binders: (SqlPreparedStatement.() -> Unit)?
            ): SqlCursor {
                TODO("Not yet implemented")
            }

            override fun newTransaction(): Transacter.Transaction {
                TODO("Not yet implemented")
            }
        }
    }
}