package leo32.runtime

import leo.base.Empty
import leo.base.fold
import leo.base.orNull
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

fun Templates.at(either: Either): Templates? =
	templateTree.at32(either.seq32)?.templates

fun Templates.at(type: Type): Templates? =
	orNull.fold(type.eithers.seq) { at(it) }

val Templates.templateOrNull get() =
	templateTree.valueOrNull
