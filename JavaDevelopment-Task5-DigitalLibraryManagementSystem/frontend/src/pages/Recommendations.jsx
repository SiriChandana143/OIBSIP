import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import toast from 'react-hot-toast';
import { FiStar } from 'react-icons/fi';

export default function Recommendations() {
  const { user } = useAuth();
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    API.get(`/recommendations/${user.id}`)
      .then(res => setBooks(res.data.data))
      .catch(() => toast.error('Failed to load recommendations'))
      .finally(() => setLoading(false));
  }, [user.id]);

  const handleBorrow = async (bookId) => {
    try {
      await API.post('/borrow', { userId: user.id, bookId });
      toast.success('Book borrowed!');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Failed to borrow');
    }
  };

  return (
    <Layout>
      <div className="flex items-center gap-3 mb-6">
        <FiStar className="text-purple-600" size={28} />
        <div>
          <h1 className="text-2xl font-bold">Recommended For You</h1>
          <p className="text-gray-500">Personalized suggestions based on your reading history</p>
        </div>
      </div>

      {loading ? (
        <div className="flex justify-center py-12"><div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600"></div></div>
      ) : (
        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {books.map(book => (
            <div key={book.id} className="card !p-0 overflow-hidden">
              <img src={book.imageUrl || 'https://via.placeholder.com/300x200?text=Book'} alt={book.title} className="w-full h-40 object-cover" />
              <div className="p-4">
                <h3 className="font-semibold">{book.title}</h3>
                <p className="text-sm text-gray-500">{book.author}</p>
                <span className="text-xs bg-purple-100 text-purple-700 px-2 py-0.5 rounded-full mt-2 inline-block">{book.category}</span>
                {book.available && (
                  <button onClick={() => handleBorrow(book.id)} className="btn-primary w-full mt-3 text-sm !py-1.5">Borrow Now</button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
      {!loading && books.length === 0 && (
        <div className="text-center py-12 text-gray-500">
          <p>Start borrowing and searching books to get personalized recommendations!</p>
        </div>
      )}
    </Layout>
  );
}
