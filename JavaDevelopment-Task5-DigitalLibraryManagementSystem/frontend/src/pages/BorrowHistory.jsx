import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import toast from 'react-hot-toast';

export default function BorrowHistory() {
  const { user } = useAuth();
  const [borrows, setBorrows] = useState([]);

  useEffect(() => {
    API.get(`/borrow/user/${user.id}`).then(res => setBorrows(res.data.data)).catch(() => {});
  }, [user.id]);

  const handleReturn = async (borrowId) => {
    try {
      await API.put(`/borrow/return/${borrowId}`);
      toast.success('Book returned successfully!');
      const { data } = await API.get(`/borrow/user/${user.id}`);
      setBorrows(data.data);
    } catch (err) {
      toast.error(err.response?.data?.message || 'Return failed');
    }
  };

  const statusColor = { ISSUED: 'bg-blue-100 text-blue-700', RETURNED: 'bg-green-100 text-green-700', OVERDUE: 'bg-red-100 text-red-700' };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">Borrow History</h1>
      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="border-b text-left text-gray-500">
              <th className="pb-3 pr-4">Book</th>
              <th className="pb-3 pr-4">Issue Date</th>
              <th className="pb-3 pr-4">Due Date</th>
              <th className="pb-3 pr-4">Return Date</th>
              <th className="pb-3 pr-4">Status</th>
              <th className="pb-3">Action</th>
            </tr>
          </thead>
          <tbody>
            {borrows.map(b => (
              <tr key={b.id} className="border-b last:border-0">
                <td className="py-3 pr-4">
                  <p className="font-medium">{b.bookTitle}</p>
                  <p className="text-xs text-gray-500">{b.bookAuthor}</p>
                </td>
                <td className="py-3 pr-4">{new Date(b.issueDate).toLocaleDateString()}</td>
                <td className="py-3 pr-4">{new Date(b.dueDate).toLocaleDateString()}</td>
                <td className="py-3 pr-4">{b.returnDate ? new Date(b.returnDate).toLocaleDateString() : '-'}</td>
                <td className="py-3 pr-4"><span className={`px-2 py-1 rounded-full text-xs ${statusColor[b.status]}`}>{b.status}</span></td>
                <td className="py-3">
                  {(b.status === 'ISSUED' || b.status === 'OVERDUE') && (
                    <button onClick={() => handleReturn(b.id)} className="btn-primary text-xs !py-1 !px-3">Return</button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {borrows.length === 0 && <p className="text-center py-8 text-gray-500">No borrow history yet.</p>}
      </div>
    </Layout>
  );
}
