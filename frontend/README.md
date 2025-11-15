# Book Search Frontend

React frontend for the Elasticsearch search-as-you-type demo.

## Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start the development server:
```bash
npm run dev
```

The app will run on http://localhost:3000

## Features

- **Search as you type**: Real-time suggestions powered by Elasticsearch completion suggester
- **Keyboard navigation**: Use arrow keys to navigate suggestions, Enter to select
- **Responsive design**: Works on desktop and mobile
- **Beautiful UI**: Modern gradient design with smooth animations

## Usage

1. Make sure the Spring Boot backend is running on http://localhost:8080
2. Start typing in the search bar to see suggestions appear
3. Click a suggestion or press Enter to search
4. Browse the book results displayed as cards
