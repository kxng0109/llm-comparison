import React, { useState } from 'react';

const PromptInput = ({ onSubmit, isLoading }) => {
    const [prompt, setPrompt] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (prompt.trim()) {
            onSubmit(prompt);
        }
    };

    return (
        <div className="w-full max-w-7xl mx-auto mb-6 sm:mb-8">
            <form onSubmit={handleSubmit} className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm rounded-xl shadow-lg p-5 sm:p-7 border border-gray-200/50 dark:border-gray-700/50 transition-all duration-300 hover:shadow-xl">
                <label htmlFor="prompt" className="block text-base sm:text-lg font-semibold text-gray-800 dark:text-gray-100 mb-3 sm:mb-4">
                    Enter Your Prompt
                </label>
                <textarea
                    id="prompt"
                    value={prompt}
                    onChange={(e) => setPrompt(e.target.value)}
                    placeholder="Type your question or request here..."
                    className="w-full px-4 py-3 sm:px-5 sm:py-4 border border-gray-300 dark:border-gray-600 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-transparent outline-none resize-none transition-all duration-200 bg-white dark:bg-gray-900/50 text-gray-900 dark:text-gray-100 placeholder-gray-400 dark:placeholder-gray-500 text-sm sm:text-base"
                    rows="4"
                    disabled={isLoading}
                />
                <div className="mt-4 sm:mt-5 flex justify-end">
                    <button
                        type="submit"
                        disabled={isLoading || !prompt.trim()}
                        className="w-full sm:w-auto px-8 py-3 sm:py-3.5 bg-gradient-to-r from-indigo-600 to-purple-600 text-white font-semibold rounded-xl hover:from-indigo-700 hover:to-purple-700 focus:ring-4 focus:ring-indigo-300 dark:focus:ring-indigo-800 disabled:from-gray-400 disabled:to-gray-400 dark:disabled:from-gray-600 dark:disabled:to-gray-600 disabled:cursor-not-allowed transition-all duration-200 transform hover:scale-105 active:scale-95 shadow-md hover:shadow-lg text-sm sm:text-base"
                    >
                        {isLoading ? (
                            <span className="flex items-center justify-center">
                <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                  <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
                Processing...
              </span>
                        ) : (
                            'Compare Models ✨'
                        )}
                    </button>
                </div>
            </form>
        </div>
    );
};

export default PromptInput;