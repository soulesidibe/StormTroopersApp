package com.soulesidibe.stormtroopersapp

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import kotlin.system.measureTimeMillis

/**
 * Created on 12/2/18 at 1:26 PM
 * Project name : StormTroopersApp
 */
class TimingRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                val enabled = description?.annotations?.filterIsInstance<LogTiming>()?.isNotEmpty() ?: false

                if (enabled) {
                    val time = measureTimeMillis {
                        base?.evaluate()
                    }
                    println(description?.methodName + " takes $time ms")
                } else {
                    base?.evaluate()
                }
            }

        }
    }
}

annotation class LogTiming