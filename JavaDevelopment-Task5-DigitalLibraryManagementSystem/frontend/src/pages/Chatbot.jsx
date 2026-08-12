import { useState, useRef, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Layout from '../components/Layout';
import API from '../services/api';
import { FiSend, FiMessageCircle } from 'react-icons/fi';

export default function Chatbot() {
  const { user } = useAuth();
  const [messages, setMessages] = useState([
    { role: 'bot', text: 'Hello! I\'m your SMARTLIB AI assistant. How can I help you today?' }
  ]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);
  const bottomRef = useRef(null);

  useEffect(() => { bottomRef.current?.scrollIntoView({ behavior: 'smooth' }); }, [messages]);

  const sendMessage = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    const userMsg = input.trim();
    setInput('');
    setMessages(prev => [...prev, { role: 'user', text: userMsg }]);
    setLoading(true);

    try {
      const { data } = await API.post('/chat', { message: userMsg, userId: user.id });
      const reply = data.data;
      setMessages(prev => [...prev, {
        role: 'bot',
        text: reply.reply,
        books: reply.suggestedBooks
      }]);
    } catch {
      setMessages(prev => [...prev, { role: 'bot', text: 'Sorry, I encountered an error. Please try again.' }]);
    } finally {
      setLoading(false);
    }
  };

  const quickActions = [
    'Find machine learning books',
    'Show my borrowed books',
    'When should I return my book?',
    'Recommend programming books',
    'Show my fines'
  ];

  return (
    <Layout>
      <div className="max-w-3xl mx-auto">
        <div className="flex items-center gap-3 mb-6">
          <div className="w-10 h-10 bg-primary-600 rounded-full flex items-center justify-center">
            <FiMessageCircle className="text-white" size={20} />
          </div>
          <div>
            <h1 className="text-2xl font-bold">AI Library Assistant</h1>
            <p className="text-sm text-gray-500">Ask me anything about the library</p>
          </div>
        </div>

        <div className="card !p-0 flex flex-col h-[calc(100vh-220px)]">
          <div className="flex-1 overflow-y-auto p-4 space-y-4">
            {messages.map((msg, i) => (
              <div key={i} className={`flex ${msg.role === 'user' ? 'justify-end' : 'justify-start'}`}>
                <div className={`max-w-[80%] rounded-2xl px-4 py-3 ${msg.role === 'user' ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-800'}`}>
                  <p className="whitespace-pre-wrap text-sm">{msg.text}</p>
                  {msg.books && msg.books.length > 0 && (
                    <div className="mt-3 space-y-2">
                      {msg.books.map(book => (
                        <div key={book.id} className="bg-white rounded-lg p-2 flex items-center gap-2 text-gray-800">
                          <img src={book.imageUrl || 'https://via.placeholder.com/30x40'} alt="" className="w-8 h-10 object-cover rounded" />
                          <div>
                            <p className="text-xs font-medium">{book.title}</p>
                            <p className="text-xs text-gray-500">{book.author}</p>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                </div>
              </div>
            ))}
            {loading && (
              <div className="flex justify-start">
                <div className="bg-gray-100 rounded-2xl px-4 py-3">
                  <div className="flex gap-1">
                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce"></div>
                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.1s' }}></div>
                    <div className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0.2s' }}></div>
                  </div>
                </div>
              </div>
            )}
            <div ref={bottomRef} />
          </div>

          <div className="p-3 border-t">
            <div className="flex flex-wrap gap-2 mb-3">
              {quickActions.map(action => (
                <button key={action} onClick={() => { setInput(action); }} className="text-xs px-3 py-1 bg-gray-100 rounded-full hover:bg-gray-200 transition text-gray-600">
                  {action}
                </button>
              ))}
            </div>
            <form onSubmit={sendMessage} className="flex gap-2">
              <input type="text" className="input-field flex-1" placeholder="Type your message..." value={input} onChange={(e) => setInput(e.target.value)} />
              <button type="submit" disabled={loading} className="btn-primary !px-4"><FiSend /></button>
            </form>
          </div>
        </div>
      </div>
    </Layout>
  );
}
