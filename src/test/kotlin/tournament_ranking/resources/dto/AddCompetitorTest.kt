package tournament_ranking.resources.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.validation.Validation

class AddCompetitorTest {
    val validatorFactory = Validation.buildDefaultValidatorFactory()
    val validator = validatorFactory.validator

    @Test
    fun shouldBeInvalidIfPseudoIsEmpty() {
        val command = AddCompetitor("")

        assertThat(validator.validate(command)).hasSize(1)
    }

    @Test
    fun shouldBeInvalidIfPseudoIsNull() {
        val command = AddCompetitor(null)

        assertThat(validator.validate(command)).hasSize(1)
    }
}