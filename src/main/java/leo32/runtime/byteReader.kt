package leo32.runtime

import leo.base.Empty
import leo.base.fold
import leo.base.orNull
import leo.base.replace
import leo.binary.utf8ByteSeq

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

fun ByteReader.plus(string: String) =
	string
		.utf8ByteSeq
		.replace(32.toByte() to 0.toByte())
		.let { byteSeq ->
			orNull.fold(byteSeq) { byte ->
				this?.plus(byte)
			}
		}
