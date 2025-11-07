import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 120000,
});

export const checkHealth = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/llm/health`, {
            timeout: 5000,
        });
        return response.status === 204 || response.status === 200;
    } catch (error) {
        return false;
    }
};

export const sendPromptToLLMs = async (prompt, selectedLLMs = []) => {
    try {
        const response = await api.post('/llm/compare', {
            prompt,
            llms: selectedLLMs.length > 0 ? selectedLLMs : undefined
        });
        return response.data;
    } catch (error) {
        throw error;
    }
};

export const getAvailableLLMs = async () => {
    try {
        const response = await api.get('/llm/available');
        return response.data;
    } catch (error) {
        throw error;
    }
};

export default api;