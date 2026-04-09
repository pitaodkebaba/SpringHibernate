import { useState } from 'react';

export default function Login({ onLoginSuccess }) {
  // Stany odpowiadające polom z LoginUserDto
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch('/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        // Tworzymy JSON zgodny z Twoim LoginUserDto
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();
        // Zapisujemy token JWT do localStorage, aby używać go w przyszłych zapytaniach
        localStorage.setItem('token', data.token);
        setMessage('Login successful!');
        if (onLoginSuccess) onLoginSuccess();
      } else {
        setMessage('Invalid username or password.');
      }
    } catch (error) {
      console.error('Error connecting:', error);
      setMessage('Error connecting to the server.');
    }
  };

  return (
    <div style={{ maxWidth: '300px', margin: 'auto', padding: '20px' }}>
      <h2>Login</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <button type="submit">Login</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}