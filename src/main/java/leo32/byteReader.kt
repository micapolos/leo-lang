package leo32

import leo.base.*

data class ByteReader(
	val symbolReader: SymbolReader,
	val symbolOrNull: Symbol?)

val SymbolReader.byteReader
	get() =
		ByteReader(this, null)

val Empty.byteReader
	get() =
		symbolReader.byteReader

fun ByteReader.plus(byte: Byte) =
	if (byte == 0.toByte())
		symbolReader
			.plus(symbolOrNull)
			?.let { symbolReader ->
				copy(
					symbolReader = symbolReader,
					symbolOrNull = null)
			}
	else
		symbolOrNull
			.orNullPlus(byte)
			?.let { symbol ->
				copy(symbolOrNull = symbol)
			}

fun ByteReader.plus(byteSeq: Seq<Byte>): ByteReader? =
	orNullFold(byteSeq, ByteReader::plus)

fun ByteReader.plus(coreString: CoreString) =
	coreString.byteSeq.let { byteSeq ->
		orNull.fold(byteSeq) { byte ->
			this?.plus(byte)
		}
	}

val ByteReader.termOrNull
	get() =
		ifOrNull(symbolOrNull == null) {
			symbolReader.termOrNull
		}

val Term.byteReader
	get() =
		fieldReader.symbolReader.byteReader

fun Term.readByte(byteSeq: Seq<Byte>): Term =
	byteReader.plus(byteSeq)?.termOrNull!!

fun Term.read(coreString: CoreString): Term =
	byteReader.plus(coreString)?.termOrNull!!
