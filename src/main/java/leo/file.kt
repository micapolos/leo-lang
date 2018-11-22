package leo

import leo.base.*
import leo.java.io.file
import leo.java.io.useBitStream
import java.nio.file.Path
import java.nio.file.Paths

data class File(
	val wordStack: Stack<Word>)

val Stack<Word>.file: File
	get() =
		File(this)

fun file(word: Word, vararg words: Word): File =
	stack(word, *words).file

fun <R> File.useBitStream(fn: Stream<Bit>?.() -> R): R =
	path.file.useBitStream(fn)

val File.path: Path
	get() =
		wordStack.stream
			.foldFirst { Paths.get("$it.leo") }
			.foldNext { Paths.get(it.toString()).resolve(this) }
