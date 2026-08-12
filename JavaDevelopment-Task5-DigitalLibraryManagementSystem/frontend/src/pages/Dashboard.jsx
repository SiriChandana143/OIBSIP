import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import { FiBook, FiClock, FiStar, FiMessageCircle } from 'react-icons/fi';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ borrows: 0, fines: 0, recommendations: 0 });
  const [recentBooks, setRecentBooks] = useState([]);

  useEffect(() => {
    Promise.all([
      API.get(`/borrow/user/${user.id}`),
      API.get(`/user/${user.id}/fines`),
      API.get(`/recommendations/${user.id}`),
      API.get('/books'),
    ]).then(([borrows, fines, recs, books]) => {
      setStats({
        borrows: borrows.data.data.filter(b => b.status === 'ISSUED' || b.status === 'OVERDUE').length,
        fines: fines.data.data.filter(f => f.paymentStatus === 'PENDING').length,
        recommendations: recs.data.data.length,
      });
      setRecentBooks(books.data.data.slice(0, 4));
    }).catch(() => {});
  }, [user.id]);

  return (
    <Layout>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-gray-800">Welcome, {user.name}!</h1>
        <p className="text-gray-500">Your personal library dashboard</p>
      </div>

      <div className="grid md:grid-cols-3 gap-6 mb-8">
        {[
          { icon: FiBook, label: 'Active Borrows', value: stats.borrows, color: 'bg-blue-500' },
          { icon: FiClock, label: 'Pending Fines', value: stats.fines, color: 'bg-red-500' },
          { icon: FiStar, label: 'Recommendations', value: stats.recommendations, color: 'bg-purple-500' },
        ].map(({ icon: Icon, label, value, color }) => (
          <div key={label} className="card flex items-center gap-4">
            <div className={`w-12 h-12 ${color} rounded-xl flex items-center justify-center`}>
              <Icon className="text-white" size={24} />
            </div>
            <div>
              <p className="text-2xl font-bold">{value}</p>
              <p className="text-sm text-gray-500">{label}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="grid md:grid-cols-2 gap-6">
        <div className="card">
          <h2 className="text-lg font-semibold mb-4">Quick Actions</h2>
          <div className="grid grid-cols-2 gap-3">
            <Link to="/books" className="p-4 bg-primary-50 rounded-lg text-center hover:bg-primary-100 transition">
              <FiBook className="mx-auto mb-2 text-primary-600" size={24} />
              <span className="text-sm font-medium">Search Books</span>
            </Link>
            <Link to="/recommendations" className="p-4 bg-purple-50 rounded-lg text-center hover:bg-purple-100 transition">
              <FiStar className="mx-auto mb-2 text-purple-600" size={24} />
              <span className="text-sm font-medium">Recommendations</span>
            </Link>
            <Link to="/chatbot" className="p-4 bg-green-50 rounded-lg text-center hover:bg-green-100 transition">
              <FiMessageCircle className="mx-auto mb-2 text-green-600" size={24} />
              <span className="text-sm font-medium">AI Assistant</span>
            </Link>
            <Link to="/borrow-history" className="p-4 bg-orange-50 rounded-lg text-center hover:bg-orange-100 transition">
              <FiClock className="mx-auto mb-2 text-orange-600" size={24} />
              <span className="text-sm font-medium">Borrow History</span>
            </Link>
          </div>
        </div>

        <div className="card">
          <h2 className="text-lg font-semibold mb-4">Featured Books</h2>
          <div className="space-y-3">
            {recentBooks.map(book => (
              <div key={book.id} className="flex items-center gap-3 p-2 rounded-lg hover:bg-gray-50">
                <img src={book.imageUrl || 'https://via.placeholder.com/40x60'} alt="" className="w-10 h-14 object-cover rounded" />
                <div className="flex-1 min-w-0">
                  <p className="font-medium text-sm truncate">{book.title}</p>
                  <p className="text-xs text-gray-500">{book.author}</p>
                </div>
                <span className={`text-xs px-2 py-1 rounded-full ${book.available ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                  {book.available ? 'Available' : 'Unavailable'}
                </span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </Layout>
  );
}
