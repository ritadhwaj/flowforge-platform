import { useEffect, useState } from 'react';
import API from './services/api';

function App() {
  const [message, setMessage] = useState('Loading...');

  useEffect(() => {
    API.get('/health')
      .then((res) => setMessage(res.data))
      .catch((err) => setMessage('Error: ' + err.message));
  }, []);

  return (
    <div style={{ padding: '40px', fontFamily: 'Arial' }}>
      <h1>FlowForge Platform</h1>
      <p>Backend says: {message}</p>
    </div>
  );
}

export default App;