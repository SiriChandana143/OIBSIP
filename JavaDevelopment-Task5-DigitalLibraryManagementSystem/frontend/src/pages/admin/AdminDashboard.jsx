import { useEffect, useState } from 'react';
import Layout from '../../components/Layout';
import API from '../../services/api';
import { Chart as ChartJS, ArcElement, BarElement, LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend } from 'chart.js';
import { Pie, Bar, Line } from 'react-chartjs-2';

ChartJS.register(ArcElement, BarElement, LineElement, PointElement, CategoryScale, LinearScale, Tooltip, Legend);

const MONTHS = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

export default function AdminDashboard() {
  const [analytics, setAnalytics] = useState(null);

  useEffect(() => {
    API.get('/admin/analytics').then(res => setAnalytics(res.data.data)).catch(() => {});
  }, []);

  if (!analytics) return <Layout><div className="flex justify-center py-12"><div className="animate-spin rounded-full h-10 w-10 border-b-2 border-primary-600"></div></div></Layout>;

  const statCards = [
    { label: 'Total Books', value: analytics.totalBooks, color: 'from-blue-500 to-blue-600' },
    { label: 'Total Users', value: analytics.totalUsers, color: 'from-green-500 to-green-600' },
    { label: 'Issued Books', value: analytics.issuedBooks, color: 'from-orange-500 to-orange-600' },
    { label: 'Available Books', value: analytics.availableBooks, color: 'from-purple-500 to-purple-600' },
    { label: 'Overdue Books', value: analytics.overdueBooks, color: 'from-red-500 to-red-600' },
    { label: 'Pending Fines', value: analytics.pendingFines, color: 'from-yellow-500 to-yellow-600' },
  ];

  const pieData = {
    labels: ['Issued', 'Available', 'Overdue'],
    datasets: [{ data: [analytics.issuedBooks, analytics.availableBooks, analytics.overdueBooks], backgroundColor: ['#3b82f6', '#22c55e', '#ef4444'] }]
  };

  const barData = {
    labels: ['Books', 'Users', 'Issued', 'Available', 'Overdue', 'Fines'],
    datasets: [{ label: 'Library Stats', data: [analytics.totalBooks, analytics.totalUsers, analytics.issuedBooks, analytics.availableBooks, analytics.overdueBooks, analytics.totalFines], backgroundColor: '#3b82f6' }]
  };

  const lineData = {
    labels: MONTHS,
    datasets: [{
      label: 'Books Borrowed',
      data: analytics.monthlyBorrows || MONTHS.map(() => 0),
      borderColor: '#8b5cf6',
      backgroundColor: 'rgba(139, 92, 246, 0.1)',
      fill: true,
      tension: 0.3,
    }]
  };

  return (
    <Layout>
      <h1 className="text-2xl font-bold mb-6">Admin Analytics Dashboard</h1>
      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-8">
        {statCards.map(({ label, value, color }) => (
          <div key={label} className={`card bg-gradient-to-r ${color} text-white !p-5`}>
            <p className="text-3xl font-bold">{value}</p>
            <p className="text-sm opacity-90">{label}</p>
          </div>
        ))}
      </div>
      <div className="grid md:grid-cols-2 gap-6 mb-6">
        <div className="card"><h2 className="font-semibold mb-4">Book Distribution</h2><Pie data={pieData} /></div>
        <div className="card"><h2 className="font-semibold mb-4">Library Overview</h2><Bar data={barData} options={{ responsive: true, plugins: { legend: { display: false } } }} /></div>
      </div>
      <div className="card">
        <h2 className="font-semibold mb-4">Monthly Borrow Trends ({new Date().getFullYear()})</h2>
        <Line data={lineData} options={{ responsive: true, plugins: { legend: { display: false } }, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }} />
      </div>
    </Layout>
  );
}
