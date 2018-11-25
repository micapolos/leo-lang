package leo

import leo.base.appendableString
import leo.base.fail
import leo.base.string

data class Meta<out V>(
	val value: V) {
	override fun toString() = appendableString { it.append(this) }
}

val <V> V.meta: Meta<V>
	get() =
		Meta(this)

fun <V> Appendable.append(meta: Meta<V>): Appendable =
	append(meta.value.string)

fun <V> Meta<V>.reflect(metaValueReflect: V.() -> Field<Nothing>): Field<Nothing> =
	metaWord fieldTo term(metaValueReflect(value))

val Meta<Nothing>.reflect: Field<Nothing>
	get() =
		reflect { fail }

fun <V> Field<Nothing>.parseMeta(valueParse: Field<Nothing>.() -> V?): Meta<V>? =
	matchKey(metaWord) {
		onlyFieldOrNull?.let { onlyField ->
			valueParse(onlyField)?.meta
		}
	}

val <V> Meta<V>.token: Token<V>
  get() =
	  value.metaToken
