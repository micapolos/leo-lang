package leo13.script

import leo13.fail
import leo9.map1OrNull
import leo9.map2OrNull

data class Reader<out V>(val name: String, val unsafeBodyValueFn: Script.() -> V)

fun <V> reader(name: String, unsafeBodyValueFn: Script.() -> V) = Reader(name, unsafeBodyValueFn)
fun <V> Reader<V>.unsafeBodyValue(script: Script) = script.unsafeBodyValueFn()
fun <V> Reader<V>.unsafeValue(line: ScriptLine) = line.unsafeRhs(name).unsafeBodyValueFn()
fun <V> Reader<V>.unsafeValue(script: Script) = unsafeValue(script.unsafeOnlyLine)

fun <V> reader(name: String, value: V) = reader(name) { unsafeEmpty.run { value } }
fun <V> reader(name: String, reader: Reader<V>) = reader(name) { reader.unsafeValue(unsafeOnlyLine) }

fun <V : Any, V1> reader(
	name: String,
	reader1: Reader<V1>,
	fn: (V1) -> V): Reader<V> =
	reader(name) {
		lineStack
			.map1OrNull { line1 ->
				fn(reader1.unsafeValue(line1))
			}
			?: fail(name)
	}

fun <V : Any, V1, V2> reader(
	name: String,
	reader1: Reader<V1>,
	reader2: Reader<V2>,
	fn: (V1, V2) -> V): Reader<V> =
	reader(name) {
		lineStack
			.map2OrNull { line1, line2 ->
				fn(
					reader1.unsafeValue(line1),
					reader2.unsafeValue(line2))
			}
			?: fail(name)
	}