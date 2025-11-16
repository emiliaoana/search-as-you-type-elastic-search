import './BookDetail.css'

function BookDetail({ book, onBack }) {
  return (
    <div className="book-detail-page">
      <div className="book-detail-container">
        <button className="back-button" onClick={onBack}>
          ← Back to Search
        </button>

        <div className="book-detail">
          <div className="book-detail-header">
            <h1 className="book-detail-title">{book.title}</h1>
            <span className="book-detail-category">{book.category}</span>
          </div>

          <div className="book-detail-content">
            <div className="book-detail-info">
              <div className="info-row">
                <span className="info-label">Author:</span>
                <span className="info-value">{book.author}</span>
              </div>
              
              <div className="info-row">
                <span className="info-label">ISBN:</span>
                <span className="info-value">{book.isbn}</span>
              </div>
              
              <div className="info-row">
                <span className="info-label">Category:</span>
                <span className="info-value">{book.category}</span>
              </div>
              
              <div className="info-row">
                <span className="info-label">Price:</span>
                <span className="info-value price">${book.price.toFixed(2)}</span>
              </div>
            </div>

            <div className="book-detail-description">
              <h2>Description</h2>
              <p>{book.description}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default BookDetail
