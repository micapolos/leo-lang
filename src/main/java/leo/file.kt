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

val File.wordStream: Stream<Word>
	get() =
		wordStack.reverse.stream

fun file(word: Word, vararg words: Word): File =
	stack(word, *words).file

fun <R> File.useBitStream(fn: Stream<Bit>?.() -> R): R =
	path.file.useBitStream(fn)

val File.path: Path
	get() =
		Paths.get("${wordStack.top}.leo").fold(wordStack.pop) { word ->
			Paths.get(word.toString()).resolve(this)
		}
