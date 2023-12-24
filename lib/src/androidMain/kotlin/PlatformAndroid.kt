package io.telereso.cashconv.client

import android.content.Context
import com.squareup.sqldelight.db.SqlDriver

/**
 * Android we type tyhe alias to the real Parcelable
 */
actual typealias  Parcelable = android.os.Parcelable

actual suspend fun provideDbDriver(
    schema: SqlDriver.Schema,
    databaseDriverFactory: CashconvClientDatabaseDriverFactory?
): SqlDriver {
    return databaseDriverFactory!!.createDriver()
}

actual class CashconvClientDatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        TODO("Not yet implemented")
//        return AndroidSqliteDriver(CashconvClientDatabase.Schema, context, Dao.DATABASE_NAME)
    }
}