package tournament_ranking.resources.exception

class CompetitorNotFound(pseudo: String): ApiErrorException("Competitor $pseudo not found", 404)
