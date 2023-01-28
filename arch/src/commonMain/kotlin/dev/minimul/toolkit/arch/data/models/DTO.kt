package dev.minimul.toolkit.arch.data.models

interface DTO<T> {
    fun toModel(): T
}

fun <T> List<DTO<T>>.toModelList() = map { it.toModel() }