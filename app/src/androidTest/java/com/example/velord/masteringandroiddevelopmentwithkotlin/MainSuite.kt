package com.example.velord.masteringandroiddevelopmentwithkotlin

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    ExampleInstrumentedTest::class,
    MainServiceTest::class
)
class MainSuite