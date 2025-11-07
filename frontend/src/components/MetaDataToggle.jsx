import React from 'react';

const MetadataToggle = ({ showMetadata, onToggle }) => {
    return (
        <button
            onClick={onToggle}
            className="fixed top-4 sm:top-6 right-16 sm:right-20 z-50 p-2.5 sm:p-3 rounded-xl bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm shadow-lg border border-gray-200/50 dark:border-gray-700/50 hover:scale-110 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            aria-label="Toggle metadata display"
            title={showMetadata ? "Hide detailed info" : "Show detailed info"}
            type="button"
        >
            {showMetadata ? (
                <svg
                    className="w-5 h-5 sm:w-6 sm:h-6 text-indigo-600 dark:text-indigo-400"
                    fill="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z"/>
                </svg>
            ) : (
                <svg
                    className="w-5 h-5 sm:w-6 sm:h-6 text-gray-600 dark:text-gray-400"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                </svg>
            )}
        </button>
    );
};

export default MetadataToggle;