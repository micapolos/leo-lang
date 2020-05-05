package leo16.native

import leo15.*
import leo16.emptyDictionary
import leo16.plus

val dictionary =
	emptyDictionary
		.plus(booleanName.definition(Boolean::class.java))
		.plus(charName.definition(Char::class.java))
		.plus(byteName.definition(Byte::class.java))
		.plus(shortName.definition(Short::class.java))
		.plus(intName.definition(Int::class.java))
		.plus(longName.definition(Long::class.java))
		.plus(floatName.definition(Float::class.java))
		.plus(doubleName.definition(Double::class.java))
		.plus(nameClassDefinition)
		.plus(classFieldDefinition)
		.plus(fieldGetDefinition)
		.plus(nativeGetFieldDefinition)
		.plus(classConstructorDefinition)
		.plus(constructorInvokeDefinition)
		.plus(classMethodDefinition)
		.plus(methodInvokeDefinition)
		.plus(nativeInvokeMethodDefinition)
		.plus(arrayListDefinition)
		.plus(nullNativeDefinition)
		.plus(nativeObjectClassDefinition)
