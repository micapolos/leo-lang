package leo32.dsl.demo

import leo32.dsl.doesEqual
import leo32.dsl.one
import leo32.runtime.invoke

fun main() {
	println(invoke(one(), doesEqual(one())))
}