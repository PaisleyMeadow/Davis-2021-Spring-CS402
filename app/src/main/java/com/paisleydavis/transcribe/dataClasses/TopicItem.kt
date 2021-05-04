package com.paisleydavis.transcribe.dataClasses


data class TopicItem(
        val name: String,
        val details: String
) {
    override fun toString(): String = name
}