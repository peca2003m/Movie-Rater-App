import React, { useState, useEffect } from 'react';
import { Search, LogOut, User } from 'lucide-react';
import api from '../../api/movieApi';
import MovieCard from '../movies/MovieCard';
import MovieDetailsModal from '../movies/MovieDetailsModal';

function Dashboard({ user, onLogout }) {
    const [searchQuery, setSearchQuery] = useState('');
    const [searchResults, setSearchResults] = useState([]);
    const [myMovies, setMyMovies] = useState([]);
    const [selectedMovie, setSelectedMovie] = useState(null);

    useEffect(() => {
        loadMyMovies();
    }, []);

    useEffect(() => {
        const timer = setTimeout(() => {
            if (searchQuery.trim()) {
                searchMovies();
            } else {
                setSearchResults([]);
            }
        }, 500);
        return () => clearTimeout(timer);
    }, [searchQuery]);

    const loadMyMovies = async () => {
        try {
            const movies = await api.getMyMovies();
            setMyMovies(movies);
        } catch (err) {
            console.error('Failed to load movies', err);
        }
    };

    const searchMovies = async () => {
        try {
            const results = await api.searchMovies(searchQuery);
            setSearchResults(results);
        } catch (err) {
            console.error('Search failed', err);
        }
    };

    const handleAddMovie = async (imdbId) => {
        try {
            await api.addMovie(imdbId);
            loadMyMovies();
            setSearchQuery('');
            setSearchResults([]);
        } catch (err) {
            alert('Failed to add movie');
        }
    };

    const handleDeleteMovie = async (imdbId) => {
        try {
            const updatedMovies = await api.deleteMovie(imdbId);
            setMyMovies(updatedMovies);
        } catch (err) {
            console.error('Failed to delete movie', err);
            alert('Failed to delete movie. Please try again.');
        }
    };

    const handleMovieClick = async (movie) => {
        try {
            const details = await api.getMovieDetails(movie.imdbId);
            setSelectedMovie(details);
        } catch (err) {
            alert('Failed to load movie details');
        }
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <header className="bg-white shadow-md px-6 py-4 flex justify-between items-center">
                <h1 className="text-2xl font-bold text-blue-600">My Movie Collection</h1>
                <div className="flex items-center gap-4">
                    <div className="flex items-center gap-2">
                        <User className="w-5 h-5 text-gray-600" />
                        <span className="font-semibold">{user.username}</span>
                    </div>
                    <button
                        onClick={onLogout}
                        className="flex items-center gap-2 text-red-600 hover:text-red-700"
                    >
                        <LogOut className="w-5 h-5" />
                        Logout
                    </button>
                </div>
            </header>

            <main className="max-w-7xl mx-auto p-6">
                <div className="mb-8">
                    <div className="relative max-w-2xl mx-auto">
                        <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400 w-6 h-6" />
                        <input
                            type="text"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            placeholder="Search for movies..."
                            className="w-full pl-12 pr-4 py-4 text-lg border-2 border-gray-300 rounded-lg focus:border-blue-500 outline-none"
                        />
                    </div>

                    {searchResults.length > 0 && (
                        <div className="mt-4 max-w-2xl mx-auto bg-white rounded-lg shadow-lg p-4">
                            <h3 className="font-bold mb-3">Search Results</h3>
                            <div className="space-y-2">
                                {searchResults.map((movie) => (
                                    <div
                                        key={movie.imdbId}
                                        onClick={() => handleAddMovie(movie.imdbId)}
                                        className="flex gap-3 p-3 hover:bg-gray-50 rounded-lg cursor-pointer"
                                    >
                                        <img
                                            src={movie.imageUrl || '/api/placeholder/60/90'}
                                            alt={movie.title}
                                            className="w-12 h-16 object-cover rounded"
                                        />
                                        <div>
                                            <p className="font-semibold">{movie.title}</p>
                                            <p className="text-sm text-gray-500">{movie.year}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    )}
                </div>

                <div>
                    <h2 className="text-2xl font-bold mb-4">My Movies</h2>
                    {myMovies.length > 0 ? (
                        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
                            {myMovies.map((movie) => (
                                <MovieCard
                                    key={movie.imdbId}
                                    movie={movie}
                                    onClick={() => handleMovieClick(movie)}
                                    onDelete={handleDeleteMovie}
                                />
                            ))}
                        </div>
                    ) : (
                        <p className="text-gray-500 text-center py-12">
                            No movies in your collection yet. Search and add some!
                        </p>
                    )}
                </div>
            </main>

            {selectedMovie && (
                <MovieDetailsModal
                    movie={selectedMovie}
                    onClose={() => setSelectedMovie(null)}
                    onUpdate={() => {
                        loadMyMovies();
                        setSelectedMovie(null);
                    }}
                />
            )}
        </div>
    );
}

export default Dashboard;