import { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import toast from 'react-hot-toast';
import { FiUser, FiMail, FiPhone, FiShield } from 'react-icons/fi';

export default function Profile() {
  const { user, updateUser } = useAuth();
  const [form, setForm] = useState({ name: '', phone: '' });
  const [profile, setProfile] = useState(null);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    API.get(`/user/${user.id}`).then(res => {
      const data = res.data.data;
      setProfile(data);
      setForm({ name: data.name, phone: data.phone || '' });
    }).catch(() => toast.error('Failed to load profile'));
  }, [user.id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    try {
      const { data } = await API.put(`/user/${user.id}/profile`, form);
      setProfile(data.data);
      updateUser({ name: data.data.name });
      toast.success('Profile updated successfully');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Update failed');
    } finally {
      setSaving(false);
    }
  };

  if (!profile) {
    return (
      <Layout>
        <div className="flex justify-center py-12">
          <div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600" />
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">My Profile</h1>
      <div className="grid lg:grid-cols-3 gap-6">
        <div className="card lg:col-span-1">
          <div className="flex flex-col items-center text-center">
            <div className="w-20 h-20 rounded-full bg-primary-100 text-primary-700 flex items-center justify-center text-2xl font-bold mb-4">
              {profile.name.charAt(0).toUpperCase()}
            </div>
            <h2 className="text-xl font-semibold">{profile.name}</h2>
            <p className="text-gray-500 text-sm mt-1">{profile.email}</p>
            <span className="inline-block mt-3 px-3 py-1 text-xs rounded-full bg-primary-100 text-primary-700">{profile.role}</span>
            <p className="text-xs text-gray-400 mt-4">Member since {new Date(profile.createdDate).toLocaleDateString()}</p>
          </div>
        </div>

        <div className="card lg:col-span-2">
          <h2 className="font-semibold mb-4">Edit Profile</h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm text-gray-600 mb-1"><FiUser className="inline mr-1" />Name</label>
              <input type="text" className="input-field" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1"><FiMail className="inline mr-1" />Email</label>
              <input type="email" className="input-field bg-gray-50" value={profile.email} disabled />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1"><FiPhone className="inline mr-1" />Phone</label>
              <input type="tel" className="input-field" value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} placeholder="+91XXXXXXXXXX" />
            </div>
            <div>
              <label className="block text-sm text-gray-600 mb-1"><FiShield className="inline mr-1" />Role</label>
              <input type="text" className="input-field bg-gray-50" value={profile.role} disabled />
            </div>
            <button type="submit" disabled={saving} className="btn-primary">
              {saving ? 'Saving...' : 'Save Changes'}
            </button>
          </form>
        </div>
      </div>
    </Layout>
  );
}
