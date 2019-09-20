package leo13.base

import leo13.Stack
import leo13.map
import leo13.script.*
import leo13.script.Writer

val stackName = "list"

fun <V> stackReader(valueReader: Reader<V>): Reader<Stack<V>> =
	reader(valueReader.name) {
		unsafeOnlyLine
			.unsafeRhs(stackName)
			.lineStack.map { valueReader.unsafeValue(this) }
	}

fun <V> stackWriter(valueWriter: Writer<V>): Writer<Stack<V>> =
	writer(valueWriter.name) {
		script(stackName lineTo map { valueWriter.scriptLine(this) }.script)
	}
