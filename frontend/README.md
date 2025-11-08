# LLM Comparison Tool - Frontend

React-based frontend for comparing LLM responses side-by-side.

## Quick Start

### Prerequisites

* Node.js 18+ and npm

### Installation

```bash
npm install
```

### Configuration

Create a .env file:

```bash
cp .env.example .env
```

Edit .env:

```
VITE_API_URL=http://localhost:8080/api
```

### Development

```bash
npm run dev
```

Open [http://localhost:3000](http://localhost:3000)

### Build for Production

```bash
npm run build
```

Output will be in dist/ directory.

### Preview Production Build

```bash
npm run preview
```

## Available Scripts

| Command         | Description                              |
| --------------- | ---------------------------------------- |
| npm run dev     | Start development server with hot reload |
| npm run build   | Build for production                     |
| npm run preview | Preview production build locally         |

## Environment Variables

| Variable     | Description     | Default                                                |
| ------------ | --------------- | ------------------------------------------------------ |
| VITE_API_URL | Backend API URL | [http://localhost:8080/api](http://localhost:8080/api) |

## Features

* Dark Mode Toggle - Switch between light/dark themes
* Metadata Toggle - Show/hide detailed response metrics
* Health Indicator - Visual backend connectivity status
* Responsive Design - Works on mobile, tablet, and desktop
* Dynamic Model Selection - Choose which LLMs to compare

## Tech Stack

* React 18 - UI library
* Vite - Build tool
* TailwindCSS - Styling
* Axios - HTTP client
* Inter Font - Typography

## Project Structure

```
frontend/
├── src/
│   ├── components/
│   │   ├── DarkModeToggle.jsx    # Dark mode switch
│   │   ├── MetadataToggle.jsx    # Info toggle
│   │   ├── HealthIndicator.jsx   # Backend status
│   │   ├── PromptInput.jsx       # User input form
│   │   ├── ResponseCard.jsx      # LLM response display
│   │   └── ResponseGrid.jsx      # Grid layout
│   ├── services/
│   │   └── api.js                # Backend API calls
│   ├── App.jsx                   # Main component
│   ├── index.css                 # Global styles
│   └── main.jsx                  # Entry point
├── public/                       # Static assets
├── .env.example                  # Environment template
├── index.html
├── package.json
├── tailwind.config.js
├── eslint.config.js
├── postcss.config.js
└── vite.config.js
```

## Troubleshooting

### Frontend won't start

# Clear cache and reinstall

```bash
rm -rf node_modules package-lock.json
npm install
```

### Can't connect to backend

* Check VITE_API_URL is correct in .env
* Verify backend is running: curl [http://localhost:8080/api/llm/health](http://localhost:8080/api/llm/health)
* Check browser console for CORS errors
* Look at the health indicator (bottom-right corner)

### Build errors

# Update dependencies

```bash
npm update
npm run build
```

### Dark mode not working

* Clear browser cache (Ctrl+Shift+Delete)
* Clear localStorage: Open DevTools > Application > Local Storage > Clear

## API Integration

The frontend expects these backend endpoints:

### GET /api/llm/available

```json
[
    "openai",
    "anthropic",
    "ollama"
]
```

### POST /api/llm/compare

```json
{
  "prompt": "What is AI?",
  "llms": ["openai", "anthropic"]
}
```

### GET /api/llm/health

Returns 200 status.

## License

MIT License - see LICENSE
