import './BookCard.css'

function BookCard({ book }) {
  return (
    <div className="book-card">
      <div className="book-header">
        <h3 className="book-title">{book.title}</h3>
        <span className="book-category">{book.category}</span>
      </div>
      
      <div className="book-body">
        <p className="book-author">
          <strong>Author:</strong> {book.author}
        </p>
        <p className="book-isbn">
          <strong>ISBN:</strong> {book.isbn}
        </p>
        <p className="book-description">{book.description}</p>
      </div>
      
      <div className="book-footer">
        <span className="book-price">${book.price.toFixed(2)}</span>
      </div>
    </div>
  )
}

export default BookCard
