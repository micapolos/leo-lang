package leo.base

data class Writer<V>(
	val writeFn: (V) -> Writer<V>)

fun <V> Writer<V>.write(value: V): Writer<V> =
	writeFn(value)

fun <V> nullWriter(): Writer<V> =
	Writer { nullWriter() }

fun <V> printlnWriter(): Writer<V> =
	Writer { println(it); printlnWriter() }

fun <V> Writer<V>.write(stream: Stream<V>) =
	fold(stream, Writer<V>::write)

fun <A, B> Writer<B>.map(fn: (A) -> B): Writer<A> =
	Writer { write(fn(it)).map(fn) }

val <V> Writer<V>.streamJoin: Writer<Stream<V>>
	get() =
		Writer { stream ->
			stream
				.foldFirst { value -> write(value) }
				.foldNext { value -> write(value) }
				.streamJoin
		}

val Writer<Bit>.bitByteWriter: Writer<Byte>
	get() =
		streamJoin.map(Byte::bitStream)

val Writer<Byte>.byteBitWriter: Writer<Bit>
	get() =
		Writer { bit7 ->
			Writer { bit6 ->
				Writer { bit5 ->
					Writer { bit4 ->
						Writer { bit3 ->
							Writer { bit2 ->
								Writer { bit1 ->
									Writer { bit0 ->
										write(byte(bit7, bit6, bit5, bit4, bit3, bit2, bit1, bit0)).byteBitWriter
									}
								}
							}
						}
					}
				}
			}
		}

fun <R, V> R.writerFold(fn: R.(V) -> R, writerFn: Writer<V>.() -> Unit): R {
	var state = this
	lateinit var writer: Writer<V>
	writer = Writer { value -> state = fn.invoke(state, value); writer }
	writerFn(writer)
	return state
}

fun <V> writeStackOrNull(fn: Writer<V>.() -> Unit): Stack<V>? =
	nullStack<V>().writerFold(Stack<V>?::push, fn)