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

import com.squareup.sqldelight.db.SqlDriver

import kotlin.test.BeforeTest

/**
 * To run the test on KOver run in gradle this command
 *./gradlew :sdk:koverHtmlReport
 * RTunning this will oinlyt run tests for the sdk variant rather
 * than runing for all debug release and sdk
it will gneerate an html repoirt with test coverage
 */
class TestUtils {
    @BeforeTest
    fun before() {
    }

    companion object{
        const val RESOURCE_PATH = "./src/commonTest/resources"
    }
}

expect suspend fun provideDbDriverTest(
    schema: SqlDriver.Schema,
    databaseDriverFactory: CashconvClientDatabaseDriverFactory?
): SqlDriver

expect class Resource(name: String) {
    val name: String

    fun exists(): Boolean
    fun readText(): String
}
