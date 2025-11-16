import { useState, useEffect, useCallback } from 'react'
import SearchBar from './components/SearchBar'
import BookList from './components/BookList'
import BookDetail from './components/BookDetail'
import AddBook from './components/AddBook'
import './App.css'

const API_BASE_URL = 'http://localhost:8080/api/books'

function App() {
  const [books, setBooks] = useState([])
  const [suggestions, setSuggestions] = useState([])
  const [loading, setLoading] = useState(false)
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedBook, setSelectedBook] = useState(null)
  const [showAddBook, setShowAddBook] = useState(false)

  useEffect(() => {
    fetchAllBooks()
  }, [])

  const fetchAllBooks = async () => {
    try {
      setLoading(true)
      const response = await fetch(API_BASE_URL)
      
      if (!response.ok) {
        console.error('Fetch failed:', response.status)
        setBooks([])
        return
      }
      
      const data = await response.json()
      setBooks(Array.isArray(data) ? data : [])
    } catch (error) {
      console.error('Error fetching books:', error)
      setBooks([])
    } finally {
      setLoading(false)
    }
  }

  const fetchSuggestions = useCallback(async (query) => {
    if (!query || query.length < 2) {
      setSuggestions([])
      return
    }

    try {
      const response = await fetch(`${API_BASE_URL}/suggest?query=${encodeURIComponent(query)}`)
      const data = await response.json()
      setSuggestions(data)
    } catch (error) {
      console.error('Error fetching suggestions:', error)
      setSuggestions([])
    }
  }, [])

  const searchBooks = async (query) => {
    if (!query) {
      fetchAllBooks()
      return
    }

    try {
      setLoading(true)
      const response = await fetch(`${API_BASE_URL}/search/title?query=${encodeURIComponent(query)}`)
      
      if (!response.ok) {
        console.error('Search failed:', response.status)
        setBooks([])
        return
      }
      
      const data = await response.json()
      setBooks(Array.isArray(data) ? data : [])
    } catch (error) {
      console.error('Error searching books:', error)
      setBooks([])
    } finally {
      setLoading(false)
    }
  }

  const handleSearchChange = (query) => {
    setSearchQuery(query)
    fetchSuggestions(query)
  }

  const handleSearch = (query) => {
    setSearchQuery(query)
    setSuggestions([])
    searchBooks(query)
  }

  const handleBookClick = (book) => {
    setSelectedBook(book)
  }

  const handleBackToList = () => {
    setSelectedBook(null)
    setShowAddBook(false)
  }

  const handleShowAddBook = () => {
    setShowAddBook(true)
  }

  const handleBookAdded = () => {
    fetchAllBooks()
  }

  if (showAddBook) {
    return <AddBook onBack={handleBackToList} onBookAdded={handleBookAdded} />
  }

  if (selectedBook) {
    return <BookDetail book={selectedBook} onBack={handleBackToList} />
  }

  return (
    <div className="app">
      <div className="container">
        <header className="header">
          <h1>Book Search</h1>
          <p>Search as you type with Elasticsearch</p>
        </header>

        <div className="actions-bar">
          <button className="add-book-button" onClick={handleShowAddBook}>
            + Add Book
          </button>
        </div>

        <SearchBar
          value={searchQuery}
          suggestions={suggestions}
          onChange={handleSearchChange}
          onSearch={handleSearch}
        />

        <BookList books={books} loading={loading} onBookClick={handleBookClick} />
      </div>
    </div>
  )
}

export default App
