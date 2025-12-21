import React, { useState } from 'react';
import { Star, X } from 'lucide-react';

function MovieCard({ movie, onClick, onDelete }) {
    const [showConfirm, setShowConfirm] = useState(false);

    const handleDeleteClick = (e) => {
        e.stopPropagation();
        setShowConfirm(true);
    };

    const handleConfirmDelete = (e) => {
        e.stopPropagation();
        onDelete(movie.imdbId);
        setShowConfirm(false);
    };

    const handleCancelDelete = (e) => {
        e.stopPropagation();
        setShowConfirm(false);
    };

    return (
        <>
            <div
                onClick={onClick}
                className="bg-white rounded-lg shadow-lg overflow-hidden cursor-pointer transform hover:scale-105 transition relative"
            >
                {/* Delete Button */}
                <button
                    onClick={handleDeleteClick}
                    className="absolute top-2 right-2 bg-red-500 hover:bg-red-600 text-white rounded-full p-1.5 z-10 shadow-lg transition"
                    aria-label="Delete movie"
                >
                    <X className="w-4 h-4" />
                </button>

                <img
                    src={movie.imageUrl || '/api/placeholder/200/300'}
                    alt={movie.title}
                    className="w-full h-64 object-cover"
                />
                <div className="p-4">
                    <h3 className="font-semibold text-gray-800 mb-2 line-clamp-2">
                        {movie.title}
                    </h3>
                    <div className="flex items-center text-yellow-500">
                        <Star className="w-5 h-5 fill-current" />
                        <span className="ml-1 font-bold">
                            {movie.rating ? movie.rating + '/10' : 'Not yet rated'}
                        </span>
                    </div>
                </div>
            </div>

            {/* Confirmation Modal */}
            {showConfirm && (
                <div
                    className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
                    onClick={handleCancelDelete}
                >
                    <div
                        className="bg-white rounded-lg p-6 max-w-sm mx-4 shadow-xl"
                        onClick={(e) => e.stopPropagation()}
                    >
                        <h3 className="text-lg font-semibold text-gray-800 mb-2">
                            Delete Review?
                        </h3>
                        <p className="text-gray-600 mb-6">
                            Are you sure you want to delete your review for "{movie.title}"? This action cannot be undone.
                        </p>
                        <div className="flex gap-3 justify-end">
                            
                            <button
                                onClick={handleConfirmDelete}
                                className="px-4 py-2 bg-red-500 hover:bg-red-600 text-white rounded-lg transition"
                            >
                                Delete
                            </button>

                            <button
                                onClick={handleCancelDelete}
                                className="px-4 py-2 bg-gray-200 hover:bg-gray-300 text-gray-800 rounded-lg transition"
                            >
                                Cancel
                            </button>
                            
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}

export default MovieCard;