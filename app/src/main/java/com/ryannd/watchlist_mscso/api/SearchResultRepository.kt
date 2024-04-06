package com.ryannd.watchlist_mscso.api

class SearchResultRepository(private val tmdbApi: TmdbApi) {
    private fun filterOutPeople(response: TmdbApi.SearchResponse): List<SearchResult> {
        val results = response.results
        val filtered = mutableListOf<SearchResult>()
        results.forEach {
            if(it.knownFor.isNullOrEmpty()) {
                filtered.add(it)
            }
        }

        return filtered
    }

    suspend fun getSearch(searchTerm: String): List<SearchResult> {
        val response = tmdbApi.getSearch(searchTerm)

        return filterOutPeople(response)
    }
}