package leo32.runtime

import leo.base.*
import leo32.Seq32
import leo32.base.*
import leo32.base.List
import leo32.string32

data class Reader(
	val parentOrNull: Reader?,
	val term: Term,
	val charList: List<I32>,
	val isQuoted: Boolean)

val Empty.reader get() =
	Reader(null, term, list(), false)

fun Reader.plus(i32: I32) =
	when (i32) {
		'('.i32 -> plusBegin
		')'.i32 -> plusEnd
		else -> plusChar(i32)
	}

val Reader.plusBegin get() =
	if (charList.isEmpty) null
	else copy(
		parentOrNull = this,
		term = term.begin,
		charList = empty.list(),
		isQuoted = isQuoted || charList.seq.string32 == "quote")

val Reader.plusEnd get() =
	if (!charList.isEmpty || parentOrNull == null) null
	else if (isQuoted)
		parentOrNull.copy(
			term =
				if (!parentOrNull.isQuoted) parentOrNull.term.plus(term)
				else parentOrNull.term.plus(parentOrNull.charList.seq.string32 to term),
			charList = empty.list())
	else
		parentOrNull.copy(
			term = parentOrNull.term.plusResolved(parentOrNull.charList.seq.string32 to term),
			charList = empty.list())

fun Reader.plusChar(i32: I32) =
	copy(charList = charList.add(i32))

val Reader.seq32: Seq32 get() =
	parentOrNull
		.ifNotNullOr({ it.seq32.then { seq('('.i32) } }, { emptySeq() })
		.then { term.seq32.then { charList.seq } }
