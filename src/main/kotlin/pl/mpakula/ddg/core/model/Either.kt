package pl.mpakula.ddg.core.model

sealed class Either<out T> {
    data class Error(val exception: Exception) : Either<Nothing>()
    data class Success<T>(val value: T) : Either<T>()
}
