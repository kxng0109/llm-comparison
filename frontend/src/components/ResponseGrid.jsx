import React from 'react';
import ResponseCard from './ResponseCard';

const ResponseGrid = ({ responses, showMetadata }) => {
    const getGridClass = () => {
        const count = responses.length;
        if (count === 1) return 'grid-cols-1';
        if (count === 2) return 'grid-cols-1 lg:grid-cols-2';
        return 'grid-cols-1 md:grid-cols-2 lg:grid-cols-3';
    };

    return (
        <div className="w-full max-w-7xl mx-auto">
            <div className={`grid ${getGridClass()} gap-4 sm:gap-6`}>
                {responses.map((item, index) => (
                    <ResponseCard
                        key={`${item.llmId}-${index}`}
                        llmName={item.llmName}
                        response={item.response}
                        isLoading={item.isLoading}
                        error={item.error}
                        metadata={item.metadata}
                        showMetadata={showMetadata}
                    />
                ))}
            </div>
        </div>
    );
};

export default ResponseGrid;