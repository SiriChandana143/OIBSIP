import { Link } from 'react-router-dom';
import { FiBook, FiSearch, FiStar, FiMessageCircle, FiShield, FiUsers } from 'react-icons/fi';

export default function Landing() {
  return (
    <div className="min-h-screen">
      {/* Hero */}
      <header className="bg-gradient-to-br from-primary-700 via-primary-800 to-accent-600 text-white">
        <nav className="max-w-7xl mx-auto px-6 py-4 flex justify-between items-center">
          <h1 className="text-2xl font-bold">SMARTLIB AI</h1>
          <div className="space-x-4">
            <Link to="/login" className="px-4 py-2 rounded-lg hover:bg-white/10 transition">Login</Link>
            <Link to="/register" className="px-4 py-2 bg-white text-primary-700 rounded-lg font-medium hover:bg-gray-100 transition">Register</Link>
          </div>
        </nav>
        <div className="max-w-7xl mx-auto px-6 py-24 text-center">
          <h2 className="text-5xl font-bold mb-6">Intelligent Digital Library Management</h2>
          <p className="text-xl text-primary-100 mb-8 max-w-2xl mx-auto">
            Discover, borrow, and manage books with AI-powered recommendations and an intelligent library assistant.
          </p>
          <div className="space-x-4">
            <Link to="/register" className="inline-block px-8 py-3 bg-white text-primary-700 rounded-lg font-semibold hover:bg-gray-100 transition text-lg">Get Started</Link>
            <Link to="/login" className="inline-block px-8 py-3 border-2 border-white rounded-lg font-semibold hover:bg-white/10 transition text-lg">Sign In</Link>
          </div>
        </div>
      </header>

      {/* Features */}
      <section className="max-w-7xl mx-auto px-6 py-20">
        <h3 className="text-3xl font-bold text-center mb-12">Why SMARTLIB AI?</h3>
        <div className="grid md:grid-cols-3 gap-8">
          {[
            { icon: FiSearch, title: 'Smart Search', desc: 'Search books by title, author, ISBN, or category with intelligent filtering.' },
            { icon: FiStar, title: 'AI Recommendations', desc: 'Personalized book suggestions based on your reading history and preferences.' },
            { icon: FiMessageCircle, title: 'AI Assistant', desc: 'Chat with our intelligent library bot for instant help with books, due dates, and fines.' },
            { icon: FiBook, title: 'Digital Borrowing', desc: 'Borrow and return books seamlessly with automatic fine calculation.' },
            { icon: FiShield, title: 'Secure Access', desc: 'JWT-based authentication with role-based access control for admins and users.' },
            { icon: FiUsers, title: 'Multi-Role System', desc: 'Dedicated dashboards for Admin, Librarian, and User roles.' },
          ].map(({ icon: Icon, title, desc }) => (
            <div key={title} className="card text-center">
              <div className="w-14 h-14 bg-primary-100 rounded-xl flex items-center justify-center mx-auto mb-4">
                <Icon size={28} className="text-primary-600" />
              </div>
              <h4 className="text-lg font-semibold mb-2">{title}</h4>
              <p className="text-gray-600">{desc}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-gray-400 py-8 text-center">
        <p>&copy; 2024 SMARTLIB AI - Intelligent Digital Library Management System</p>
      </footer>
    </div>
  );
}
