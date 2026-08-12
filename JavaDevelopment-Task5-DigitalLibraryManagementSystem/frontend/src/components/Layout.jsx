import Sidebar from '../components/Sidebar';

export default function Layout({ children }) {
  return (
    <div className="min-h-screen bg-gray-50">
      <Sidebar />
      <main className="lg:ml-64 p-6 pt-16 lg:pt-6">{children}</main>
    </div>
  );
}
