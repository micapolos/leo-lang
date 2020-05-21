package leo16.native

import leo.base.println
import leo13.array
import leo13.linkOrNull
import leo13.map
import leo13.stack
import leo14.untyped.typed.loadClass
import leo15.anyName
import leo15.arrayName
import leo15.booleanName
import leo15.className
import leo15.constructorName
import leo15.fieldName
import leo15.getName
import leo15.invokeName
import leo15.methodName
import leo15.nameName
import leo15.nativeName
import leo15.nullName
import leo15.objectName
import leo15.parameterName
import leo15.textName
import leo16.accessOrNull
import leo16.contentOrNull
import leo16.does
import leo16.field
import leo16.getOrNull
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.nativeValue
import leo16.onlyFieldOrNull
import leo16.printed
import leo16.sentenceOrNull
import leo16.stackOrNull
import leo16.theNativeOrNull
import leo16.value
import leo16.valueValue
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

fun String.definition(class_: Class<*>) =
	value(className(this())).does {
		className(class_.nativeField).value
	}

val nativeObjectClassDefinition =
	value(className(objectName(nativeName()))).does {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(objectName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value!!
			.javaClass
		className(class_.nativeField).value
	}

val nullNativeDefinition =
	value(nativeName(nullName())).does {
		null.nativeValue
	}

val nativeBooleanDefinition =
	value(booleanName(nativeName())).does {
		this
			.getOrNull(booleanName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.run { value as Boolean }
			.field.value
	}

val nameClassDefinition =
	value(className(nameName(textName(nativeName())))).does {
		val name = this
			.getOrNull(className)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		className(name.loadClass.nativeField).value
	}

val classFieldDefinition =
	value(
		className(anyName()),
		fieldName(nameName(textName(nativeName())))
	).does {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(fieldName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		fieldName(class_.getField(name).nativeField).value
	}

val fieldGetDefinition =
	value(getName(fieldName(nativeName()))).does {
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

val nativeGetFieldDefinition =
	value(
		nativeName(),
		getName(fieldName(nativeName()))
	).does {
		val object_ = this
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value
		val field = this
			.getOrNull(getName)!!
			.getOrNull(fieldName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Field
		field.get(object_).nativeValue
	}

val classConstructorDefinition =
	value(
		className(nativeName()),
		constructorName(parameterName(_list(anyName())))
	).does {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val classes = this
			.getOrNull(constructorName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(className)!!
					.getOrNull(nativeName)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		constructorName(class_.getConstructor(*classes).nativeField).value
	}

val constructorInvokeDefinition =
	value(
		constructorName(nativeName()),
		invokeName(parameterName(_list(anyName())))
	).does {
		val constructor = this
			.getOrNull(constructorName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Constructor<*>
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		constructor.newInstance(*args).nativeValue
	}

val classMethodDefinition =
	value(
		className(nativeName()),
		methodName(
			nameName(textName(nativeName())),
			parameterName(_list(anyName()))
		)
	).does {
		val class_ = this
			.getOrNull(className)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(methodName)!!
			.getOrNull(nameName)!!
			.getOrNull(textName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as String
		val classes = this
			.getOrNull(methodName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(className)!!
					.getOrNull(nativeName)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		methodName(class_.getMethod(name, *classes).nativeField).value
	}

val methodInvokeDefinition =
	value(
		methodName(nativeName()),
		invokeName(parameterName(_list(anyName())))
	).does {
		val method = this
			.getOrNull(methodName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(null, *args).nativeValue
	}

val nativeInvokeMethodDefinition =
	value(
		nativeName(),
		invokeName(
			methodName(nativeName()),
			parameterName(_list(anyName())))
	).does {
		val object_ = this
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value
		val method = this
			.getOrNull(invokeName)!!
			.getOrNull(methodName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(invokeName)!!
			.getOrNull(parameterName)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(object_, *args).nativeValue
	}

val arrayStackDefinition =
	value(_list(arrayName(nativeName()))).does {
		val array = this
			.getOrNull(_list)!!
			.getOrNull(arrayName)!!
			.getOrNull(nativeName)!!
			.theNativeOrNull!!
			.value as Array<*>
		stack(*array).map { value(nativeField) }.valueValue
	}

val printingDefinition =
	value(_printing(anyName())).does {
		val content = this
			.getOrNull(_printing)!!
			.contentOrNull!!
		content.also { content.printed.println }
	}

val leoHeadDefinition =
	value(_head(_any())).does {
		this
			.getOrNull(_lhs)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.value
			?.value
			?: this.getOrNull(_head)!!
	}

val leoTailDefinition =
	value(_tail(_any())).does {
		this
			.getOrNull(_tail)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.stack
			?.value
			?: this.getOrNull(_tail)!!
	}

val leoOpDefinition =
	value(_op(_any())).does {
		this
			.getOrNull(_op)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.value
			?.sentenceOrNull
			?.word
			?.invoke()
			?.value
			?: this.getOrNull(_op)!!
	}
