package tournament_ranking.resources.dto

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import javax.validation.Validation

class AddCompetitorTest {
    @Test
    fun shouldBeValidIfPseudoIsEmpty() {
        val validatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = validatorFactory.validator

        val command = AddCompetitor("")

        assertThat(validator.validate(command)).hasSize(1)
    }
}