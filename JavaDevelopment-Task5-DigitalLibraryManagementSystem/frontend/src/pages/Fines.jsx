import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';

export default function Fines() {
  const { user } = useAuth();
  const [fines, setFines] = useState([]);

  useEffect(() => {
    API.get(`/user/${user.id}/fines`).then(res => setFines(res.data.data)).catch(() => {});
  }, [user.id]);

  const totalPending = fines.filter(f => f.paymentStatus === 'PENDING').reduce((sum, f) => sum + parseFloat(f.amount), 0);

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">My Fines</h1>
      {totalPending > 0 && (
        <div className="card bg-red-50 border border-red-200 mb-6">
          <p className="text-red-700 font-medium">Total Pending: ₹{totalPending.toFixed(2)}</p>
          <p className="text-sm text-red-600 mt-1">Fine rate: ₹5 per overdue day. Please visit the library to pay.</p>
        </div>
      )}
      <div className="grid gap-4">
        {fines.map(f => (
          <div key={f.id} className="card flex items-center justify-between">
            <div>
              <h3 className="font-semibold">{f.bookTitle}</h3>
              <p className="text-sm text-gray-500">{new Date(f.createdDate).toLocaleDateString()}</p>
            </div>
            <div className="text-right">
              <p className="text-lg font-bold">₹{f.amount}</p>
              <span className={`text-xs px-2 py-1 rounded-full ${f.paymentStatus === 'PAID' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>{f.paymentStatus}</span>
            </div>
          </div>
        ))}
        {fines.length === 0 && <p className="text-center py-12 text-gray-500">No fines. Great job!</p>}
      </div>
    </Layout>
  );
}
