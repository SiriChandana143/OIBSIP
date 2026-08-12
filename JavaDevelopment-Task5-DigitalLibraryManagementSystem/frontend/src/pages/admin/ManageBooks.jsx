import { useEffect, useState } from 'react';
import Layout from '../../components/Layout';
import API from '../../services/api';
import toast from 'react-hot-toast';
import { FiPlus, FiEdit2, FiTrash2 } from 'react-icons/fi';

export default function ManageBooks() {
  const [books, setBooks] = useState([]);
  const [categories, setCategories] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [editId, setEditId] = useState(null);
  const [form, setForm] = useState({ title: '', author: '', isbn: '', categoryId: '', publisher: '', publicationYear: '', quantity: 1, imageUrl: '' });

  useEffect(() => { loadData(); }, []);

  const loadData = () => {
    API.get('/books').then(res => setBooks(res.data.data));
    API.get('/books/categories').then(res => setCategories(res.data.data));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = { ...form, categoryId: form.categoryId ? Number(form.categoryId) : null, publicationYear: form.publicationYear ? Number(form.publicationYear) : null, quantity: Number(form.quantity) };
    try {
      if (editId) {
        await API.put(`/books/${editId}`, payload);
        toast.success('Book updated');
      } else {
        await API.post('/books', payload);
        toast.success('Book added');
      }
      setShowForm(false);
      setEditId(null);
      setForm({ title: '', author: '', isbn: '', categoryId: '', publisher: '', publicationYear: '', quantity: 1, imageUrl: '' });
      loadData();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Operation failed');
    }
  };

  const handleEdit = (book) => {
    setEditId(book.id);
    setForm({ title: book.title, author: book.author, isbn: book.isbn, categoryId: book.categoryId || '', publisher: book.publisher || '', publicationYear: book.publicationYear || '', quantity: book.quantity, imageUrl: book.imageUrl || '' });
    setShowForm(true);
  };

  const handleDelete = async (id) => {
    if (!confirm('Delete this book?')) return;
    try {
      await API.delete(`/books/${id}`);
      toast.success('Book deleted');
      loadData();
    } catch { toast.error('Delete failed'); }
  };

  return (
    <Layout>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Manage Books</h1>
        <button onClick={() => { setShowForm(true); setEditId(null); }} className="btn-primary flex items-center gap-2"><FiPlus /> Add Book</button>
      </div>

      {showForm && (
        <div className="card mb-6">
          <h2 className="font-semibold mb-4">{editId ? 'Edit Book' : 'Add New Book'}</h2>
          <form onSubmit={handleSubmit} className="grid sm:grid-cols-2 gap-4">
            {['title', 'author', 'isbn', 'publisher', 'publicationYear', 'quantity', 'imageUrl'].map(f => (
              <div key={f}>
                <label className="block text-sm font-medium mb-1 capitalize">{f.replace(/([A-Z])/g, ' $1')}</label>
                <input className="input-field" value={form[f]} onChange={(e) => setForm({ ...form, [f]: e.target.value })} required={['title', 'author', 'isbn'].includes(f)} />
              </div>
            ))}
            <div>
              <label className="block text-sm font-medium mb-1">Category</label>
              <select className="input-field" value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })}>
                <option value="">Select Category</option>
                {categories.map(c => <option key={c.id} value={c.id}>{c.categoryName}</option>)}
              </select>
            </div>
            <div className="sm:col-span-2 flex gap-3">
              <button type="submit" className="btn-primary">{editId ? 'Update' : 'Add'} Book</button>
              <button type="button" onClick={() => setShowForm(false)} className="btn-secondary">Cancel</button>
            </div>
          </form>
        </div>
      )}

      <div className="card overflow-x-auto">
        <table className="w-full text-sm">
          <thead><tr className="border-b text-left text-gray-500">
            <th className="pb-3 pr-4">Title</th><th className="pb-3 pr-4">Author</th><th className="pb-3 pr-4">ISBN</th><th className="pb-3 pr-4">Category</th><th className="pb-3 pr-4">Stock</th><th className="pb-3">Actions</th>
          </tr></thead>
          <tbody>
            {books.map(b => (
              <tr key={b.id} className="border-b last:border-0">
                <td className="py-3 pr-4 font-medium">{b.title}</td>
                <td className="py-3 pr-4">{b.author}</td>
                <td className="py-3 pr-4">{b.isbn}</td>
                <td className="py-3 pr-4">{b.category}</td>
                <td className="py-3 pr-4">{b.availableQuantity}/{b.quantity}</td>
                <td className="py-3 flex gap-2">
                  <button onClick={() => handleEdit(b)} className="text-blue-600 hover:text-blue-800"><FiEdit2 /></button>
                  <button onClick={() => handleDelete(b.id)} className="text-red-600 hover:text-red-800"><FiTrash2 /></button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </Layout>
  );
}
