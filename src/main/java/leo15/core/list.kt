package leo15.core

import leo.base.Seq
import leo.base.fold
import leo.base.seq
import leo14.invoke
import leo15.lambda.Term
import leo15.listName
import leo15.ofName

val <T : Leo<T>> Typ<T>.listTyp: Typ<List<T>>
	get() =
		Typ(listName(ofName(scriptLine))) { List(this@listTyp, this) }

data class List<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<List<T>>() {
	override val typ get() = itemTyp.listTyp
	val optionalLink: Optional<Link<T>> get() = term of itemTyp.linkTyp.optionalTyp
	val unsafeLinkOrNull: Link<T>? get() = optionalLink.unsafeOrNull
}

val <T : Leo<T>> Typ<T>.list: List<T> get() = absent.term.of(listTyp)
val <T : Leo<T>> Link<T>.list: List<T> get() = present.term.of(itemTyp.listTyp)
val <T : Leo<T>> T.list: List<T> get() = typ.list.plus(this)
operator fun <T : Leo<T>> List<T>.plus(item: T): List<T> = linkTo(item).list
fun <T : Leo<T>> Typ<T>.list(vararg items: T): List<T> = list.fold(items) { plus(it) }
fun <T : Leo<T>> list(item: T, vararg items: T): List<T> = item.list.fold(items) { plus(it) }
val <T : Leo<T>> List<T>.unsafeSeq: Seq<T> get() = seq { unsafeLinkOrNull?.unsafeSeqNode }
