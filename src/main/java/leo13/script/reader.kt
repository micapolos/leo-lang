package leo13.script

import leo13.ScriptException
import leo13.fail
import leo9.*

data class Reader<out V>(val name: String, val unsafeBodyValueFn: Script.() -> V)

fun <V> reader(name: String, unsafeBodyValueFn: Script.() -> V) = Reader(name, unsafeBodyValueFn)

fun <V> Reader<V>.unsafeBodyValue(script: Script) =
	try {
		script.unsafeBodyValueFn()
	} catch (scriptException: ScriptException) {
		fail<V>(scriptException.script.plus("read" lineTo script(name)))
	}

fun <V> Reader<V>.unsafeValue(line: ScriptLine) = line.unsafeRhs(name).unsafeBodyValueFn()
fun <V> Reader<V>.unsafeValue(script: Script) = unsafeValue(script.unsafeOnlyLine)
fun <V> Reader<V>.unsafeBodyValue(line: ScriptLine) = unsafeBodyValue(script(line))

fun <V> reader(name: String, value: V) = reader(name) { unsafeEmpty.run { value } }
fun <V> reader(name: String, reader: Reader<V>) = reader(name) { reader.unsafeValue(unsafeOnlyLine) }

fun <V, A> stackReader(name: String, itemReader: Reader<A>, fn: Stack<A>.() -> V): Reader<V> =
	reader(name) {
		lineStack.map { itemReader.unsafeValue(this) }.fn()
	}

fun <V : Any> reader(name: String, vararg cases: ReaderCase<V, *>): Reader<V> =
	reader(name) {
		unsafeOnlyLine.let { line ->
			stack(*cases).mapFirst { unsafeValueOrNull(line) } ?: fail("exhaust")
		}
	}

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

fun <V : Any, V1, V2, V3, V4, V5, V6, V7, V8> reader(
	name: String,
	reader1: Reader<V1>,
	reader2: Reader<V2>,
	reader3: Reader<V3>,
	reader4: Reader<V4>,
	reader5: Reader<V5>,
	reader6: Reader<V6>,
	reader7: Reader<V7>,
	reader8: Reader<V8>,
	fn: (V1, V2, V3, V4, V5, V6, V7, V8) -> V): Reader<V> =
	reader(name) {
		lineStack
			.map8OrNull { line1, line2, line3, line4, line5, line6, line7, line8 ->
				fn(
					reader1.unsafeValue(line1),
					reader2.unsafeValue(line2),
					reader3.unsafeValue(line3),
					reader4.unsafeValue(line4),
					reader5.unsafeValue(line5),
					reader6.unsafeValue(line6),
					reader7.unsafeValue(line7),
					reader8.unsafeValue(line8))
			}
			?: fail(name)
	}
