import { useEffect, useState } from 'react';
import Layout from '../../components/Layout';
import API from '../../services/api';
import toast from 'react-hot-toast';

export default function ManageUsers() {
  const [users, setUsers] = useState([]);

  useEffect(() => { loadUsers(); }, []);

  const loadUsers = () => {
    API.get('/admin/users').then(res => setUsers(res.data.data)).catch(() => {});
  };

  const changeRole = async (id, role) => {
    try {
      await API.put(`/admin/users/${id}/role`, { role });
      toast.success('Role updated');
      loadUsers();
    } catch { toast.error('Failed to update role'); }
  };

  const toggleStatus = async (id) => {
    try {
      await API.put(`/admin/users/${id}/toggle-status`);
      toast.success('Status updated');
      loadUsers();
    } catch { toast.error('Failed to update status'); }
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">Manage Users</h1>
      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead><tr className="border-b text-left text-gray-500">
            <th className="pb-3 pr-4">Name</th><th className="pb-3 pr-4">Email</th><th className="pb-3 pr-4">Phone</th><th className="pb-3 pr-4">Role</th><th className="pb-3 pr-4">Status</th><th className="pb-3">Actions</th>
          </tr></thead>
          <tbody>
            {users.map(u => (
              <tr key={u.id} className="border-b last:border-0">
                <td className="py-3 pr-4 font-medium">{u.name}</td>
                <td className="py-3 pr-4">{u.email}</td>
                <td className="py-3 pr-4">{u.phone || '-'}</td>
                <td className="py-3 pr-4">
                  <select value={u.role} onChange={(e) => changeRole(u.id, e.target.value)} className="text-xs border rounded px-2 py-1" disabled={u.role === 'ADMIN'}>
                    <option value="USER">USER</option>
                    <option value="LIBRARIAN">LIBRARIAN</option>
                    <option value="ADMIN">ADMIN</option>
                  </select>
                </td>
                <td className="py-3 pr-4">
                  <span className={`px-2 py-1 rounded-full text-xs ${u.active ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>{u.active ? 'Active' : 'Blocked'}</span>
                </td>
                <td className="py-3">
                  {u.role !== 'ADMIN' && (
                    <button onClick={() => toggleStatus(u.id)} className={`text-xs px-3 py-1 rounded ${u.active ? 'bg-red-100 text-red-700 hover:bg-red-200' : 'bg-green-100 text-green-700 hover:bg-green-200'}`}>
                      {u.active ? 'Block' : 'Unblock'}
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
