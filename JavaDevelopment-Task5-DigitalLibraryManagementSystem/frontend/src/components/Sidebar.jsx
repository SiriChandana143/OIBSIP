import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { FiHome, FiBook, FiSearch, FiClock, FiBookmark, FiDollarSign, FiStar, FiMessageCircle, FiUsers, FiBarChart2, FiUser, FiLogOut, FiMenu, FiX } from 'react-icons/fi';
import { useState } from 'react';

export default function Sidebar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);

  const handleLogout = () => { logout(); navigate('/login'); };

  const userLinks = [
    { to: '/dashboard', icon: FiHome, label: 'Dashboard' },
    { to: '/books', icon: FiSearch, label: 'Search Books' },
    { to: '/borrow-history', icon: FiClock, label: 'Borrow History' },
    { to: '/reservations', icon: FiBookmark, label: 'Reservations' },
    { to: '/fines', icon: FiDollarSign, label: 'Fines' },
    { to: '/recommendations', icon: FiStar, label: 'Recommendations' },
    { to: '/chatbot', icon: FiMessageCircle, label: 'AI Assistant' },
    { to: '/profile', icon: FiUser, label: 'Profile' },
  ];

  const adminLinks = [
    { to: '/admin', icon: FiBarChart2, label: 'Analytics' },
    { to: '/admin/books', icon: FiBook, label: 'Manage Books' },
    { to: '/admin/users', icon: FiUsers, label: 'Manage Users' },
    { to: '/admin/borrows', icon: FiClock, label: 'Issue Management' },
    { to: '/admin/fines', icon: FiDollarSign, label: 'Fine Management' },
  ];

  const librarianLinks = [
    { to: '/dashboard', icon: FiHome, label: 'Dashboard' },
    { to: '/admin/borrows', icon: FiClock, label: 'Issue & Return' },
    ...userLinks.slice(1),
  ];

  const links = user.role === 'ADMIN' ? [...adminLinks, ...userLinks.slice(1)] :
    user.role === 'LIBRARIAN' ? librarianLinks : userLinks;

  const NavContent = () => (
    <>
      <div className="p-6 border-b border-gray-200">
        <h1 className="text-xl font-bold text-primary-700">SMARTLIB AI</h1>
        <p className="text-sm text-gray-500 mt-1">{user.name}</p>
        <span className="inline-block mt-1 px-2 py-0.5 text-xs rounded-full bg-primary-100 text-primary-700">{user.role}</span>
      </div>
      <nav className="flex-1 p-4 space-y-1">
        {links.map(({ to, icon: Icon, label }) => (
          <NavLink key={to} to={to} onClick={() => setOpen(false)}
            className={({ isActive }) => `flex items-center gap-3 px-4 py-2.5 rounded-lg transition-colors ${isActive ? 'bg-primary-50 text-primary-700 font-medium' : 'text-gray-600 hover:bg-gray-100'}`}>
            <Icon size={18} /> {label}
          </NavLink>
        ))}
      </nav>
      <div className="p-4 border-t border-gray-200">
        <button onClick={handleLogout} className="flex items-center gap-3 px-4 py-2.5 w-full rounded-lg text-red-600 hover:bg-red-50 transition-colors">
          <FiLogOut size={18} /> Logout
        </button>
      </div>
    </>
  );

  return (
    <>
      <button onClick={() => setOpen(!open)} className="lg:hidden fixed top-4 left-4 z-50 p-2 bg-white rounded-lg shadow-md">
        {open ? <FiX size={24} /> : <FiMenu size={24} />}
      </button>
      <aside className={`fixed inset-y-0 left-0 z-40 w-64 bg-white shadow-lg transform transition-transform lg:translate-x-0 ${open ? 'translate-x-0' : '-translate-x-full'} flex flex-col`}>
        <NavContent />
      </aside>
      {open && <div className="fixed inset-0 bg-black/30 z-30 lg:hidden" onClick={() => setOpen(false)} />}
    </>
  );
}
