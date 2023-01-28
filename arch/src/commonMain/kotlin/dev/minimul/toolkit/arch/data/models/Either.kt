package dev.minimul.toolkit.arch.data.models

sealed class Either<out L, out R> {
    class Left<L>(val value: L) : Either<L, Nothing>()
    class Right<R>(val value: R) : Either<Nothing, R>()
}