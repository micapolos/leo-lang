package leo13.base

import leo13.script.*
import leo13.script.Writer
import leo9.Stack
import leo9.map

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
