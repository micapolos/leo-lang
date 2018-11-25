package leo

import leo.base.appendableString
import leo.base.fail
import leo.base.string

data class Meta<out V>(
	val value: V) {
	override fun toString() = appendableString { it.append(this) }
}

val metaChar = '#'

val <V> V.meta: Meta<V>
	get() =
		Meta(this)

fun <V> Appendable.append(meta: Meta<V>): Appendable =
	append(metaChar).append(meta.value.string)

fun <V, R> Meta<V>.map(fn: (V) -> R): Meta<R> =
	fn(value).meta

val Meta<Nothing>.reflect: Field<Nothing>
	get() =
		reflect { fail }

fun <V> Meta<V>.reflectTerm(valueReflect: V.() -> Term<Nothing>): Field<Nothing> =
	metaWord fieldTo reflectMeta(valueReflect)

fun <V> Meta<V>.reflect(valueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	metaWord fieldTo reflectMeta { valueReflect().term }

fun <V> Meta<V>.reflectMeta(valueReflect: V.() -> Term<Nothing>): Term<Nothing> =
	valueReflect(value)

fun <V> Term<Nothing>.parseMeta(valueParse: Term<Nothing>.() -> V?): Term<V> =
	valueParse(this)?.meta?.term ?: this
