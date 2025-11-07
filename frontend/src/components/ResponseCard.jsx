import React from 'react';

const ResponseCard = ({ llmName, response, isLoading, error, metadata, showMetadata }) => {
    const formatModelName = (name) => {
        if (!name) return 'Unknown Model';

        return name
            .split('-')
            .map(word => word.charAt(0).toUpperCase() + word.slice(1))
            .join(' ');
    };

    const formatTimestamp = (timestamp) => {
        if (!timestamp) return 'N/A';
        try {
            const date = new Date(timestamp);
            return date.toLocaleString();
        } catch {
            return timestamp;
        }
    };

    const formatResponseTime = (time) => {
        if (!time) return 'N/A';
        if (time < 1000) return `${time}ms`;
        return `${(time / 1000).toFixed(2)}s`;
    };

    const formatDuration = (seconds) => {
        if (!seconds) return 'N/A';
        if (seconds < 60) return `${seconds}s`;
        if (seconds < 3600) return `${Math.floor(seconds / 60)}m ${seconds % 60}s`;
        return `${Math.floor(seconds / 3600)}h ${Math.floor((seconds % 3600) / 60)}m`;
    };

    return (
        <div className="bg-white/80 dark:bg-gray-800/80 backdrop-blur-sm rounded-xl shadow-lg p-5 sm:p-6 border border-gray-200/50 dark:border-gray-700/50 transition-all duration-300 hover:shadow-xl">
            <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-4 gap-2">
                <div className="flex-1 min-w-0">
                    <h3 className="text-lg sm:text-xl font-bold text-gray-900 dark:text-gray-50 truncate">
                        {formatModelName(llmName)}
                    </h3>
                    <p className="text-xs text-gray-500 dark:text-gray-400 mt-1 truncate">{llmName}</p>
                </div>
                {isLoading && (
                    <div className="flex items-center text-indigo-600 dark:text-indigo-400 self-start sm:self-center">
                        <svg className="animate-spin h-4 w-4 sm:h-5 sm:w-5 mr-2" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        <span className="text-xs sm:text-sm font-medium">Generating...</span>
                    </div>
                )}
            </div>

            <div className="border-t border-gray-200 dark:border-gray-700 pt-4">
                {isLoading ? (
                    <div className="space-y-3">
                        <div className="h-3 sm:h-4 bg-gray-200 dark:bg-gray-700 rounded animate-pulse"></div>
                        <div className="h-3 sm:h-4 bg-gray-200 dark:bg-gray-700 rounded animate-pulse w-5/6"></div>
                        <div className="h-3 sm:h-4 bg-gray-200 dark:bg-gray-700 rounded animate-pulse w-4/6"></div>
                    </div>
                ) : error ? (
                    <div className="text-red-600 dark:text-red-400 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-3 sm:p-4">
                        <p className="font-semibold mb-1 text-sm sm:text-base">Error:</p>
                        <p className="text-xs sm:text-sm">{error}</p>
                    </div>
                ) : response ? (
                    <div className="prose dark:prose-invert max-w-none prose-sm sm:prose-base">
                        <p className="text-gray-700 dark:text-gray-300 whitespace-pre-wrap leading-relaxed text-sm sm:text-base">
                            {response}
                        </p>
                    </div>
                ) : (
                    <p className="text-gray-400 dark:text-gray-500 italic text-sm sm:text-base">
                        No response yet. Submit a prompt to get started.
                    </p>
                )}
            </div>

            {response && !isLoading && !error && (
                <div className="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 flex items-center justify-between text-xs text-gray-500 dark:text-gray-400">
                    <span>✓ Generated</span>
                    <span>{response.length} chars</span>
                </div>
            )}

            {showMetadata && metadata && response && !isLoading && !error && (
                <div className="mt-4 pt-4 border-t border-gray-100 dark:border-gray-700 space-y-4">

                    <div>
                        <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase tracking-wide">
                            📊 Usage & Performance
                        </h4>
                        <div className="grid grid-cols-2 gap-2 text-xs">
                            {metadata.promptTokens !== null && metadata.promptTokens !== undefined && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Prompt Tokens</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{metadata.promptTokens.toLocaleString()}</span>
                                </div>
                            )}
                            {metadata.generationTokens !== null && metadata.generationTokens !== undefined && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Generation Tokens</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{metadata.generationTokens.toLocaleString()}</span>
                                </div>
                            )}
                            {metadata.totalTokens !== null && metadata.totalTokens !== undefined && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Total Tokens</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{metadata.totalTokens.toLocaleString()}</span>
                                </div>
                            )}
                            {metadata.responseTime !== null && metadata.responseTime !== undefined && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Response Time</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{formatResponseTime(metadata.responseTime)}</span>
                                </div>
                            )}
                        </div>
                    </div>

                    {metadata.rateLimit && (
                        <div>
                            <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase tracking-wide flex items-center">
                                <svg className="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                                </svg>
                                Rate Limits
                            </h4>
                            <div className="grid grid-cols-2 gap-2 text-xs">
                                {metadata.rateLimit.requestsLimit !== null && metadata.rateLimit.requestsLimit !== undefined && metadata.rateLimit.requestsLimit !== 0 && (
                                    <div className="bg-blue-50 dark:bg-blue-900/20 rounded-lg p-2.5 border border-blue-200 dark:border-blue-800">
                                        <span className="text-gray-500 dark:text-gray-400 block mb-1">Request Limit</span>
                                        <span className="font-semibold text-blue-700 dark:text-blue-300">{metadata.rateLimit.requestsLimit.toLocaleString()}</span>
                                    </div>
                                )}
                                {metadata.rateLimit.requestsRemaining !== null && metadata.rateLimit.requestsRemaining !== undefined && metadata.rateLimit.requestsRemaining !== 0 && (
                                    <div className="bg-green-50 dark:bg-green-900/20 rounded-lg p-2.5 border border-green-200 dark:border-green-800">
                                        <span className="text-gray-500 dark:text-gray-400 block mb-1">Requests Remaining</span>
                                        <span className="font-semibold text-green-700 dark:text-green-300">{metadata.rateLimit.requestsRemaining.toLocaleString()}</span>
                                    </div>
                                )}
                                {metadata.rateLimit.tokensLimit !== null && metadata.rateLimit.tokensLimit !== undefined && metadata.rateLimit.tokensLimit !== 0 && (
                                    <div className="bg-purple-50 dark:bg-purple-900/20 rounded-lg p-2.5 border border-purple-200 dark:border-purple-800">
                                        <span className="text-gray-500 dark:text-gray-400 block mb-1">Token Limit</span>
                                        <span className="font-semibold text-purple-700 dark:text-purple-300">{metadata.rateLimit.tokensLimit.toLocaleString()}</span>
                                    </div>
                                )}
                                {metadata.rateLimit.tokensRemaining !== null && metadata.rateLimit.tokensRemaining !== undefined && metadata.rateLimit.tokensRemaining !== 0 && (
                                    <div className="bg-indigo-50 dark:bg-indigo-900/20 rounded-lg p-2.5 border border-indigo-200 dark:border-indigo-800">
                                        <span className="text-gray-500 dark:text-gray-400 block mb-1">Tokens Remaining</span>
                                        <span className="font-semibold text-indigo-700 dark:text-indigo-300">{metadata.rateLimit.tokensRemaining.toLocaleString()}</span>
                                    </div>
                                )}
                                {metadata.rateLimit.resetAfter && metadata.rateLimit.resetAfter !== 0 && (
                                    <div className="bg-orange-50 dark:bg-orange-900/20 rounded-lg p-2.5 border border-orange-200 dark:border-orange-800 col-span-2">
                                        <span className="text-gray-500 dark:text-gray-400 block mb-1">Limit Resets In</span>
                                        <span className="font-semibold text-orange-700 dark:text-orange-300">{formatDuration(metadata.rateLimit.resetAfter)}</span>
                                    </div>
                                )}
                            </div>
                        </div>
                    )}

                    <div>
                        <h4 className="text-xs font-semibold text-gray-700 dark:text-gray-300 mb-2 uppercase tracking-wide">
                            🤖 Model Details
                        </h4>
                        <div className="grid grid-cols-2 gap-2 text-xs">
                            {metadata.model && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5 col-span-2">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Model Version</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{metadata.model}</span>
                                </div>
                            )}
                            {metadata.finishReason && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Finish Reason</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100">{metadata.finishReason}</span>
                                </div>
                            )}
                            {metadata.timestamp && (
                                <div className="bg-gray-50 dark:bg-gray-700/50 rounded-lg p-2.5">
                                    <span className="text-gray-500 dark:text-gray-400 block mb-1">Timestamp</span>
                                    <span className="font-semibold text-gray-900 dark:text-gray-100 text-xs">{formatTimestamp(metadata.timestamp)}</span>
                                </div>
                            )}
                        </div>
                    </div>

                </div>
            )}
        </div>
    );
};

export default ResponseCard;