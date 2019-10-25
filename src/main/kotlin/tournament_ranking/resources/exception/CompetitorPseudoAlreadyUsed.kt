package tournament_ranking.resources.exception

class CompetitorPseudoAlreadyUsed(pseudo: String) : ApiErrorException("the pseudo '$pseudo' is already used")
