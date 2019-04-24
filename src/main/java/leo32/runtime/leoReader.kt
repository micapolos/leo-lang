@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package leo32.runtime

import leo.base.*
import leo.binary.utf8ByteSeq
import leo.binary.utf8String
import leo32.base.Effect
import leo32.base.apply
import leo32.base.mapValue

data class LeoReader(
	val byteReader: ByteReader,
	val leadingTabStackStackOrNull: Stack<Stack<Tab>>?,
	val trailingTabStackStackOrNull: Stack<Stack<Tab>>?,
	val currentTabStackOrNull: Stack<Tab>?)

val ByteReader.leoReader
	get() =
		LeoReader(this, null, null, null)

val Empty.leoReader
	get() =
		byteReader.leoReader

fun LeoReader.plus(string: String): LeoReader =
	plusEffect(string)
		.apply { remainingString ->
			if (remainingString.isEmpty()) this
			else error(remainingString)
		}

fun LeoReader.plusEffect(string: String): Effect<LeoReader, String> =
	foldUntilNullEffect(string.utf8ByteSeq) { byte ->
		plus(byte)
	}.mapValue { utf8String }

fun LeoReader.plus(byte: Byte): LeoReader? =
	when (byte) {
		' '.clampedByte -> plusSpace
		'\t'.clampedByte -> plusTab
		'\n'.clampedByte -> plusNewline
		'.'.clampedByte -> plusDot
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
						leadingTabStackStackOrNull,
						null,
						currentTabStackOrNull.push(tab))
				}
		}

val LeoReader.plusTab: LeoReader?
	get() =
		ifOrNull(byteReader.symbolOrNull == null && currentTabStackOrNull == null) {
			trailingTabStackStackOrNull
				?.let { trailingTabStackStack ->
					LeoReader(
						byteReader,
						leadingTabStackStackOrNull.push(trailingTabStackStack.head),
						trailingTabStackStack.tail,
						null)
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
							leadingTabStackStackOrNull.push(currentTabStackOrNull.push(tab)).reverse,
							null)
					}
			else -> null
		}

val LeoReader.plusDot: LeoReader?
	get() =
		ifOrNull(byteReader.symbolOrNull != null && trailingTabStackStackOrNull == null) {
			byteReader
				.plus(0)
				?.plus(0)
				?.let { byteReader ->
					LeoReader(
						byteReader,
						leadingTabStackStackOrNull,
						trailingTabStackStackOrNull,
						currentTabStackOrNull)
				}
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
			LeoReader(
				byteReader,
				leadingTabStackStackOrNull,
				null,
				currentTabStackOrNull)
		}

val LeoReader.termOrNull: Term?
	get() =
		plusNewline?.run {
			byteReader
				.orNull
				.fold(trailingTabStackStackOrNull) { tabStack ->
					fold(tabStack) { tab ->
						this?.plus(0)
					}
				}
				?.termOrNull
		}
