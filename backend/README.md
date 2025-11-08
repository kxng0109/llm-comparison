# LLM Comparison Tool - Backend

Spring Boot backend with Spring AI integration for multiple LLM providers.

## Quick Start

### Prerequisites

- Java 17 or higher (check: java -version)
- No need to install Maven - project includes Maven wrapper
- At least one LLM provider configured

### Installation

On MacOS/Linux:
```bash
./mvnw clean install
```
`
On Windows:
```cmd
mvnw.cmd clean install
```

### Configuration

Create a .env file or configure application.properties:

cp .env.example .env

Minimal setup (Free with Ollama):
SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434
SPRING_AI_OLLAMA_CHAT_MODEL=llama3.2:1b
CORS_ALLOWED_ORIGINS=http://localhost:3000

### Run

On MacOS/Linux:
```bash
./mvnw spring-boot:run
```
`
On Windows:
```cmd
mvnw.cmd spring-boot:run
```

Backend available at http://localhost:8080

### Test
```bash
curl http://localhost:8080/api/llm/health
````

Should return HTTP 200 Ok.

## Project Structure

```
backend/
├── src/main/java/io/github/kxng0109/backend/
│ ├── config/
│ │ ├── AiClientConfig.java # ChatClient beans configuration
│ │ └── CorsConfig.java # CORS configuration
│ ├── controller/
│ │ └── AiController.java # REST API endpoints
│ ├── error/
│ │ ├── GlobalExceptionHandler.java # Global error handling
│ │ ├── ErrorResponse.java # Error response DTO
│ │ └── ModelNotFoundException.java # Custom exception
│ ├── model/dto/
│ │ ├── ChatRequest.java # Request DTO
│ │ ├── ModelResponse.java # Response DTO
│ │ ├── ModelMetadata.java # Metadata DTO
│ │ └── ModelRateLimit.java # Rate limit DTO
│ ├── service/
│ │ └── AiService.java # Business logic
│ └── BackendApplication.java # Main application class
├── src/main/resources/
│ └── application.properties # Configuration
├── src/test/
│ └── (unit tests)
├── .env.example # Environment template
├── .gitignore
├── pom.xml # Maven dependencies
└── README.md
```

## Maven Wrapper Commands

This project uses Maven Wrapper, so you don't need to install Maven separately.

Linux/Mac:

| Command | Description |
|---------|-------------|
| ./mvnw clean | Remove target directory |
| ./mvnw compile | Compile source code |
| ./mvnw test | Run unit tests |
| ./mvnw package | Create JAR file in target/ |
| ./mvnw spring-boot:run | Run application |
| ./mvnw clean install | Full build with tests |

Windows:

| Command | Description |
|---------|-------------|
| mvnw.cmd clean | Remove target directory |
| mvnw.cmd compile | Compile source code |
| mvnw.cmd test | Run unit tests |
| mvnw.cmd package | Create JAR file in target/ |
| mvnw.cmd spring-boot:run | Run application |
| mvnw.cmd clean install | Full build with tests |

### Why Maven Wrapper?

The Maven Wrapper ensures everyone uses the same Maven version:

- No need to install Maven separately
- Consistent builds across different environments
- Automatically downloads the correct Maven version
- Committed to the repository for team consistency

## LLM Provider Configuration

### Option 1: Ollama (Free - Recommended for Testing)

Ollama runs locally and requires no API key.

1. Install Ollama from https://ollama.ai/download

2. Pull and run a model:
   ollama pull llama3.2:1b
   ollama serve

3. Configure in .env:
   SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434
   SPRING_AI_OLLAMA_CHAT_MODEL=llama3.2:1b
   CORS_ALLOWED_ORIGINS=http://localhost:3000

4. Start the backend:
   mvn spring-boot:run

No API keys needed.

### Option 2: OpenAI

OpenAI provides state-of-the-art models like GPT-4.

1. Get API key from https://platform.openai.com/api-keys

2. Configure in .env:
   SPRING_AI_OPENAI_API_KEY=sk-your-key-here
   SPRING_AI_OPENAI_CHAT_MODEL=model-name
   CORS_ALLOWED_ORIGINS=http://localhost:3000

Warning: Costs money per request. Monitor usage at https://platform.openai.com/usage

### Option 3: Anthropic

Anthropic provides Claude models with a focus on safety and reliability.

1. Get API key from https://console.anthropic.com/

2. Configure in .env:
   SPRING_AI_ANTHROPIC_API_KEY=sk-ant-your-key-here
   SPRING_AI_ANTHROPIC_CHAT_MODEL=model-name
   CORS_ALLOWED_ORIGINS=http://localhost:3000

Warning: Costs money per request. Monitor usage in Anthropic Console.

### Using Multiple Providers

Configure all three to enable full comparison capability:

# Ollama (free)

SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434
SPRING_AI_OLLAMA_CHAT_MODEL=llama3.2:1b

# OpenAI (paid)

SPRING_AI_OPENAI_API_KEY=sk-xxx
SPRING_AI_OPENAI_CHAT_MODEL=model-name

# Anthropic (paid)

SPRING_AI_ANTHROPIC_API_KEY=sk-ant-xxx
SPRING_AI_ANTHROPIC_CHAT_MODEL=model-name

# CORS (required)

CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:5173

## API Endpoints

### Health Check

GET /api/llm/health

Returns: HTTP 200 Ok

Used by frontend to check if backend is reachable.

### Get Available Models

GET /api/llm/available

Response:

```json
[
	"openai",
	"anthropic",
	"ollama"
]
```

Returns a list of model identifiers that are currently configured and available.

### Compare LLMs

POST /api/llm/compare
Content-Type: application/json

Request Body:

```json
{
	"prompt": "What is artificial intelligence?",
	"llms": [
		"openai",
		"anthropic",
		"ollama"
	]
}
```

Response:

```json
{
	"responses": [
		{
			"llm": "openai",
			"response": "Artificial intelligence refers to...",
			"metadata": {
				"promptTokens": 25,
				"generationTokens": 150,
				"totalTokens": 175,
				"responseTime": 2340,
				"model": "gpt-4-turbo",
				"finishReason": "stop",
				"timestamp": "2025-11-07T18:20:49Z",
				"rateLimit": {
					"requestsLimit": 10000,
					"requestsRemaining": 9847,
					"tokensLimit": 2000000,
					"tokensRemaining": 1850234,
					"resetAfter": 0
				}
			}
		}
	]
}
```

Field Descriptions:

- llm: Identifier of the model that generated the response
- response: The actual text response from the model
- metadata.promptTokens: Number of tokens in the input prompt
- metadata.generationTokens: Number of tokens in the generated response
- metadata.totalTokens: Sum of prompt and generation tokens
- metadata.responseTime: Time taken to generate response (milliseconds)
- metadata.model: Specific model version used
- metadata.finishReason: Why generation stopped (stop, length, etc.)
- metadata.timestamp: When the response was generated
- metadata.rateLimit: API rate limit information (if available)

### Error Responses

All errors return a structured error response:

```json
    {
	"timestamp": "2025-11-07T18:49:10Z",
	"status": 400,
	"error": "Validation Failed",
	"message": "Invalid request parameters",
	"details": {
		"prompt": "must not be empty",
		"llms": "must not be null"
	}
}
```

Common HTTP Status Codes:

- 400 Bad Request: Invalid input (missing prompt, invalid model names)
- 404 Not Found: Requested model does not exist
- 500 Internal Server Error: Unexpected server error

## Configuration Details

### CORS Configuration

CORS (Cross-Origin Resource Sharing) allows your frontend to communicate with the backend.

In application.properties:
cors.allowed-origins=http://localhost:3000,http://localhost:5173

This setting:

- Allows requests from localhost:3000 (typical React dev server)
- Allows requests from localhost:5173 (Vite dev server)
- Blocks requests from any other domain

For production deployment:
cors.allowed-origins=https://your-frontend.whatever,http://localhost:3000

Multiple origins are separated by commas.

### Environment Variables

All sensitive data should be stored in environment variables:

1. Create a .env file (never commit this):
   SPRING_AI_OPENAI_API_KEY=sk-xxx
   SPRING_AI_ANTHROPIC_API_KEY=sk-ant-xxx

2. Load environment variables (Linux/Mac):
   export $(cat .env | xargs)
   mvn spring-boot:run

3. Or use IDE environment variable configuration

### System Message Configuration

The backend uses a system message to set the AI's behavior, this can be customized in AiService.java.

## Deployment

When deploying your app to a server, make sure updated your VITE_API_URL to point to the URL of the site

### Cost Considerations

API Costs:

- OpenAI: Charges per token (input + output)
- Anthropic: Charges per token (input + output)
- Ollama: Free (but requires server to run it)

Monitor your usage to avoid unexpected bills.

## Docker Setup (Optional)

A docker-compose file has been provided for running ollama locally using Docker

Start Ollama:
docker-compose up -d

Pull a model:
docker exec -it ollama ollama pull llama3.2:1b

## Troubleshooting

### Backend won't start

Check Java version:
java -version

Must be 21 or higher.

Check at least one provider is configured:
cat .env | grep -E "OLLAMA|OPENAI|ANTHROPIC"

Check logs for specific errors:
mvn spring-boot:run

Look for lines starting with ERROR or WARN.

### "No LLM provider configured" error

Ensure .env has at least one provider configured:
SPRING_AI_OLLAMA_BASE_URL=http://localhost:11434

Verify environment variables are loaded:
echo $SPRING_AI_OLLAMA_BASE_URL

### Ollama connection failed

Check Ollama is running:
curl http://localhost:11434

Should return Ollama version info.

Start Ollama if not running:
ollama serve

Check if model is pulled:
ollama list

Pull model if missing:
ollama pull llama3.2:1b

### CORS errors from frontend

Add your frontend URL to CORS configuration:
cors.allowed-origins=http://localhost:3000,https://your-app.whatever

Restart backend after changing configuration.

Check browser console for exact CORS error message.

### Model not found error

Verify the model identifier exists:
curl http://localhost:8080/api/llm/available

Should return list like: ["openai", "anthropic", "ollama"]

Request only models that appear in this list.

### OpenAI/Anthropic API errors

Invalid API key:

- Verify key is correct in .env
- Check key hasn't expired
- Verify key has proper permissions

Rate limit exceeded:

- Wait for rate limit to reset
- Check usage in provider dashboard
- Consider upgrading tier

Insufficient credits:

- Add payment method to provider account
- Check billing section for issues

### High response times

Ollama slow:

- Ollama runs locally and depends on CPU/GPU
- Consider using smaller models (e.g., llama3.2:1b instead of llama3:8b)
- Check system resource usage

API slow:

- Network latency to API servers
- Model complexity (GPT-4 slower than GPT-3.5)
- Prompt size affects processing time

## Testing

Run all tests:
```bash
./mvnw test
```

On Windows:
```cmd
mvnw.cmd test
```

Run specific test class:
```cmd
./mvnw test -Dtest=AiServiceTest
```

## License

MIT License - see LICENSE file

## Additional Resources

- Spring AI Documentation: https://docs.spring.io/spring-ai/reference/
- Spring Boot Documentation: https://spring.io/projects/spring-boot
- OpenAI API Reference: https://platform.openai.com/docs
- Anthropic API Reference: https://docs.anthropic.com/
- Ollama Documentation: https://ollama.ai/docs