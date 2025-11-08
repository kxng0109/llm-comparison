# LLM Comparison Tool

![Backend CI](https://github.com/kxng0109/llm-comparison-tool/workflows/Backend%20CI%2FCD/badge.svg)
![Frontend CI](https://github.com/kxng0109/llm-comparison-tool/workflows/Frontend%20CI%2FCD/badge.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)

A modern full-stack web application to compare responses from multiple Large Language Models (LLMs) side-by-side. Compare OpenAI, Anthropic Claude, Ollama, and more in real-time.

## Features

* Real-time Comparison - Compare multiple LLM responses simultaneously
* Modern UI - Beautiful, responsive design with dark mode support
* Detailed Metrics - View token usage, response time, and rate limits
* Mobile Friendly - Fully responsive across all devices
* Health Monitoring - Visual indicator for backend connectivity
* Fast & Lightweight - Built with Vite and React
* Multiple LLM Support - OpenAI, Anthropic, Ollama, and more
* Secure - API keys managed server-side only

## Architecture

This is a monorepo containing both frontend and backend:

```
llm-comparison-tool/
├── frontend/    # React + Vite + TailwindCSS
└── backend/     # Spring Boot + Spring AI
```

### Tech Stack

**Frontend:**

* React 18 - UI library
* Vite - Build tool and dev server
* TailwindCSS - Utility-first CSS framework
* Axios - HTTP client
* Inter Font - Modern typography

**Backend:**

* Spring Boot 3.x - Java framework
* Spring AI - LLM integration framework
* ChatClient - Unified interface for multiple LLM providers
* Maven - Dependency management

## Quick Start

### Prerequisites

* Frontend: Node.js 18+ and npm
* Backend: Java 21+ and Maven
* API keys for the LLM providers you want to use (or use Ollama for free)

### 1. Clone the Repository

```bash
git clone https://github.com/kxng0109/llm-comparison-tool.git
cd llm-comparison-tool
```

### 2. Setup Backend

```bash
cd backend
cp .env.example .env
# Edit .env and add your configuration
mvn clean install
mvn spring-boot:run
```

Backend will be available at [http://localhost:8080](http://localhost:8080)

See backend/README.md for detailed backend setup

### 3. Setup Frontend

```bash
cd frontend
npm install
cp .env.example .env
# Edit .env if needed (defaults to http://localhost:8080)
npm run dev
```

Frontend will be available at [http://localhost:3000](http://localhost:3000)

See frontend/README.md for detailed frontend setup

## Configuration

### Supported LLM Providers

| Provider  | Cost | API Key Required | Recommended For      |
| --------- | ---- | ---------------- | -------------------- |
| Ollama    | Free | No               | Testing, Development |
| OpenAI    | Paid | Yes              | Production, GPT-4    |
| Anthropic | Paid | Yes              | Production, Claude   |

Quick Setup with Ollama (Free):

1. Install Ollama from [https://ollama.ai/download](https://ollama.ai/download)
2. Run: ollama pull llama3.2:1b (or any other model) && ollama serve
3. No API keys needed

For Commercial APIs:

* Get OpenAI API Key from [https://platform.openai.com/api-keys](https://platform.openai.com/api-keys)
* Get Anthropic API Key from [https://console.anthropic.com/](https://console.anthropic.com/)

See individual README files for detailed deployment instructions.

## API Documentation

### Endpoints

* GET /api/llm/available - Get list of available models
* POST /api/llm/compare - Compare models with a prompt
* GET /api/llm/health - Health check

See backend/README.md for complete API documentation.

## Project Structure

```
llm-comparison-tool/
├── frontend/
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── services/        # API integration
│   │   └── App.jsx          # Main app component
│   ├── public/
│   ├── .env.example
│   ├── package.json
│   └── README.md
├── backend/
│   ├── src/main/java/
│   │   └── io/github/kxng0109/backend
│   │       ├── controller/  # REST controllers
│   │       ├── service/     # Business logic
│   │       ├── model/dto    # Data models
│   │       ├── error/       # Error Handling
│   │       └── config/      # Configuration
│   ├── .env.example
│   ├── pom.xml
│   └── README.md
├── docker-compose.yml      #Ollama setup
└── README.md               # This file
```

## Troubleshooting

### Frontend won't connect to backend

* Verify VITE_API_URL is set correctly
* Check backend is running: curl [http://localhost:8080/api/llm/health](http://localhost:8080/api/llm/health)
* Look for CORS errors in browser console

### Backend won't start

* Check Java version: java -version (need 17+)
* Verify at least one LLM provider is configured
* Check application logs for errors

### LLM responses timing out

* Commercial APIs: Check API key validity
* Ollama: Ensure ollama serve is running
* Check rate limits haven't been exceeded

## License

This project is licensed under the MIT License - see the [LICENSE](/LICENSE) file for details.

## Acknowledgments

* Built with Vite and React
* Backend powered by Spring Boot and Spring AI
* Styled with TailwindCSS
* Icons from Heroicons
* Font from Google Fonts

## Additional Resources

* Spring AI Documentation: [https://docs.spring.io/spring-ai/reference/](https://docs.spring.io/spring-ai/reference/)
* OpenAI API Documentation: [https://platform.openai.com/docs](https://platform.openai.com/docs)
* Anthropic API Documentation: [https://docs.anthropic.com/](https://docs.anthropic.com/)
* Ollama Documentation: [https://ollama.ai/docs](https://ollama.ai/docs)
* React Documentation: [https://react.dev/](https://react.dev/)
* Vite Documentation: [https://vitejs.dev/](https://vitejs.dev/)

## Roadmap

* Add conversation history
* Support for image-based prompts
* Export comparison results as PDF/CSV
* Cost calculator for API usage
* Response caching
* Custom system prompts per model
* A/B testing mode
* Response rating system

Built by Joshua Ike