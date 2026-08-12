import { useEffect, useState } from 'react';
import Layout from '../../components/Layout';
import API from '../../services/api';
import toast from 'react-hot-toast';

export default function ManageFines() {
  const [fines, setFines] = useState([]);

  useEffect(() => { loadFines(); }, []);

  const loadFines = () => {
    API.get('/admin/fines').then(res => setFines(res.data.data)).catch(() => {});
  };

  const markPaid = async (id) => {
    try {
      await API.put(`/admin/fines/${id}/pay`);
      toast.success('Fine marked as paid');
      loadFines();
    } catch { toast.error('Failed to update'); }
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">Fine Management</h1>
      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead><tr className="border-b text-left text-gray-500">
            <th className="pb-3 pr-4">User</th><th className="pb-3 pr-4">Book</th><th className="pb-3 pr-4">Amount</th><th className="pb-3 pr-4">Date</th><th className="pb-3 pr-4">Status</th><th className="pb-3">Action</th>
          </tr></thead>
          <tbody>
            {fines.map(f => (
              <tr key={f.id} className="border-b last:border-0">
                <td className="py-3 pr-4">{f.userName}</td>
                <td className="py-3 pr-4">{f.bookTitle}</td>
                <td className="py-3 pr-4 font-medium">₹{f.amount}</td>
                <td className="py-3 pr-4">{new Date(f.createdDate).toLocaleDateString()}</td>
                <td className="py-3 pr-4"><span className={`px-2 py-1 rounded-full text-xs ${f.paymentStatus === 'PAID' ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>{f.paymentStatus}</span></td>
                <td className="py-3">{f.paymentStatus === 'PENDING' && <button onClick={() => markPaid(f.id)} className="btn-primary text-xs !py-1 !px-3">Mark Paid</button>}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {fines.length === 0 && <p className="text-center py-8 text-gray-500">No fines recorded.</p>}
      </div>
    </Layout>
  );
}
