package dev.serch.adventofcode

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

internal class CalculatePasswordCandidatesByCriteriaSpec : StringSpec({
    "test calculate passwords criteria with at least two adjacent numbers" {
        val calculatePasswordCandidatesByCriteria = CalculatePasswordCandidatesByCriteria(EnsureAtLeastTwoAdjacentNumbers())
        calculatePasswordCandidatesByCriteria(264360..746325) shouldBe 945
    }

    "test calculate passwords criteria with just two adjacent numbers" {
        val calculatePasswordCandidatesByCriteria = CalculatePasswordCandidatesByCriteria(EnsureFixedNumberOfAdjacentNumbers())
        calculatePasswordCandidatesByCriteria(264360..746325) shouldBe 617
    }
})