import React, { useState } from 'react';

const HealthIndicator = ({ isHealthy }) => {
    const [isHovered, setIsHovered] = useState(false);

    if (isHealthy === null) return null;
    if (isHealthy) return null;

    return (
        <div
            className="fixed bottom-6 right-6 z-50 flex items-center"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <div
                className={`bg-red-500 dark:bg-red-600 text-white px-4 py-2 rounded-l-lg shadow-lg transition-all duration-300 ease-in-out ${
                    isHovered ? 'opacity-100 max-w-xs' : 'opacity-0 max-w-0 px-0'
                } overflow-hidden whitespace-nowrap`}
            >
        <span className="text-sm font-medium">
          ⚠️ Backend server unreachable
        </span>
            </div>
            <div className="bg-red-500 dark:bg-red-600 p-3 rounded-lg shadow-lg hover:scale-110 transition-transform duration-200 cursor-pointer">
                <svg
                    className="w-5 h-5 text-white"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                    />
                </svg>
            </div>
        </div>
    );
};

export default HealthIndicator;