@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package leo32.runtime

import leo.base.*
import leo.binary.utf8ByteSeq

data class LeoReader(
	val byteReader: ByteReader,
	val leadingTabStackStackOrNull: Stack<Stack<Tab>>?,
	val trailingTabStackStackOrNull: Stack<Stack<Tab>>?)

val ByteReader.leoReader
	get() =
		LeoReader(this, null, null)

val Empty.leoReader
	get() =
		byteReader.leoReader

fun LeoReader.plus(string: String) =
	orNull
		.fold(string.utf8ByteSeq) { byte ->
			this?.plus(byte)
		}

fun LeoReader.plus(byte: Byte): LeoReader? =
	when (byte) {
		' '.clampedByte -> plusSpace
		'\t'.clampedByte -> plusTab
		'\n'.clampedByte -> plusNewline
		0.clampedByte -> null
		else -> plusOther(byte)
	}

val LeoReader.plusSpace: LeoReader?
	get() =
		ifOrNull(trailingTabStackStackOrNull == null && byteReader.symbolOrNull != null) {
			byteReader
				.plus(0)
				?.let { byteReader ->
					LeoReader(
						byteReader,
						leadingTabStackStackOrNull.push(stack(tab)),
						null)
				}
		}

val LeoReader.plusTab: LeoReader?
	get() =
		ifOrNull(byteReader.symbolOrNull == null) {
			trailingTabStackStackOrNull
				?.let { trailingTabStackStack ->
					LeoReader(
						byteReader,
						leadingTabStackStackOrNull.push(trailingTabStackStack.head),
						trailingTabStackStack.tail)
				}
		}

val LeoReader.plusNewline: LeoReader?
	get() =
		when {
			byteReader.symbolOrNull == null && leadingTabStackStackOrNull == null ->
				this
			byteReader.symbolOrNull != null && trailingTabStackStackOrNull == null ->
				byteReader
					.plus(0)
					?.let { byteReader ->
						LeoReader(
							byteReader,
							null,
							leadingTabStackStackOrNull.push(stack(tab)).reverse)
					}
			else -> null
		}

fun LeoReader.plusOther(byte: Byte) =
	byteReader
		.orNull
		.fold(trailingTabStackStackOrNull) { tabStack ->
			fold(tabStack) { tab ->
				this?.plus(0)
			}
		}
		?.plus(byte)
		?.let { byteReader ->
			LeoReader(byteReader, leadingTabStackStackOrNull, null)
		}

val LeoReader.termOrNull: Term?
	get() =
		plusNewline
			?.run {
				byteReader
					.orNull
					.fold(trailingTabStackStackOrNull) { tabStack ->
						fold(tabStack) { tab ->
							this?.plus(0)
						}
					}
				?.termOrNull
		}
