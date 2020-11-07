package leo21.token.type.compiler

import leo13.select
import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.script

val TokenTypeCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(type.script)

val TypeParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeNameTypeParent -> typeCompiler.fragment.parent(begin(name))
			is ChoiceNameTypeParent -> choiceCompiler.fragment.parent(begin(name))
			is ArrowDoingTypeParent -> arrowCompiler.fragment.parent(begin("doing"))
		}

val TokenChoiceCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(choice.script)

val ChoiceParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerChoiceParent -> typeCompiler.fragment.parent(begin("choice"))
		}

val TokenArrowCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(typeOrArrow.select(Type::script, Arrow::script))

val ArrowParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerArrowParent -> typeCompiler.fragment.parent(begin("function"))
		}

