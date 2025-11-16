import BookCard from './BookCard'
import './BookList.css'

function BookList({ books, loading, onBookClick }) {
  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
        <p>Loading books...</p>
      </div>
    )
  }

  if (books.length === 0) {
    return (
      <div className="no-results">
        <p>No books found</p>
      </div>
    )
  }

  return (
    <div className="book-list">
      {books.map(book => (
        <BookCard key={book.id} book={book} onClick={() => onBookClick(book)} />
      ))}
    </div>
  )
}

export default BookList
