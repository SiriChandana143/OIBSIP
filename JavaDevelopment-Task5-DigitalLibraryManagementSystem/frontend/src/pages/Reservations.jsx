import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import toast from 'react-hot-toast';

export default function Reservations() {
  const { user } = useAuth();
  const [reservations, setReservations] = useState([]);

  useEffect(() => { loadReservations(); }, [user.id]);

  const loadReservations = () => {
    API.get(`/user/${user.id}/reservations`).then(res => setReservations(res.data.data)).catch(() => {});
  };

  const handleCancel = async (id) => {
    try {
      await API.delete(`/user/reservations/${id}`);
      toast.success('Reservation cancelled');
      loadReservations();
    } catch { toast.error('Failed to cancel'); }
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">My Reservations</h1>
      <div className="grid gap-4">
        {reservations.map(r => (
          <div key={r.id} className="card flex items-center justify-between">
            <div>
              <h3 className="font-semibold">{r.bookTitle}</h3>
              <p className="text-sm text-gray-500">Reserved on {new Date(r.reservationDate).toLocaleDateString()}</p>
            </div>
            <div className="flex items-center gap-3">
              <span className={`px-3 py-1 rounded-full text-xs ${r.status === 'PENDING' ? 'bg-yellow-100 text-yellow-700' : r.status === 'FULFILLED' ? 'bg-green-100 text-green-700' : 'bg-gray-100 text-gray-700'}`}>{r.status}</span>
              {r.status === 'PENDING' && <button onClick={() => handleCancel(r.id)} className="btn-danger text-xs !py-1 !px-3">Cancel</button>}
            </div>
          </div>
        ))}
        {reservations.length === 0 && <p className="text-center py-12 text-gray-500">No reservations yet.</p>}
      </div>
    </Layout>
  );
}
