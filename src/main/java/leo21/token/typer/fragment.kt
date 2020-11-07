package leo21.token.typer

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.type.script

val TokenTypeCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(type.script)

val TypeParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeNameTypeParent -> typeCompiler.fragment.parent(begin(name))
			is ChoiceNameTypeParent -> choiceCompiler.fragment.parent(begin(name))
		}

val TokenChoiceCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(choice.script)

val ChoiceParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is TypeCompilerChoiceParent -> typeCompiler.fragment.parent(begin("choice"))
		}

