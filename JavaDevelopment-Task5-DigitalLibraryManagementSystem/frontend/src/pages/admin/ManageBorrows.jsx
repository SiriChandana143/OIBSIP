import { useEffect, useState } from 'react';
import Layout from '../../components/Layout';
import API from '../../services/api';
import toast from 'react-hot-toast';

export default function ManageBorrows() {
  const [borrows, setBorrows] = useState([]);
  const [filter, setFilter] = useState('ALL');

  const fetchBorrows = () => {
    API.get('/borrow').then(res => setBorrows(res.data.data)).catch(() => toast.error('Failed to load borrows'));
  };

  useEffect(() => { fetchBorrows(); }, []);

  const handleReturn = async (borrowId) => {
    try {
      await API.put(`/borrow/return/${borrowId}`);
      toast.success('Book returned successfully');
      fetchBorrows();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Return failed');
    }
  };

  const statusColor = { ISSUED: 'bg-blue-100 text-blue-700', RETURNED: 'bg-green-100 text-green-700', OVERDUE: 'bg-red-100 text-red-700' };
  const filtered = filter === 'ALL' ? borrows : borrows.filter(b => b.status === filter);

  return (
    <Layout>
      <div className="flex flex-wrap items-center justify-between gap-4 mb-6">
        <h1 className="text-2xl font-bold">Issue Management</h1>
        <div className="flex gap-2">
          {['ALL', 'ISSUED', 'OVERDUE', 'RETURNED'].map(s => (
            <button key={s} onClick={() => setFilter(s)}
              className={`px-3 py-1.5 rounded-lg text-sm ${filter === s ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}>
              {s}
            </button>
          ))}
        </div>
      </div>
      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead><tr className="border-b text-left text-gray-500">
            <th className="pb-3 pr-4">User</th><th className="pb-3 pr-4">Book</th><th className="pb-3 pr-4">Issue Date</th><th className="pb-3 pr-4">Due Date</th><th className="pb-3 pr-4">Return Date</th><th className="pb-3 pr-4">Status</th><th className="pb-3">Action</th>
          </tr></thead>
          <tbody>
            {filtered.map(b => (
              <tr key={b.id} className="border-b last:border-0">
                <td className="py-3 pr-4">{b.userName}</td>
                <td className="py-3 pr-4"><p className="font-medium">{b.bookTitle}</p><p className="text-xs text-gray-500">{b.bookAuthor}</p></td>
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
        {filtered.length === 0 && <p className="text-center py-8 text-gray-500">No records found.</p>}
      </div>
    </Layout>
  );
}
