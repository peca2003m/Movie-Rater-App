const API_BASE = 'http://localhost:8080';

const api = {
    async call(endpoint, options = {}) {
        const auth = sessionStorage.getItem('auth');
        const headers = {
            'Content-Type': 'application/json',
            ...(auth && { 'Authorization': auth }),
            ...options.headers
        };

        const response = await fetch(`${API_BASE}${endpoint}`, {
            ...options,
            headers
        });

        if (response.status === 401) {
            sessionStorage.removeItem('auth');
            throw new Error('Unauthorized');
        }
        /*
        if (!response.ok && response.status !== 400) {
            throw new Error(`HTTP ${response.status}`);
        }
        */
        if (!response.ok) {
            const errorBody = await response.json();
            throw new Error(errorBody.message || `HTTP ${response.status}`);
        }

        if (response.status === 204 || (response.status === 200 && options.method === 'POST')) {
            return response;
        }

        return response.json();
    },

    async getMe() {
        return this.call('/users/me');
    },

    async register(data) {
        return this.call('/auth/register', {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    async preRegister(data) {
        return this.call('/auth/pre-register', {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },

    async deleteMovie(imdbId) {
    return this.call(`/movies/${imdbId}`, { method: 'DELETE' });
    },

    async searchMovies(query) {
        return this.call(`/movies/search?query=${encodeURIComponent(query)}`);
    },

    async getMyMovies() {
        return this.call('/movies');
    },

    async getMovieDetails(imdbId) {
        return this.call(`/movies/${imdbId}`);
    },

    async addMovie(imdbId) {
        return this.call(`/movies/${imdbId}`, { method: 'POST' });
    },

    async updateMovie(imdbId, data) {
        return this.call(`/movies/${imdbId}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }
};

export default api;