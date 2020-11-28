package leo23.compiler.binding

import leo13.Stack
import java.lang.reflect.Type

data class BindingConstant(val typeStack: Stack<Type>, val isType: Type)