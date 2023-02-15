interface A {
    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(data: String): T
    }

    class Base(private val data: String) : A {
        override fun <T> map(mapper: Mapper<T>): T {
            return mapper.map(data)
        }
    }
}

interface B {
    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(number: Int): T
    }

    class Base(private val number: Int) : B {
        override fun <T> map(mapper: Mapper<T>): T = mapper.map(number)
    }
}

data class C(private val data: String, private val number: Int)

class ComplexMapper(private val b: B) : A.Mapper<C> {
    override fun map(data: String): C {
        val mapper = object : B.Mapper<C> {
            override fun map(number: Int): C = C(data.ifEmpty { throw InvalidDataException() }, number)
        }
        return b.map(mapper)
    }
}

class InvalidDataException(override val message: String = "invalid data") :
    java.lang.IllegalStateException(message)

fun main() {
    val a = A.Base("a")
    val b = B.Base(1)
    val c = a.map(ComplexMapper(b))
    println(c)
}