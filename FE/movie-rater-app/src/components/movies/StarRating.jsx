import React from 'react';
import { Star } from 'lucide-react';

function StarRating({ rating, onRatingChange }) {
    const handleClick = (selectedRating) => {
        onRatingChange(selectedRating);
    };

    return (
        <div className="flex items-center gap-1">
            {[1, 2, 3, 4, 5, 6, 7, 8, 9, 10].map((star) => {
                const isFilled = star <= rating;
                
                return (
                    <button
                        key={star}
                        type="button"
                        onClick={() => handleClick(star)}
                        className="focus:outline-none"
                    >
                        <Star
                            className={`w-8 h-8 transition-colors ${
                                isFilled
                                    ? 'fill-yellow-400 text-yellow-400'
                                    : 'fill-white text-gray-300'
                            }`}
                        />
                    </button>
                );
            })}
            <span className="ml-2 text-lg font-semibold text-gray-700">
                {rating > 0 ? `${rating}/10` : 'Not rated'}
            </span>
        </div>
    );
}

export default StarRating;