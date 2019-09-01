package leo13.token.reader

import leo.base.fold
import leo.base.updateIfNotNull
import leo13.*
import leo13.script.script
import leo13.token.*

data class Reader(
	val tokens: Tokens,
	val tabIndentOrNull: TabIndent?,
	val head: Head) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "reader"
	override val scriptableBody
		get() = script(
			tokens.scriptableLine,
			tabIndentOrNull.orNullAsScriptLine("indent"),
			head.scriptableLine)
}

fun reader(tokens: Tokens, parentTabIndentOrNull: TabIndent?, head: Head) =
	Reader(tokens, parentTabIndentOrNull, head)

fun reader() = reader(tokens(), null, NameHead(""))

fun tokens(string: String): Tokens =
	reader().push(string).finish

val String.tokens get() = tokens(this)

object Colon

val colon = Colon

fun Reader.push(string: String): Reader =
	fold(string) { push(it) }

fun Reader.push(char: Char): Reader =
	pushOrNull(char) ?: fail("char")

fun Reader.pushOrNull(char: Char): Reader? =
	when (char) {
		' ' -> pushSpaceOrNull
		'\t' -> pushTabOrNull
		':' -> pushColonOrNull
		'\n' -> pushNewlineOrNull
		else -> pushOtherOrNull(char)
	}

val Reader.pushSpaceOrNull: Reader?
	get() =
		when (head) {
			is NameHead ->
				if (head.name.isEmpty()) null
				else Reader(
					tokens.plus(token(opening(head.name))).plus(token(closing)),
					tabIndentOrNull,
					head(""))
			is ColonHead ->
				reader(tokens, tabIndentOrNull, head(""))
			is BlockIndentHead ->
				reader(
					tokens,
					tabIndentOrNull,
					head
						.tabIndent
						.removeOrNull(space)
						?.let { tabIndent -> head(tabIndent) }
						?: head(""))
		}

val Reader.pushTabOrNull: Reader?
	get() =
		when (head) {
			is NameHead -> null
			is ColonHead -> null
			is BlockIndentHead ->
				reader(
					tokens,
					tabIndentOrNull.orNullPlus(head.tabIndent.spaceIndent),
					head.tabIndent.removeOrNull(tab)?.let { tabIndent -> head(tabIndent) } ?: head(""))
		}

val Reader.pushColonOrNull: Reader?
	get() =
		when (head) {
			is NameHead ->
				if (head.name.isEmpty()) null
				else reader(
					tokens.plus(token(opening(head.name))),
					tabIndentOrNull.orNullPlus(indent(space)),
					head(colon))
			is ColonHead -> null
			is BlockIndentHead -> null
		}

val Reader.pushNewlineOrNull: Reader?
	get() =
		when (head) {
			is NameHead ->
				if (head.name.isEmpty()) null
				else Reader(
					tokens.plus(token(opening(head.name))),
					null,
					head(tabIndentOrNull.orNullPlus(space).reverse))
			is ColonHead -> null
			is BlockIndentHead -> null
		}

fun Reader.pushOtherOrNull(char: Char): Reader? =
	when (head) {
		is NameHead ->
			if (!char.isLetter()) null
			else reader(tokens, tabIndentOrNull, head(head.name + char))
		is ColonHead -> null
		is BlockIndentHead ->
			reader(
				tokens.flush(head.tabIndent),
				tabIndentOrNull,
				head("$char"))
	}

fun Tokens.flush(tabIndent: TabIndent): Tokens =
	flush(tabIndent.spaceIndent).updateIfNotNull(tabIndent.removeOrNull(tab)) { flush(it) }

fun Tokens.flush(spaceIndent: SpaceIndent): Tokens =
	plus(token(closing)).updateIfNotNull(spaceIndent.removeOrNull(space)) { flush(it) }

val Reader.finish: Tokens
	get() =
		finishOrNull ?: fail("finish")

val Reader.finishOrNull: Tokens?
	get() =
		if (tabIndentOrNull != null) null
		else when (head) {
			is NameHead -> null
			is ColonHead -> null
			is BlockIndentHead -> tokens.flush(head.tabIndent)
		}
