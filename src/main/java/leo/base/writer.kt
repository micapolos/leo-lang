package leo.base

data class Writer<in V>(
	val writeFn: (V) -> Writer<V>)

fun <V> Writer<V>.write(value: V): Writer<V> =
	writeFn(value)

fun <V> ignoreWriter(): Writer<V> =
	Writer { ignoreWriter() }

fun <V> printlnWriter(): Writer<V> =
	Writer { println(it); printlnWriter() }

fun <V> Writer<V>.write(stream: Stream<V>?): Writer<V> =
	fold(stream, Writer<V>::write)

fun <A, B> Writer<B>.map(fn: (A) -> B): Writer<A> =
	Writer { write(fn(it)).map(fn) }

val <V> Writer<V>.streamJoin: Writer<Stream<V>?>
	get() =
		Writer { stream -> write(stream).streamJoin }

val Writer<EnumBit>.bitByteWriter: Writer<Byte>
	get() =
		streamJoin.map(Byte::bitStream)

val Writer<Byte>.byteBitWriter: Writer<EnumBit>
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
	nullOf<Stack<V>>().writerFold(Stack<V>?::push, fn)

val <V> Processor<*, V>.writer: Writer<V>
	get() =
		Writer { value ->
			process(value).writer
		}

fun <I, O> Writer<O>.split(fn1: (I) -> O, fn2: (I) -> O): Writer<I> =
	Writer { value ->
		write(fn1(value)).write(fn2(value)).split(fn1, fn2)
	}

fun <I : Any, O> Writer<O>.join(fn: (I, I) -> O): Writer<I> =
	join(null, fn)

fun <I : Any, O> Writer<O>.join(firstOrNull: I?, fn: (I, I) -> O): Writer<I> =
	Writer { value ->
		if (firstOrNull == null) join(value, fn)
		else write(fn(firstOrNull, value)).join(null, fn)
	}
