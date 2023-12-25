package io.telereso.cashconv.client

//import com.squareup.sqldelight.db.SqlDriver
//import com.squareup.sqldelight.drivers.sqljs.initSqlDriver

private external fun require(module: String): dynamic
private val fs = require("fs")

actual class Resource actual constructor(actual val name: String) {

    private val path = "../../../../build/js/packages/cashconv-client-test/kotlin/$name"

    actual fun exists(): Boolean = fs.existsSync(path) as Boolean

    actual fun readText(): String = fs.readFileSync(path, "utf8") as String
}

//actual suspend fun provideDbDriverTest(schema: SqlDriver.Schema,
//    databaseDriverFactory: CashconvClientDatabaseDriverFactory?): SqlDriver {
//    return initSqlDriver(schema).await()
//}