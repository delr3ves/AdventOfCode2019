package dev.serch.adventofcode

class PrintImage {
    operator fun invoke(pixels: List<List<Int>>): String {
        return pixels.fold("") { acc, row ->
            val rowAsString = row.map { pixel ->
                when (pixel) {
                    0 -> '■'
                    1 -> '□'
                    else -> ' '
                }
            }.joinToString("")
            "${acc}\n$rowAsString"
        }
    }
}

class CombineLayers(private val splitLayers: SplitLayers = SplitLayers()) {
    operator fun invoke(input: String, width: Int, height: Int): List<List<Int>> {
        val layers = splitLayers(input, width, height)
        val transparentColor = 2
        val transparentLayer: List<Int> = List(width * height) { transparentColor }
        val image = layers.fold(transparentLayer) { acc, layer ->
            acc.zip(layer).map { pair ->
                if (pair.first == transparentColor) {
                    pair.second
                } else {
                    pair.first
                }
            }
        }
        return image.chunked(width)
    }
}

class CheckImageConsistency(private val splitLayers: SplitLayers = SplitLayers()) {
    operator fun invoke(input: String, width: Int, height: Int): Int {
        val layers = splitLayers(input, width, height)
        val layerWithLessZero = layers.map {
            Pair(it, it.filter { pixel -> pixel == 0 }.size)
        }.minBy { it.second }
        val numberOfOnesInLayer = layerWithLessZero?.first?.filter { it == 1 }?.size ?: 0
        val numberOfTwosInLayer = layerWithLessZero?.first?.filter { it == 2 }?.size ?: 0
        return numberOfOnesInLayer * numberOfTwosInLayer
    }
}

class SplitLayers {
    operator fun invoke(input: String, width: Int, height: Int): List<List<Int>> {
        tailrec fun splitInLayers(pixels: List<Int>, itemsPerLayer: Int, currentLayers: List<List<Int>>): List<List<Int>> {
            if (pixels.isEmpty()) {
                return currentLayers
            }
            return splitInLayers(pixels.drop(itemsPerLayer), itemsPerLayer, currentLayers + listOf(pixels.take(itemsPerLayer)))
        }
        return splitInLayers(input.toList().map { it.toString().toInt() }, width * height, emptyList())
    }
}