package leo21.token.compiler

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.compiled.script
import leo21.type.script

val TokenCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(lineCompiler.compiled.script)

val CompiledParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is CompilerNameCompiledParent -> compiler.fragment.parent(begin(name))
			is CompilerDoCompiledParent -> compiler.fragment.parent(begin("do"))
			is DataCompilerNameCompiledParent -> dataCompiler.fragment.parent(begin(name))
			is FunctionDoesCompiledParent -> functionTypeCompiler.fragment.parent(begin("does"))
		}

val DataCompiler.fragment: Fragment
	get() =
		parent.fragment.parent(begin("data")).fragment(script)

val FunctionTypeCompiler.fragment: Fragment
	get() =
		parentCompiler.fragment.parent(begin("function")).fragment(typeCompiler.type.script)

val FunctionCompiler.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(arrowCompiled.arrow.script)

val FunctionParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is CompilerFunctionParent -> compiler.fragment.parent(begin("function"))
		}