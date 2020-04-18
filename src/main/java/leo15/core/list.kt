package leo15.core

import leo.base.Seq
import leo.base.fold
import leo.base.foldRight
import leo.base.seq
import leo14.Script
import leo14.emptyScript
import leo14.invoke
import leo14.plus
import leo15.lambda.Term
import leo15.listName
import leo15.ofName

val <T : Leo<T>> Typ<T>.listTyp: Typ<List<T>>
	get() =
		Typ(listName(ofName(scriptLine))) { List(this@listTyp, this) }

data class List<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<List<T>>() {
	override val typ get() = itemTyp.listTyp
	override val scriptLine get() = listName(itemsScript)
	val itemsScript: Script get() = emptyScript.foldRight(seq) { plus(it.scriptLine) }
	val linkOrNull: Link<T>? get() = term.leo(itemTyp.linkTyp.optionalTyp).orNull
}

val <T : Leo<T>> Typ<T>.list: List<T> get() = optional.term.leo(listTyp)
val <T : Leo<T>> Link<T>.list: List<T> get() = optional.term.leo(itemTyp.listTyp)
val <T : Leo<T>> T.list: List<T> get() = typ.list.plus(this)
operator fun <T : Leo<T>> List<T>.plus(item: T): List<T> = linkTo(item).list
val <T : Leo<T>> List<T>.seq: Seq<T> get() = seq { linkOrNull?.seqNode }
fun <T : Leo<T>> Typ<T>.list(vararg items: T): List<T> = list.fold(items) { plus(it) }
fun <T : Leo<T>> list(item: T, vararg items: T): List<T> = item.list.fold(items) { plus(it) }
