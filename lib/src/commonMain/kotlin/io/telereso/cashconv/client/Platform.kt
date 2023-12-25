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

import io.telereso.kmp.core.Settings
import com.squareup.sqldelight.db.SqlDriver


// For Android Parcelable
/**
 * By declaring an empty expected interface in our common sourceset, we will be able to write classes that implement Parcelable.
 */
expect interface Parcelable

/**
 * // For Android Parcelize
 * @OptionalExpectation is not supported for interfaces take note
 * @OptionalExpectation helps us make actual implementations optional on target platform.
 * Here is an example where we expect and actual Parcelize for Android Platforms in this case
 * a Parcelize annotation but the rest like iOS it will be optional.
 * this way the IDE wont warn we missed an actual.
 */
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class CommonParcelize()

expect suspend fun provideDbDriver(
    schema: SqlDriver.Schema,
    databaseDriverFactory: CashconvClientDatabaseDriverFactory?
): SqlDriver

/**
 * an abstract factory for database drivers.
 * SQLDelight provides multiple platform-specific implementations of the SQLite driver, so you need to create them for each platform separately.
 */
expect class CashconvClientDatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

val settings: Settings = Settings.get()