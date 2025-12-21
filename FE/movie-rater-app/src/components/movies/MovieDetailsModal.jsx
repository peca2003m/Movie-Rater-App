import React, { useState, useEffect } from 'react';
import { Star } from 'lucide-react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import api from '../../api/movieApi';
import StarRating from './StarRating';

function MovieDetailsModal({ movie, onClose, onUpdate }) {
    const [userReview, setUserReview] = useState('');
    const [reviews, setReviews] = useState(movie.reviews || []);
    const [rating, setRating] = useState(movie.rating || 0);

    useEffect(() => {
        const socket = new SockJS('http://localhost:8080/ws');
        const client = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log('Connected to websocket');
                client.subscribe('/topic/'+movie.imdbId, 
                    (message) => {
                    const newReview = JSON.parse(message.body);
                    setReviews(oldReviews => [newReview, ...oldReviews]);
                });
                console.log('Subscribed to /topic/'+movie.imdbId);
            }
        });

        client.activate();

        return () => client.deactivate();
    }, []);

    const handleSubmit = async () => {
        if (!userReview || !rating || rating < 1 || rating > 10) {
            alert('Please provide both a rating (1-10) and a review');
            return;
        }

        try {
            await api.updateMovie(movie.imdbId, { review: userReview, rating: parseInt(rating) });
            onUpdate();
            alert('Review updated successfully!');
        } catch (err) {
            alert('Failed to update review');
        }
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50" onClick={onClose}>
            <div className="bg-white rounded-lg max-w-3xl w-full max-h-[90vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
                <div className="sticky top-0 bg-white border-b px-6 py-4 flex justify-between items-center">
                    <h2 className="text-2xl font-bold">{movie.title}</h2>
                    <button onClick={onClose} className="text-gray-500 hover:text-gray-700 text-2xl">×</button>
                </div>

                <div className="p-6">
                    <div className="flex flex-col md:flex-row gap-6 mb-6">
                        <img
                            src={movie.imageUrl || '/api/placeholder/200/300'}
                            alt={movie.title}
                            className="w-48 h-72 object-cover rounded-lg shadow-lg"
                        />
                        <div className="flex-1">
                            <p className="text-gray-700 mb-4">{movie.description}</p>
                            <div className="flex items-center text-yellow-500 mb-4">
                                <Star className="w-6 h-6 fill-current" />
                                <span className="ml-2 text-xl font-bold">
                  {movie.averageRating ? movie.averageRating.toFixed(1) : 'N/A'} Average Rating
                </span>
                            </div>
                        </div>
                    </div>

                    <div className="mb-6 p-4 bg-blue-50 rounded-lg">
                        <h3 className="font-bold mb-3">Your Review</h3>
                        <div className="mb-3">
                            <label className="block text-sm mb-1">Rating (1-10)</label>
                            <StarRating 
                                rating={rating} 
                                onRatingChange={(newRating) => setRating(newRating)} 
                            />
                        </div>
                        <div className="mb-3">
                            <label className="block text-sm mb-1">Review</label>
                            <textarea
                                value={userReview}
                                onChange={(e) => setUserReview(e.target.value)}
                                className="w-full px-3 py-2 border rounded-lg h-24"
                                placeholder="Write your review..."
                            />
                        </div>
                        <button
                            onClick={handleSubmit}
                            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:bg-gray-400"
                        >
                            Save Review
                        </button>
                    </div>

                    <div>
                        <h3 className="font-bold mb-3">All Reviews</h3>
                        {reviews && reviews.length > 0 ? (
                            <div className="space-y-3">
                                {reviews.map((r, idx) => (
                                    <div key={idx} className="p-4 bg-gray-50 rounded-lg">
                                        <div className="flex justify-between items-center mb-2">
                                            <span className="font-semibold text-blue-600">{r.username}</span>
                                            <span className="text-sm text-gray-500">
                        {new Date(r.createdAt).toLocaleString()}
                      </span>
                                        </div>
                                        <p className="text-gray-700">{r.review}</p>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <p className="text-gray-500">No reviews yet.</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MovieDetailsModal;