import { useState, useRef, useEffect } from 'react'
import './SearchBar.css'

function SearchBar({ value, suggestions, onChange, onSearch }) {
  const [showSuggestions, setShowSuggestions] = useState(false)
  const [selectedIndex, setSelectedIndex] = useState(-1)
  const wrapperRef = useRef(null)

  useEffect(() => {
    function handleClickOutside(event) {
      if (wrapperRef.current && !wrapperRef.current.contains(event.target)) {
        setShowSuggestions(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  useEffect(() => {
    setShowSuggestions(suggestions.length > 0)
    setSelectedIndex(-1)
  }, [suggestions])

  const handleInputChange = (e) => {
    onChange(e.target.value)
  }

  const handleClear = () => {
    onChange('')
    onSearch('')
    setShowSuggestions(false)
  }

  const handleKeyDown = (e) => {
    if (e.key === 'ArrowDown') {
      e.preventDefault()
      setSelectedIndex(prev => 
        prev < suggestions.length - 1 ? prev + 1 : prev
      )
    } else if (e.key === 'ArrowUp') {
      e.preventDefault()
      setSelectedIndex(prev => prev > 0 ? prev - 1 : -1)
    } else if (e.key === 'Enter') {
      e.preventDefault()
      if (selectedIndex >= 0) {
        handleSuggestionClick(suggestions[selectedIndex])
      } else {
        onSearch(value)
        setShowSuggestions(false)
      }
    } else if (e.key === 'Escape') {
      setShowSuggestions(false)
    }
  }

  const handleSuggestionClick = (suggestion) => {
    onSearch(suggestion.title)
    setShowSuggestions(false)
  }

  return (
    <div className="search-bar-wrapper" ref={wrapperRef}>
      <div className="search-bar">
        <input
          type="text"
          className="search-input"
          placeholder="Search for books, authors, or categories..."
          value={value}
          onChange={handleInputChange}
          onKeyDown={handleKeyDown}
          autoComplete="off"
        />
        {value && (
          <button 
            className="clear-button"
            onClick={handleClear}
            aria-label="Clear search"
          >
            ×
          </button>
        )}
        <button 
          className="search-button"
          onClick={() => onSearch(value)}
          aria-label="Search"
        >
          Search
        </button>
      </div>

      {showSuggestions && (
        <ul className="suggestions-list">
          {suggestions.map((suggestion, index) => (
            <li
              key={suggestion.id || index}
              className={`suggestion-item ${index === selectedIndex ? 'selected' : ''}`}
              onClick={() => handleSuggestionClick(suggestion)}
              onMouseEnter={() => setSelectedIndex(index)}
            >
              {suggestion.title}
              {suggestion.author && <span className="suggestion-detail"> by {suggestion.author}</span>}
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default SearchBar
