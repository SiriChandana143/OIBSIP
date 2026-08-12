import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import toast from 'react-hot-toast';
import { FiSearch, FiBook, FiX } from 'react-icons/fi';

export default function Books() {
  const { user } = useAuth();
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState('');
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState(null);

  useEffect(() => { fetchBooks(); }, []);

  const fetchBooks = async (search = '') => {
    setLoading(true);
    try {
      const url = search ? `/books/search?query=${search}&userId=${user.id}` : '/books';
      const { data } = await API.get(url);
      setBooks(data.data);
    } catch { toast.error('Failed to load books'); }
    finally { setLoading(false); }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    fetchBooks(query);
  };

  const handleBorrow = async (bookId) => {
    try {
      await API.post('/borrow', { userId: user.id, bookId });
      toast.success('Book borrowed successfully!');
      setSelected(null);
      fetchBooks(query);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to borrow');
    }
  };

  const handleReserve = async (bookId) => {
    try {
      await API.post('/user/reservations', { userId: user.id, bookId });
      toast.success('Book reserved! We\'ll notify you when available.');
      setSelected(null);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to reserve');
    }
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">Search Books</h1>
      <form onSubmit={handleSearch} className="flex gap-3 mb-8">
        <div className="relative flex-1">
          <FiSearch className="absolute left-3 top-3 text-gray-400" />
          <input type="text" placeholder="Search by title, author, ISBN, or category..." className="input-field pl-10" value={query} onChange={(e) => setQuery(e.target.value)} />
        </div>
        <button type="submit" className="btn-primary">Search</button>
      </form>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600"></div></div>
      ) : (
        <div className="grid sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {books.map(book => (
            <div key={book.id} className="card !p-0 overflow-hidden cursor-pointer" onClick={() => setSelected(book)}>
              <img src={book.imageUrl || 'https://via.placeholder.com/300x200?text=Book'} alt={book.title} className="w-full h-48 object-cover" />
              <div className="p-4">
                <h3 className="font-semibold truncate">{book.title}</h3>
                <p className="text-sm text-gray-500">{book.author}</p>
                <div className="flex items-center gap-2 mt-2">
                  <span className="text-xs bg-primary-100 text-primary-700 px-2 py-0.5 rounded-full">{book.category}</span>
                  <span className={`text-xs px-2 py-0.5 rounded-full ${book.available ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                    {book.availableQuantity}/{book.quantity}
                  </span>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
      {!loading && books.length === 0 && (
        <div className="text-center py-12 text-gray-500">
          <FiBook size={48} className="mx-auto mb-4 opacity-50" />
          <p>No books found. Try a different search.</p>
        </div>
      )}

      {selected && (
        <div className="fixed inset-0 bg-black/50 z-50 flex items-center justify-center p-4" onClick={() => setSelected(null)}>
          <div className="bg-white rounded-xl max-w-lg w-full max-h-[90vh] overflow-y-auto" onClick={(e) => e.stopPropagation()}>
            <div className="relative">
              <img src={selected.imageUrl || 'https://via.placeholder.com/600x300?text=Book'} alt={selected.title} className="w-full h-56 object-cover rounded-t-xl" />
              <button onClick={() => setSelected(null)} className="absolute top-3 right-3 p-2 bg-white rounded-full shadow"><FiX /></button>
            </div>
            <div className="p-6">
              <h2 className="text-xl font-bold">{selected.title}</h2>
              <p className="text-gray-600 mt-1">by {selected.author}</p>
              <div className="grid grid-cols-2 gap-3 mt-4 text-sm">
                <div><span className="text-gray-500">ISBN</span><p className="font-medium">{selected.isbn}</p></div>
                <div><span className="text-gray-500">Category</span><p className="font-medium">{selected.category}</p></div>
                <div><span className="text-gray-500">Publisher</span><p className="font-medium">{selected.publisher || 'N/A'}</p></div>
                <div><span className="text-gray-500">Year</span><p className="font-medium">{selected.publicationYear || 'N/A'}</p></div>
                <div><span className="text-gray-500">Language</span><p className="font-medium">{selected.language || 'English'}</p></div>
                <div><span className="text-gray-500">Availability</span><p className={`font-medium ${selected.available ? 'text-green-600' : 'text-red-600'}`}>{selected.availableQuantity} of {selected.quantity}</p></div>
              </div>
              <div className="mt-6 flex gap-3">
                {selected.available ? (
                  <button onClick={() => handleBorrow(selected.id)} className="btn-primary flex-1">Borrow Book</button>
                ) : (
                  <button onClick={() => handleReserve(selected.id)} className="btn-secondary flex-1">Reserve Book</button>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </Layout>
  );
}
