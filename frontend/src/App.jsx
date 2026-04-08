import { useState } from 'react';
import Login from './auth/Login';
import Register from './auth/Register';
import SongList from './SongList';

function App() {
  const [hasToken, setHasToken] = useState(!!localStorage.getItem('token'));

  const handleLoginSuccess = () => {
    setHasToken(true);
  };

  return (
    <div>
      {hasToken ? (
        <SongList />
      ) : (
        <div style={{ display: 'flex', justifyContent: 'space-around', marginTop: '50px' }}>
          <Register />
          <Login onLoginSuccess={handleLoginSuccess} />
        </div>
      )}
    </div>
  );
}

export default App;