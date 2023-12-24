package io.telereso.cashconv.client.cache

//
//class DaoTest {
//
//    private val dao: Dao = Dao(SharedDatabase(::provideDbDriverTest, null))
//
//    @BeforeTest
//    fun before() {
//
//    }
//
//    @AfterTest
//    fun after() {
//
//    }
//
//    @Test
//    fun shouldInsertRockets() = runTest {
//        dao.database.queries {
//            it.insertRocketLaunch(2, RocketLaunch(mission_name = "Mars and Beyond"))
//            it.selectAllRocketLaunch().executeAsList().shouldNotBeEmpty()
//        }
//    }
//
//    @Test
//    fun shouldSelectAllRockets() = runTest {
//        dao.database.queries {
//            it.insertRocketLaunch(2, RocketLaunch(mission_name = "Mars and Beyond"))
//            it.insertRocketLaunch(5, RocketLaunch(mission_name = "Venus and Back"))
//            with(it.selectAllRocketLaunch().executeAsList()) {
//                shouldNotBeEmpty()
//                size.shouldBe(2)
//                first().rocketLaunch?.mission_name.shouldBe("Mars and Beyond")
//            }
//        }
//    }
//
//    @Test
//    fun shouldClearDatabase() = runTest {
//        dao.database.queries {
//            with(it) {
//                insertRocketLaunch(5, RocketLaunch(mission_name = "Mission Impossible"))
//                removeAllRocketLaunch()
//                selectAllRocketLaunch().executeAsList().shouldBeEmpty()
//            }
//        }
//    }
//}