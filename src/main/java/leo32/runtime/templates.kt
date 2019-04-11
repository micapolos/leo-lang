package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.orIfNull
import leo32.base.*
import leo32.bitSeq

data class Templates(
	val templateTree: Tree<Template?>)

val Tree<Template?>.templates get() =
	Templates(this)

val Empty.templates get() =
	tree<Template>().templates

fun Templates.put(type: Type, template: Template) =
	templateTree.put(type.seq32.bitSeq, template).templates

fun Templates.at(either: Either) =
	templateTree.at32(either.seq32).orIfNull { empty.tree() }.templates

val Templates.templateOrNull get() =
	templateTree.valueOrNull
