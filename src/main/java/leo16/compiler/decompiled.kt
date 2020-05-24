package leo16.compiler

import com.sun.jdi.Value
import leo.base.Binary

data class Decompiled(val value: Value, val binary: Binary)