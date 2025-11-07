import React from 'react';

const DarkModeToggle = ({ darkMode, onToggle }) => {
    return (
        <button
            onClick={onToggle}
            className="fixed top-4 sm:top-6 right-4 sm:right-6 z-50 p-2.5 sm:p-3 rounded-xl bg-white/90 dark:bg-gray-800/90 backdrop-blur-sm shadow-lg border border-gray-200/50 dark:border-gray-700/50 hover:scale-110 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            aria-label="Toggle dark mode"
            type="button"
        >
            {darkMode ? (
                <svg
                    className="w-5 h-5 sm:w-6 sm:h-6 text-yellow-500"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 0l-.707.707M6.343 17.657l-.707.707M16 12a4 4 0 11-8 0 4 4 0 018 0z"
                    />
                </svg>
            ) : (
                <svg
                    className="w-5 h-5 sm:w-6 sm:h-6 text-gray-700"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth={2}
                        d="M20.354 15.354A9 9 0 018.646 3.646 9.003 9.003 0 0012 21a9.003 9.003 0 008.354-5.646z"
                    />
                </svg>
            )}
        </button>
    );
};

export default DarkModeToggle;