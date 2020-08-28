package com.friendoye.recyclerxray

import com.friendoye.recyclerxray.internal.NotInitializedRecyclerXRayApi
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class NotInitializedRecyclerXRayApiTest(private val publicApiMethod: Method, private val methodName: String?) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{1}()")
        fun data() = NotInitializedRecyclerXRayApi::class.java
            .declaredMethods
            .filter { Modifier.isPublic(it.modifiers) }
            .map { arrayOf(it, it.name) }
    }

    @Test(expected = RecyclerXRayIsNotInitializedException::class)
    fun shouldFireNotInitializedException() {
        val api = NotInitializedRecyclerXRayApi
        val argsTypes = publicApiMethod.parameterTypes
        val array = Array<Any?>(argsTypes.size) { null }

        try {
            publicApiMethod.invoke(api, *array)
        } catch (e: InvocationTargetException) {
            // Java Reflection API wraps any exception in InvocationTargetException.
            // Rethrow original exception, cause we care only for this one.
            throw e.cause!!
        }
    }
}
